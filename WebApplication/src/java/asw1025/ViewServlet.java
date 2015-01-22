package asw1025;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
import javax.xml.transform.TransformerException;
import asw1025.ManageXML;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ViewServlet extends HttpServlet {
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String fileSnippet = Util.getCorrectFilePath(this, "snippet.xml");
            HttpSession session = request.getSession();
            Document xmlSnippet = null;
            String username = (String) session.getAttribute("user");
            String idSnippet = request.getParameter("id");
            
            File fSnippet = new File(fileSnippet);
            
            ManageXML mngXML = new ManageXML();
            
            // Lettura esclusiva
            Util.mutexSnippetFile.acquire();
            
            // Caricamento xml Snippet
            DataInputStream dis = null;
            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(fileSnippet)));
            xmlSnippet = mngXML.parse(dis);
            dis.close();
         
            
            NodeList snippet = xmlSnippet.getDocumentElement().getChildNodes();
            ArrayList<SnippetData> IdSnippet= new ArrayList<>();
            
            // RICERCA negli Snippet e del nodo snippet da visualizzare
            for (int i = 0; i < snippet.getLength(); i++) {
                String id = snippet.item(i).getChildNodes().item(0).getTextContent();
                String thisUser = snippet.item(i).getChildNodes().item(1).getTextContent();
              
                if ((thisUser.equals(username)) && (id.equals(idSnippet))) {
                    SnippetData mysnippet =new SnippetData(snippet.item(i).getChildNodes().item(0).getTextContent(),
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

                        IdSnippet.add(mysnippet);
                    break;
                }
            }
               
            Util.mutexSnippetFile.release();
      
            request.setAttribute("IdSnippet", IdSnippet);
            RequestDispatcher rd = request.getRequestDispatcher("jsp/viewSnippet.jsp");
            rd.forward(request, response);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(ViewServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ViewServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(ViewServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(ViewServlet.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
}
