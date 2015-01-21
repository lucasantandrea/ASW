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
            String title = request.getParameter("title");
            String username = request.getParameter("author");
            String language = request.getParameter("languageResearch");
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
            for (int i = 0; i < snippet.getLength(); i++) {
                String thisUser = snippet.item(i).getChildNodes().item(1).getTextContent();
                String thisTitle = snippet.item(i).getChildNodes().item(2).getTextContent();
                String thisLen = snippet.item(i).getChildNodes().item(4).getTextContent();
                if (!title.equals("")) {
                    if (thisTitle.equals(title)) {
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
                }else{
                if (username.equals("") && !language.equals("---") && thisLen.equals(language)) {
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
                if (username != null & language.equals("---") & thisUser.equals(username)) {
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
               

                if (username != null & !language.equals("---") & thisUser.equals(username) & thisLen.equals(language)) {
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
