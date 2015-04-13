/*    
 Esame ASW 2014-2015
 Autori: Luca Santandrea, Matteo Mariani, Antonio Leo Folliero, Francesco Degli Angeli
 Gruppo: 1025
 */
package asw1025;

import java.io.FileInputStream;
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
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import javax.servlet.annotation.WebServlet;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

@WebServlet(name = "EditServlet", urlPatterns = {"/EditServlet"})
public class EditServlet extends HttpServlet {

    /**
     * Handles the HTTP <code>POST</code> method. Cerca i campi utili alla
     * modifica di uno snippet tramite l'ID inserendo i dati in un file XML
     *
     *
     * @param request servlet request: ID dello snippet da modificare, user:
     * usato per autenticarsi e per ottenere i permessi di modifica
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {

            RequestDispatcher rd = request.getRequestDispatcher("jsp/modify.jsp");
            rd.forward(request, response);
        } catch (Exception ex) {
            Logger.getLogger(DisplayServlet.class.getName()).log(Level.SEVERE, null, ex);
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
        response.sendRedirect(Util.BASE + "MySnippetServlet");
    }

}
