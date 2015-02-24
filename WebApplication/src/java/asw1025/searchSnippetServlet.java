/*    
 Esame ASW 2014-2015
 Autori: Luca Santandrea, Matteo Mariani, Antonio Leo Folliero, Francesco Degli Angeli
 Gruppo: 1025
 */
package asw1025;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import javax.servlet.annotation.WebServlet;
import org.w3c.dom.Document;
import org.w3c.dom.*;

@WebServlet(name = "SearchSnippetServlet", urlPatterns = {"/SearchSnippetServlet"})
public class SearchSnippetServlet extends HttpServlet {

    /**
     * Handles the HTTP <code>POST</code> method. Funzione che permette di
     * restituire gli snippet dell'utente loggato che effettua la richiesta.
     *
     * @param request servlet request: title,author,language usati come
     * parametri di ricerca e order per poter ordinare il risultato
     * @param response servlet response: dbsnippet usato per memorizzare i dati
     * d'intereasse per la visualizzazione del risultato
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String fileSnippet = Util.getCorrectFilePath(this, "snippet.xml");
            HttpSession session = request.getSession();
            //response.setContentType("text/xml;charset=UTF-8");
            OutputStream os = response.getOutputStream();
            //InputStream is = request.getInputStream();
            Document xmlSnippet = null;

            ManageXML mngXML = new ManageXML();

            String title = request.getParameter("title").toLowerCase();
            String username = request.getParameter("author").toLowerCase();
            String language = request.getParameter("language").toLowerCase();
            // Ordinamento
            String order = request.getParameter("order");

            File f = new File(fileSnippet);
            if (!f.exists()) {
                f.createNewFile();
                FileOutputStream fileOut = new FileOutputStream(f, false);
                PrintWriter writer = new PrintWriter(f);
                writer.write("<dbsnippet></dbsnippet>");
                writer.close();
                fileOut.close();
            }

            //Lettura esclusiva
            Util.mutexSnippetFile.acquire();

            // Caricamento xml
            DataInputStream dis = null;
            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(fileSnippet)));
            xmlSnippet = mngXML.parse(dis);
            dis.close();

            NodeList snippet = xmlSnippet.getDocumentElement().getChildNodes();
            ArrayList<SnippetData> mySnippet = new ArrayList<>();
            // Ricerca degli snippet dell'utente indicato
            boolean searchByTitle = false;
            boolean searchByUsername = false;
            boolean searchByLanguage = false;

            if (!(title == null || title.equals(""))) {
                searchByTitle = true;
            }
            if (!(username == null || username.equals(""))) {
                searchByUsername = true;
            }
            if (!(language == null || language.equals("---"))) {
                searchByLanguage = true;
            }

            for (int i = 0; i < snippet.getLength(); i++) {
                String thisUser = snippet.item(i).getChildNodes().item(1).getTextContent().toLowerCase();
                String thisTitle = snippet.item(i).getChildNodes().item(2).getTextContent().toLowerCase();
                String thisLen = snippet.item(i).getChildNodes().item(4).getTextContent().toLowerCase();

                boolean FilterTitle = true;
                boolean FilterUsername = true;
                boolean FilterLanguage = true;

                //controllo se confrontare il titolo
                if (searchByTitle == true) {
                    if (!thisTitle.contains(title)) {
                        FilterTitle = false;
                    }
                }

                //controllo se confrontare l'autore
                if (searchByUsername == true) {
                    if (!thisUser.contains(username)) {
                        FilterUsername = false;
                    }
                }

                //controllo se confrontare il linguaggio
                if (searchByLanguage == true) {
                    if (!thisLen.contains(language)) {
                        FilterLanguage = false;
                    }
                }

                //ricerca sulla base dei boolean valorizzati in precedenza
                if (FilterTitle == true && FilterUsername == true && FilterLanguage == true) {
                    SnippetData mysnippet = new SnippetData(snippet.item(i).getChildNodes().item(0).getTextContent(),
                            snippet.item(i).getChildNodes().item(1).getTextContent(),
                            snippet.item(i).getChildNodes().item(2).getTextContent(),
                            snippet.item(i).getChildNodes().item(3).getTextContent(),
                            snippet.item(i).getChildNodes().item(4).getTextContent(),
                            snippet.item(i).getChildNodes().item(5).getTextContent(),
                            snippet.item(i).getChildNodes().item(6).getTextContent(),
                            snippet.item(i).getChildNodes().item(7).getTextContent(),
                            snippet.item(i).getChildNodes().item(8).getTextContent(),
                            snippet.item(i).getChildNodes().item(9).getTextContent(),
                            snippet.item(i).getChildNodes().item(10).getTextContent(),
                            snippet.item(i).getChildNodes().item(10).getTextContent());

                    mySnippet.add(mysnippet);
                }
            }
            // ORDINAMENTO SU ENTRAMBI I CRITERI ----------------------------------------------------------------------------------------------
            if (!order.equals("---")) {
                if (order.equals("Creation Data")) {
                    // ORDINAMENTO PER DATA
                    Collections.sort(mySnippet, new Comparator<SnippetData>() {

                        @Override
                        public int compare(SnippetData o1, SnippetData o2) {

                            Date d1 = null;
                            Date d2 = null;
                            try {

                                d1 = Util.convertStringtoDate(o1.getDateCreation());
                                d2 = Util.convertStringtoDate(o2.getDateCreation());

                            } catch (ParseException ex) {
                                Logger.getLogger(SearchSnippetServlet.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            return d2.compareTo(d1); //ordine decrescente
                        }
                    });
                }
                if (order.equals("Owner Update Data")) {
                    // ORDINAMENTO PER DATA
                    Collections.sort(mySnippet, new Comparator<SnippetData>() {

                        @Override
                        public int compare(SnippetData o1, SnippetData o2) {

                            Date d1 = null;
                            Date d2 = null;
                            try {

                                d1 = Util.convertStringtoDate(o1.getDateLastModProp());
                                d2 = Util.convertStringtoDate(o2.getDateLastModProp());

                            } catch (ParseException ex) {
                                Logger.getLogger(SearchSnippetServlet.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            return d2.compareTo(d1); //ordine decrescente
                        }
                    });
                }
                if (order.equals("Users Update Data")) {
                    // ORDINAMENTO PER DATA
                    Collections.sort(mySnippet, new Comparator<SnippetData>() {

                        @Override
                        public int compare(SnippetData o1, SnippetData o2) {

                            Date d1 = null;
                            Date d2 = null;
                            try {

                                d1 = Util.convertStringtoDate(o1.getDateLastMod());
                                d2 = Util.convertStringtoDate(o2.getDateLastMod());

                            } catch (ParseException ex) {
                                Logger.getLogger(SearchSnippetServlet.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            return d2.compareTo(d1); //ordine decrescente
                        }
                    });
                }
            }

            // Creazione e invio XML di risposta con la lista di Snippet ricercate
            Document answer = mngXML.newDocument();
            Element risp = answer.createElement("dbsnippet");

            for (SnippetData snippetData : mySnippet) {

                Element id = answer.createElement("idSnippet");
                Element creatorElement = answer.createElement("creator");
                Element titleElement = answer.createElement("title");
                Element codeElement = answer.createElement("code");
                Element languageElement = answer.createElement("language");
                Element dateCreationElement = answer.createElement("dateCreation");
                Element modElement = answer.createElement("mod");
                Element codeModElement = answer.createElement("codeMod");
                Element userModElement = answer.createElement("userMod");
                Element lastUserModElement = answer.createElement("lastUserMod");
                Element dateLastModPropElement = answer.createElement("dateLastModProp");
                Element dateLastModElement = answer.createElement("dateLastMod");

                id.setTextContent(snippetData.getId());
                creatorElement.setTextContent(snippetData.getCreator());
                titleElement.setTextContent(snippetData.getTitle());
                codeElement.setTextContent(snippetData.getCode());
                languageElement.setTextContent(snippetData.getLanguage());
                dateCreationElement.setTextContent(snippetData.getDateCreation());
                modElement.setTextContent(snippetData.getMod());
                codeModElement.setTextContent(snippetData.getCodeMod());
                userModElement.setTextContent(snippetData.getUserMod());
                lastUserModElement.setTextContent(snippetData.getLastUserMod());
                dateLastModPropElement.setTextContent(snippetData.getDateLastModProp());
                dateLastModElement.setTextContent(snippetData.getDateLastMod());

                Element snippetos = answer.createElement("snippet");

                snippetos.appendChild(id);
                snippetos.appendChild(creatorElement);
                snippetos.appendChild(titleElement);
                snippetos.appendChild(codeElement);
                snippetos.appendChild(languageElement);
                snippetos.appendChild(dateCreationElement);
                snippetos.appendChild(modElement);
                snippetos.appendChild(codeModElement);
                snippetos.appendChild(userModElement);
                snippetos.appendChild(lastUserModElement);
                snippetos.appendChild(dateLastModPropElement);
                snippetos.appendChild(dateLastModElement);

                risp.appendChild(snippetos);
            }

            // Rilascio risorsa condivisa
            Util.mutexSnippetFile.release();

            answer.appendChild(risp);
            mngXML.transform(os, answer);

        } catch (Exception ex) {
            Logger.getLogger(SearchSnippetServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
