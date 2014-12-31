/*    
    Esame ASW 2014-2015
    Autori: Luca Santandrea
    Matricola: 0900050785
*/

package asw1025;

        
import asw1025.Util;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.AsyncContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
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
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class RegistrationServletOld extends HttpServlet {
    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    /*
        Funzione di REGISTRAZIONE.
        Permette ad un nuovo utente del sito, di registrare (nome, cognome, username e password) controllando
        eventuali conflitti di username. (dbpersone)
        Inoltre effettua direttamente la Login del nuovo utente (settando gli attributi della sessione),
        e notificando agli utenti registrati al servizio di notifica.
    */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
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
            
            // Lettura esclusiva
            Util.mutexPersoneFile.acquire();
            
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

                nome.setTextContent(request.getParameter("firstname"));
                cognome.setTextContent(request.getParameter("lastname"));
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
                session.setAttribute("nome", request.getParameter("firstname"));
                session.setAttribute("cognome", request.getParameter("lastname"));
                request.removeAttribute("errorUserRegistration");
                session.removeAttribute("errrorLogin");
                
                ServletContext application=getServletContext();
                Map<String,AsyncContext> hashMapAsyncContexts;
                
                Util.mutexAsyncContextList.acquire();
                
                if (application.getAttribute("asyncContexts")==null) {   //non esiste quindi non c'è nessun altro loggato quindì creo la struttura per memorizzare il context associato all'utente
                    hashMapAsyncContexts = new HashMap<>();
                }else{
                    hashMapAsyncContexts = (Map<String,AsyncContext>) application.getAttribute("asyncContexts");
                }
                //creo un campo fittizzio nell'hashmap relativo all'utente su cui verrà poi inserito il context
                hashMapAsyncContexts.put(((String)session.getAttribute("user")), null); 
                
                application.setAttribute("asyncContexts", hashMapAsyncContexts);
                                
                String txtResponse= "("+hashMapAsyncContexts.size()+"): ? ";
                for (Map.Entry<String, AsyncContext> entry : hashMapAsyncContexts.entrySet()) {   
                    
                    txtResponse= txtResponse+entry.getKey()+", ";
                    
                }
                
                //notifica agli utenti del numero di utenti loggati modificato
                for (Map.Entry<String, AsyncContext> entry : hashMapAsyncContexts.entrySet()) {   
                    // Se non è asyncContext dell'utente loggato notifico e l'utente in questione ha un async assegnato
                    if ((!(entry.getKey().equals(session.getAttribute("user"))))&&(entry.getValue()!=null)) {   
                        AsyncContext asyncContext = entry.getValue();
                        try {
                            PrintWriter writer = asyncContext.getResponse().getWriter();
                            writer.println(txtResponse);
                            writer.close();
                            asyncContext.complete();
                        } catch (Exception ex) { ex.printStackTrace(); }
                    }
                }
                
                Util.mutexAsyncContextList.release();
                
                // Rilascio risorsa condivisa
                Util.mutexPersoneFile.release();
                
                RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
                rd.forward(request, response);
            // Se l'username scelto è già stato registrato
            }else{
                
                // Rilascio risorsa condivisa
                Util.mutexPersoneFile.release();
                
                request.setAttribute("errorUserRegistration", username);
                request.setAttribute("nome", request.getParameter("firstname"));
                request.setAttribute("cognome", request.getParameter("lastname"));
                RequestDispatcher rd = request.getRequestDispatcher("jsp/registration.jsp");
                rd.forward(request, response);
            }
            
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(RegistrationServletOld.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(RegistrationServletOld.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(RegistrationServletOld.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(RegistrationServletOld.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(RegistrationServletOld.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}