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
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;
import org.w3c.dom.Document;
import org.w3c.dom.*;

@WebServlet(name = "StartSnippetServlet", urlPatterns = {"/StartSnippetServlet"})
public class StartSnippetServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods. Permette di visualizzare tutti gli snippet del data base a tutti
     * gli utenti compresi quelli che non sono loggati
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String fileSnippet = Util.getCorrectFilePath(this, "snippet.xml");
            Document xmlSnippet = null;

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
            // Ricerca di tutti gli snippet inseriti
            for (int i = snippet.getLength() - 1; i > - 1; i--) {

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
                        snippet.item(i).getChildNodes().item(11).getTextContent());

                mySnippet.add(mysnippet);
            }

            // Rilascio risorsa condivisa
            Util.mutexSnippetFile.release();

            request.setAttribute("mySnippet", mySnippet);
            RequestDispatcher rd = request.getRequestDispatcher("jsp/totalSnippet.jsp");
            rd.forward(request, response);

        } catch (Exception ex) {
            Logger.getLogger(StartSnippetServlet.class.getName()).log(Level.SEVERE, null, ex);
            Util.mutexSnippetFile.release();
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
        return "Snippet per ottenere tutti gli snippet attuali";
    }// </editor-fold>
}
