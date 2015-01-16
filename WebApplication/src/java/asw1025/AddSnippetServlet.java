/*    
    Esame ASW 2014-2015
    Autori: Luca Santandrea
    Matricola: 0900050785
*/
package asw1025;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import asw1025.ManageXML;
import java.util.ArrayList;
import javax.servlet.ServletContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@WebServlet(urlPatterns = {"/AddSnippetServlet"})
public class AddSnippetServlet extends HttpServlet {
    
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    /*
        Funzione che permette di salvare snippet dell'utente loggato nel dbsnippet.
    */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();        
        response.setContentType("text/xml;charset=UTF-8");
        try {
            ManageXML mngXML = new ManageXML();
            String fileSnippet = Util.getCorrectFilePath(this, "snippet.xml");
            String username = (String) session.getAttribute("user");
            Document xmlSnippet = null;
            DataOutputStream dos = null;
            
            File f = new File(fileSnippet);
            if (!f.exists()) {
                f.createNewFile();
                FileOutputStream fileOut = new FileOutputStream(f, false);
                PrintWriter writer = new PrintWriter(f);
                writer.write("<dbsnippet></dbsnippet>");
                writer.close();
                fileOut.close();
            }
            
            // Lettura esclusiva
            Util.mutexSnippetFile.acquire();
            
            // Caricamento xml
            DataInputStream dis = null;
            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(fileSnippet)));
            xmlSnippet = mngXML.parse(dis);
            dis.close();
            
            NodeList snippet = xmlSnippet.getDocumentElement().getChildNodes();
            
            // CREAZIONE di un nuovo snippet
            int idSnippet;
              
            // Non ci sono altri snippet (id=1)
            if (snippet.getLength()==0) {
                idSnippet = 1;
                // Aggiorno ID del nuovo snippet in modo incrementale
            }else{
                 idSnippet = Integer.parseInt(snippet.item(snippet.getLength()-1).getChildNodes().item(0).getTextContent()) + 1;
            }                         
                                   
                
            Element id = xmlSnippet.createElement("idSnippet");          
            Element user = xmlSnippet.createElement("user");
            Element title = xmlSnippet.createElement("title");
            Element language = xmlSnippet.createElement("language");
            Element code = xmlSnippet.createElement("code");
            Element date_creation = xmlSnippet.createElement("date_creation");
            Element mod = xmlSnippet.createElement("mod");
            Element code_mod = xmlSnippet.createElement("code_mod");
            Element date_lastmodprop = xmlSnippet.createElement("date_lastmodprop");
            Element date_lastmod = xmlSnippet.createElement("date_lastmod");
            Element user_mod = xmlSnippet.createElement("user_mod");
                     
            id.setTextContent(""+idSnippet);
            user.setTextContent(""+username);
            title.setTextContent(request.getParameter("Title"));
            code.setTextContent(request.getParameter("Code"));
            language.setTextContent(request.getParameter("languageResearch"));
            date_creation.setTextContent(Util.convertDateToString(new Date()));
            mod.setTextContent("");
            code_mod.setTextContent("");
            date_lastmodprop.setTextContent("");
            date_lastmod.setTextContent("");
            user_mod.setTextContent("");
            
            Element snip = xmlSnippet.createElement("snippet");
            snip.appendChild(id);
            snip.appendChild(user);
            snip.appendChild(title);
            snip.appendChild(code);
            snip.appendChild(language);
            snip.appendChild(date_creation);
            snip.appendChild(mod);
            snip.appendChild(code_mod);
            snip.appendChild(date_lastmodprop);
            snip.appendChild(date_lastmod);
            snip.appendChild(user_mod);
            
           
            xmlSnippet.getDocumentElement().appendChild(snip);
            
            
            dos=new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fileSnippet)));
            mngXML.transform(dos, xmlSnippet);
                  
              
            // Rilascio risora condivisa
            Util.mutexSnippetFile.release();
                     
            
            RequestDispatcher rd = request.getRequestDispatcher("MySnippetServlet");
            rd.forward(request, response);
               
            
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(AddSnippetServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(AddSnippetServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(AddSnippetServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(AddSnippetServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(AddSnippetServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
