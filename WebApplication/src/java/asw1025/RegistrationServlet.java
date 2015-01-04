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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.AsyncContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Luca
 */
@WebServlet(name = "RegistrationServlet", urlPatterns = {"/RegistrationServlet"})
public class RegistrationServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            //TODO: definire real path!
            //String filePersone = getServletContext().getRealPath("") + "\\persone.xml";
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
            
            //TODO: manca mutex per lettura esclusiva del file!!
            
            
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
                
                // Salvataggio username relativo alla sessione
                HttpSession session=request.getSession();   
                session.setAttribute("user", username);
                session.setAttribute("nome", request.getParameter("nome"));
                session.setAttribute("cognome", request.getParameter("cognome"));
                request.removeAttribute("errorUserRegistration");
                session.removeAttribute("errrorLogin");
                
                ServletContext application=getServletContext();
                Map<String,AsyncContext> hashMapAsyncContexts;
                
                //TODO: assegnamento asynccontext a ogni utente e segnalazione agli altri
                
                RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
                rd.forward(request, response);
            }
            else{
                
                // TODO: Rilascio risorsa condivisa                
                request.setAttribute("errorUserRegistration", username);
                request.setAttribute("nome", request.getParameter("firstname"));
                request.setAttribute("cognome", request.getParameter("lastname"));
                RequestDispatcher rd = request.getRequestDispatcher("jsp/registration.jsp");
                rd.forward(request, response);
            }
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            ////////////////////////////////////////////
            /*ServletContext application = getServletContext();
            application.setAttribute("nome",request.getParameter("nome"));
            application.setAttribute("cognome",request.getParameter("cognome"));
            application.setAttribute("user",request.getParameter("user"));
            application.setAttribute("pass",request.getParameter("pass"));
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Registrazione</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Risultato Registrazione</h1>");
            out.println("<p>Dati registrazione inviati:</p>");
            out.println("nome:" + request.getParameter(("nome")) + "<br/>");
            out.println("cognome:" + request.getParameter(("cognome")) + "<br/>");
            out.println("user:" + request.getParameter(("user")) + "<br/>");
            out.println("pass:" + request.getParameter(("pass")) + "<br/>");
            out.println("<p><a href=\"index.jsp\" >Home</a></p>");
            out.println("</body>");
            out.println("</html>");*/
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(RegistrationServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(RegistrationServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(RegistrationServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(RegistrationServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

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
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
