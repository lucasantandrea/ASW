/*    
    Esame ASW 2014-2015
    Autori: Luca Santandrea
    Matricola: 0900050785
*/
package asw1025;

import asw1025_lib.ManageXML;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Luca
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

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
            
            
            //String filePersone = getServletContext().getRealPath("") + "\\persone.xml";
            String filePersone = Util.getCorrectFilePath(this, "persone.xml");
            Document xmlPersone = null;
            String username = request.getParameter("user");
            String password = request.getParameter("pass");
            ManageXML mngXML = new ManageXML();
            
            File f = new File(filePersone);
            if (!f.exists()) {
                f.createNewFile();
                FileOutputStream fileOut = new FileOutputStream(f, false);
                PrintWriter writer = new PrintWriter(f);
                writer.write("<dbpersone></dbpersone>");
                writer.close();
                fileOut.close();
            }
            
            //TODO:  semafori mutex per lettura esclusiva
            
            // Caricamento xml
            DataInputStream dis = null;
            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(filePersone)));
            xmlPersone = mngXML.parse(dis);
            dis.close();
            
            
            NodeList persone = xmlPersone.getDocumentElement().getChildNodes();
            String nome="";
            String cognome="";
            Boolean trovato = false;
            
            // RICERCA UTENTE
            for (int i = 0; i < persone.getLength(); i++) {   
                String thisUser = persone.item(i).getChildNodes().item(2).getTextContent();
                String thisPass = persone.item(i).getChildNodes().item(3).getTextContent();
                if ((thisUser.equals(username)) && (thisPass.equals(password))) {
                    trovato = true;
                    nome=persone.item(i).getChildNodes().item(0).getTextContent();
                    cognome=persone.item(i).getChildNodes().item(1).getTextContent();
                    break;
                }
            }
            
            
            if (trovato) {
                HttpSession session=request.getSession(); // Salvataggio dati relativi alla sessione
                session.setAttribute("user", username);
                session.setAttribute("nome", nome);
                session.setAttribute("cognome", cognome);
                session.removeAttribute("errrorLogin");
                
                //TODO: assegnamento asynccontext a ogni utente e segnalazione agli altri
            }
            else{
                HttpSession session=request.getSession();   //salvataggio errore nella sessione
                session.setAttribute("errrorLogin", "errore");
            }
            
            RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
            rd.forward(request, response);
            
            //TODO: rilascio mutex risorse xml condivise
            
            /////////////////////////////////////////////////////////
            /*
            ServletContext application = getServletContext();
            String user=(String)application.getAttribute("user");
            String pass=(String)application.getAttribute("pass");
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Risultato Login</title>");            
            out.println("</head>");
            out.println("<body>");
            
            if(user!=null && pass!=null && user.equals(request.getParameter("user")) && pass.equals(request.getParameter("pass"))){
                HttpSession session=request.getSession();
                session.setAttribute("nome", application.getAttribute("nome")+" "+application.getAttribute("cognome"));
            
                out.println("<h1>Login effettuato con successo per " + application.getAttribute("nome") + " " + application.getAttribute("cognome") +"</h1>");
            
            }
            else{
                out.println("<h1>Login errata</h1>");    
            }
          
            out.println("<p><a href=\"index.jsp\" >Home</a></p>");
            out.println("</body>");
            out.println("</html>");
            */
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
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
