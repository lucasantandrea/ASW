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
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import asw1025.ManageXML;
import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import org.w3c.dom.Document;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class searchSnippetServlet extends HttpServlet {

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */

    /*
     Funzione che permette di restituire gli snippet dell'utente loggato che effettua la richiesta.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String fileSnippet = Util.getCorrectFilePath(this, "snippet.xml");
            HttpSession session = request.getSession();
            Document xmlSnippet = null;
            String title = request.getParameter("title").toLowerCase();
            String username = request.getParameter("author").toLowerCase();
            String language = request.getParameter("languageResearch").toLowerCase();
            
            
            // Ordinamento
            String order =  request.getParameter("orderResearch");
            
            File f = new File(fileSnippet);
            if (!f.exists()) {
                f.createNewFile();
                FileOutputStream fileOut = new FileOutputStream(f, false);
                PrintWriter writer = new PrintWriter(f);
                writer.write("<dbsnippet></dbsnippet>");
                writer.close();
                fileOut.close();
            }

            ManageXML mngXML = new ManageXML();

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

            if(!(title==null || title.equals(""))){
                searchByTitle=true;
            }
            if(!(username==null || username.equals(""))){
                searchByUsername=true;
            }
            if(!(language==null || language.equals("---"))){
                searchByLanguage=true;
            }
                
            for (int i = 0; i < snippet.getLength(); i++) {
                String thisUser = snippet.item(i).getChildNodes().item(1).getTextContent().toLowerCase();
                String thisTitle = snippet.item(i).getChildNodes().item(2).getTextContent().toLowerCase();
                String thisLen = snippet.item(i).getChildNodes().item(4).getTextContent().toLowerCase();

                boolean FilterTitle=true;
                boolean FilterUsername=true;
                boolean FilterLanguage=true;
                
                //controllo se confrontare il titolo
                if(searchByTitle==true){
                    if(!thisTitle.contains(title)){
                        FilterTitle=false;
                    }
                }
                
                //controllo se confrontare l'autore
                if(searchByUsername==true){
                    if(!thisUser.contains(username)){
                        FilterUsername=false;
                    }
                }
                
                //controllo se confrontare il linguaggio
                if(searchByLanguage==true){
                    if(!thisLen.contains(language)){
                        FilterLanguage=false;
                    }
                }
                
                //ricerca sulla base dei boolean valorizzati in precedenza
                if (FilterTitle==true && FilterUsername==true && FilterLanguage == true) {
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
                            snippet.item(i).getChildNodes().item(10).getTextContent());

                    mySnippet.add(mysnippet);
                }
            }
            // ORDINAMENTO SU ENTRAMBI I CRITERI ----------------------------------------------------------------------------------------------
            if (!order.equals("---")) {
                if(order.equals("Creation Data")){
                // ORDINAMENTO PER DATA
                Collections.sort(mySnippet,new Comparator<SnippetData>() {
                    
                    @Override
                    public int compare(SnippetData o1, SnippetData o2) {
                        
                        Date d1=null;
                        Date d2=null;
                        try {
                            
                            d1= Util.convertStringtoDate(o1.getDate_creation());
                            d2= Util.convertStringtoDate(o2.getDate_creation());
                            
                        } catch (ParseException ex) {
                            Logger.getLogger(searchSnippetServlet.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        return d2.compareTo(d1); //ordine decrescente
                    }
                });
                }
                 if(order.equals("Owner Update Data")){
                // ORDINAMENTO PER DATA
                Collections.sort(mySnippet,new Comparator<SnippetData>() {
                    
                    @Override
                    public int compare(SnippetData o1, SnippetData o2) {
                        
                        Date d1=null;
                        Date d2=null;
                        try {
                            
                            d1= Util.convertStringtoDate(o1.getDate_lasmodprop());
                            d2= Util.convertStringtoDate(o2.getDate_lasmodprop());
                            
                        } catch (ParseException ex) {
                            Logger.getLogger(searchSnippetServlet.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        return d2.compareTo(d1); //ordine decrescente
                    }
                });
                }
                  if(order.equals("Users Update Data")){
                // ORDINAMENTO PER DATA
                Collections.sort(mySnippet,new Comparator<SnippetData>() {
                    
                    @Override
                    public int compare(SnippetData o1, SnippetData o2) {
                        
                        Date d1=null;
                        Date d2=null;
                        try {
                            
                            d1= Util.convertStringtoDate(o1.getDate_lasmod());
                            d2= Util.convertStringtoDate(o2.getDate_lasmod());
                            
                        } catch (ParseException ex) {
                            Logger.getLogger(searchSnippetServlet.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        return d2.compareTo(d1); //ordine decrescente
                    }
                });
                }
            }
            // Rilascio risorsa condivisa
            Util.mutexSnippetFile.release();

            request.setAttribute("mySnippet", mySnippet);
            RequestDispatcher rd = request.getRequestDispatcher("jsp/showSearch.jsp");
            rd.forward(request, response);

        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(MySnippetServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(MySnippetServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(MySnippetServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(MySnippetServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
