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
import asw1025.ManageXML;
import asw1025.SnippetData;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.AsyncContext;
import javax.servlet.annotation.WebServlet;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

@WebServlet(name = "SearchSnippetServlet", urlPatterns = {"/SearchSnippetServlet"}, asyncSupported = true)
public class SearchSnippetServlet extends HttpServlet {

   
    /*
     Funzione che permette di restituire gli snippet dell'utente loggato che effettua la richiesta.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        InputStream is = request.getInputStream();
        HttpSession session = request.getSession();
        response.setContentType("text/xml;charset=UTF-8");
        OutputStream os = response.getOutputStream();      
            
        try {
            ManageXML mngXML = new ManageXML();
            Document data = mngXML.parse(is);
            is.close();
            
            Document answer= operations(data,session,mngXML);
            mngXML.transform(os, answer);
            os.close();
        }
        catch (Exception e){ 
            System.out.println(e);
        }

    }


    private Document operations(Document data, HttpSession session, ManageXML mngXML) {
        Document answer = mngXML.newDocument();
        Element rootResponse= answer.createElement("dbSnippet");
        
        String title = data.getDocumentElement().getChildNodes().item(0).getTextContent().toLowerCase();
        String username = data.getDocumentElement().getChildNodes().item(1).getTextContent().toLowerCase();
        String language = data.getDocumentElement().getChildNodes().item(2).getTextContent().toLowerCase();
        String order = data.getDocumentElement().getChildNodes().item(3).getTextContent().toLowerCase();
        
        try{
            // Lettura esclusiva
            Util.mutexSnippetFile.acquire();    
            //lettura da file xml
            String fileSnippet = Util.getCorrectFilePath(this, "snippet.xml");
            Document xmlSnippet = null;
            
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
                    if (!thisLen.equals(language)) {
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
                lastUserModElement.setTextContent(snippetData.getMod());
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

                rootResponse.appendChild(snippetos);
            }
            
            answer.appendChild(rootResponse);
            Util.mutexSnippetFile.release();
        }
        catch (Exception ex) {
            rootResponse.setTextContent("error");
            answer.appendChild(rootResponse);
            Logger.getLogger(ModifyServlet.class.getName()).log(Level.SEVERE, null, ex);
            Util.mutexSnippetFile.release();
        }
        
        return answer;
    }
}