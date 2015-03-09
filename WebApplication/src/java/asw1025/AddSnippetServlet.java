/*    
 Esame ASW 2014-2015
 Autori: Luca Santandrea, Matteo Mariani, Antonio Leo Folliero, Francesco Degli Angeli
 Matricola: 0900050785
 Gruppo: 1025
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/*    
 Esame ASW 2014-2015
 Autori: Luca Santandrea, Matteo Mariani, Antonio Leo Folliero, Francesco Degli Angeli
 Gruppo: 1025
 */
@WebServlet(name = "AddSnippetServlet", urlPatterns = {"/AddSnippetServlet"})
public class AddSnippetServlet extends HttpServlet {

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request: Titolo della snippet, linguaggio e
     * codice.
     * @param response servlet response: MySnippetServlet dispatcher
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     *
     * Funzione che permette di salvare snippet dell'utente loggato nel
     * dbsnippet.
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
            if (snippet.getLength() == 0) {
                idSnippet = 1;
                // Aggiorno ID del nuovo snippet in modo incrementale
            } else {
                idSnippet = Integer.parseInt(snippet.item(snippet.getLength() - 1).getChildNodes().item(0).getTextContent()) + 1;
            }

            Element idElement = xmlSnippet.createElement("idSnippet");
            Element creatorElement = xmlSnippet.createElement("creator");
            Element titleElement = xmlSnippet.createElement("title");
            Element languageElement = xmlSnippet.createElement("language");
            Element codeElement = xmlSnippet.createElement("code");
            Element dateCreationElement = xmlSnippet.createElement("dateCreation");
            Element modElement = xmlSnippet.createElement("mod");
            Element codeModElement = xmlSnippet.createElement("codeMod");
            Element userModElement = xmlSnippet.createElement("userMod");
            Element lastUserModElement = xmlSnippet.createElement("lastusermod");
            Element dateLastModPropElement = xmlSnippet.createElement("dateLastModProp");
            Element dateLastModElement = xmlSnippet.createElement("dateLastmod");

            idElement.setTextContent("" + idSnippet);
            creatorElement.setTextContent("" + username);
            titleElement.setTextContent(request.getParameter("title"));
            languageElement.setTextContent(request.getParameter("language"));
            codeElement.setTextContent(request.getParameter("code").replace("&#13;", "\r\n"));
            dateCreationElement.setTextContent(Util.convertDateToString(new Date()));
            modElement.setTextContent("N");
            codeModElement.setTextContent("");
            userModElement.setTextContent("");
            lastUserModElement.setTextContent("");
            dateLastModPropElement.setTextContent(Util.convertDateToString(new Date()));
            dateLastModElement.setTextContent(Util.convertDateToString(new Date()));

            Element snip = xmlSnippet.createElement("snippet");
            snip.appendChild(idElement);
            snip.appendChild(creatorElement);
            snip.appendChild(titleElement);
            snip.appendChild(codeElement);
            snip.appendChild(languageElement);
            snip.appendChild(dateCreationElement);
            snip.appendChild(modElement);
            snip.appendChild(codeModElement);
            snip.appendChild(userModElement);
            snip.appendChild(lastUserModElement);
            snip.appendChild(dateLastModPropElement);
            snip.appendChild(dateLastModElement);

            xmlSnippet.getDocumentElement().appendChild(snip);

            dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fileSnippet)));
            mngXML.transform(dos, xmlSnippet);

            // Rilascio risora condivisa
            Util.mutexSnippetFile.release();

            RequestDispatcher rd = request.getRequestDispatcher("MySnippetServlet");
            rd.forward(request, response);

        } catch (Exception ex) {
            Logger.getLogger(AddSnippetServlet.class.getName()).log(Level.SEVERE, null, ex);
            Util.mutexSnippetFile.release();
        }
    }

    /**
     * Processes requests for both HTTP <code>GET</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(Util.BASE + "index.jsp");
    }
}
