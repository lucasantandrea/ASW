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


@WebServlet(name = "RegistrationServlet", urlPatterns = {"/RegistrationServlet"})
public class RegistrationServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            String filePersone = Util.getCorrectFilePath(this, "persone.xml");
            String username = request.getParameter("user");
            Document xmlPersone = null;
            DataOutputStream dos = null;
            
            File f = new File(filePersone);
            if (!f.exists()) {
                    f.createNewFile();
                    FileOutputStream fileOut = new FileOutputStream(f, false);
                    PrintWriter writer = new PrintWriter(f);
                    writer.write("<dbpersone></dbpersone>");
                    writer.close();
                    fileOut.close();
            }
            
            ManageXML mngXML = new ManageXML();  
            
            Util.mutexSnippetFile.acquire();
            
            // Caricamento xml
            DataInputStream dis = null;
            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(filePersone)));
            xmlPersone = mngXML.parse(dis);
            dis.close();
            
            NodeList persone = xmlPersone.getDocumentElement().getChildNodes();
            
            // Ricerca di username omonimi
            Boolean conflittoUsername = false;
            for (int i = 0; i < persone.getLength(); i++) {
                String thisUser = persone.item(i).getChildNodes().item(2).getTextContent();
                if (thisUser.equals(username)) {
                    conflittoUsername = true;
                    break;
                }
            }
            
            // Se non c'è stato conflitto di username
            if (!conflittoUsername) {
                
                // In caso corretto, aggiunge i dati al file persone.xml
                dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(filePersone)));
                Element person = xmlPersone.createElement("persona");
                Element nome = xmlPersone.createElement("nome");
                Element cognome = xmlPersone.createElement("cognome");
                Element user = xmlPersone.createElement("username");
                Element pass = xmlPersone.createElement("password");
                
                nome.setTextContent(request.getParameter("nome"));
                cognome.setTextContent(request.getParameter("cognome"));
                user.setTextContent(request.getParameter("user"));
                pass.setTextContent(request.getParameter("pass"));
                
                person.appendChild(nome);
                person.appendChild(cognome);
                person.appendChild(user);
                person.appendChild(pass);
                xmlPersone.getDocumentElement().appendChild(person);
                mngXML.transform(dos, xmlPersone);
                dos.close();  
                
                // Salvataggio username relativo alla sessione (login automatico)
                HttpSession session=request.getSession();   
                session.setAttribute("user", username);
                session.setAttribute("nome", request.getParameter("nome"));
                session.setAttribute("cognome", request.getParameter("cognome"));
                request.removeAttribute("errorUserRegistration");
                
                //forwaring alla pagina iniziale
                RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
                rd.forward(request, response);
            }
            else{
                
                //mostro errore se username già esistente
                request.setAttribute("errorUserRegistration", username);
                request.setAttribute("nome", request.getParameter("nome"));
                request.setAttribute("cognome", request.getParameter("cognome"));
                RequestDispatcher rd = request.getRequestDispatcher("jsp/registration.jsp");
                rd.forward(request, response);
            }
            Util.mutexSnippetFile.release();

        } catch (Exception ex) {
            Logger.getLogger(RegistrationServlet.class.getName()).log(Level.SEVERE, null, ex);
            Util.mutexSnippetFile.release();
        } 
    }

    /**
     * Processes requests for both HTTP <code>GET</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(Util.BASE+"index.jsp");
    }

}
