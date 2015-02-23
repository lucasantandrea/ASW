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
import asw1025_lib.ManageXML;
import asw1025_lib.SnippetData;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.annotation.WebServlet;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@WebServlet(name = "ModifyServlet", urlPatterns = {"/ModifyServlet"})
public class ModifyServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        InputStream is = request.getInputStream();
        HttpSession session = request.getSession();
        response.setContentType("text/xml;charset=UTF-8");
        OutputStream os = response.getOutputStream();
        
        try {
            ManageXML mngXML = new ManageXML();
            Document data = mngXML.parse(is);
            is.close();
            
            Document answer= operations(data,session,mngXML);
            mngXML.transform(os, answer);
            os.close();
        }
        catch (Exception e){ 
            System.out.println(e);
        }
    }
    
    private Document operations(Document data, HttpSession session, ManageXML mngXML) {
        Document answer= null;
        answer = mngXML.newDocument();
        Element rootResponse= answer.createElement("snippet");
        
        try {
            // Lettura esclusiva
            Util.mutexSnippetFile.acquire();
            
            //lettura da file xml
            String fileSnippet = Util.getCorrectFilePath(this, "snippet.xml");
            Document xmlSnippet = null;
            
            DataInputStream dis = null;
            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(fileSnippet)));
            xmlSnippet = mngXML.parse(dis);
            dis.close();
            
            NodeList snippet = xmlSnippet.getDocumentElement().getChildNodes();
            
            Element rootRequest = data.getDocumentElement();
            //il nome dell'operazione richiesta è la radice del file xml
            String operationRequest = rootRequest.getTagName();
            switch (operationRequest) {
                case "getRequest":
                    
                    String idSnippet=rootRequest.getChildNodes().item(0).getTextContent();
                    String userRequester = rootRequest.getChildNodes().item(1).getTextContent();
                    
                    // Ricerca negli Snippet 
                    for (int i = 0; i < snippet.getLength(); i++) {
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
                        
                        if (mysnippet.getId().equals(idSnippet)) {
                            //se non disponibile alla modifica
                            if(!mysnippet.getUser_Mod().equals("") && !mysnippet.getUser_Mod().equals(userRequester)){
                                Element availableElement = answer.createElement("available");
                                availableElement.setTextContent("N");
                                rootResponse.appendChild(availableElement);
                            }
                            else{
                                //aggiungo a XML per la risposta
                                Element availableElement = answer.createElement("available");
                                Element idSnippetElement = answer.createElement("idSnippet");
                                Element creatorElement = answer.createElement("creator");
                                Element titleElement = answer.createElement("title");
                                Element codeElement = answer.createElement("code");
                                Element languageElement = answer.createElement("language");
                                Element date_creationElement = answer.createElement("date_creation");
                                Element modElement = answer.createElement("mod");
                                Element code_modElement = answer.createElement("code_mod");
                                Element user_modElement = answer.createElement("user_mod");
                                Element lastusermodElement = answer.createElement("lastusermod");
                                Element date_lastmodpropElement = answer.createElement("date_lastmodprop");
                                Element date_lastmodElement = answer.createElement("date_lastmod");

                                availableElement.setTextContent("Y");                                
                                idSnippetElement.setTextContent(mysnippet.getId());
                                creatorElement.setTextContent(mysnippet.getCreator());
                                titleElement.setTextContent(mysnippet.getTitle());
                                codeElement.setTextContent(mysnippet.getCode());
                                languageElement.setTextContent(mysnippet.getLanguage());
                                date_creationElement.setTextContent(mysnippet.getDate_creation());
                                modElement.setTextContent(mysnippet.getMod());
                                code_modElement.setTextContent(mysnippet.getCode_mod());
                                user_modElement.setTextContent(mysnippet.getUser_Mod());
                                lastusermodElement.setTextContent(mysnippet.getLastusermod());
                                date_lastmodpropElement.setTextContent(mysnippet.getDate_lastmodprop());
                                date_lastmodElement.setTextContent(mysnippet.getDate_lastmod());

                                rootResponse.appendChild(availableElement);
                                rootResponse.appendChild(idSnippetElement);
                                rootResponse.appendChild(creatorElement);
                                rootResponse.appendChild(titleElement);
                                rootResponse.appendChild(codeElement);
                                rootResponse.appendChild(languageElement);
                                rootResponse.appendChild(date_creationElement);
                                rootResponse.appendChild(modElement);
                                rootResponse.appendChild(code_modElement);
                                rootResponse.appendChild(user_modElement);
                                rootResponse.appendChild(lastusermodElement);
                                rootResponse.appendChild(date_lastmodpropElement);
                                rootResponse.appendChild(date_lastmodElement);
                                
                                //imposta l'utente corrente come modificatore del record                                
                                xmlSnippet.getChildNodes().item(0).getChildNodes().item(i).getChildNodes().item(8).setTextContent(userRequester);
                                
                                break;
                            }
                        }
                    }
                    break;
                case "setRequest":
                    //parametri in arrivo dalla request
                    SnippetData mysnippet = new SnippetData(rootRequest.getChildNodes().item(0).getTextContent(),
                        rootRequest.getChildNodes().item(1).getTextContent(),
                        rootRequest.getChildNodes().item(2).getTextContent(),
                        rootRequest.getChildNodes().item(3).getTextContent(),
                        rootRequest.getChildNodes().item(4).getTextContent(),
                        rootRequest.getChildNodes().item(5).getTextContent(),
                        rootRequest.getChildNodes().item(6).getTextContent(),
                        rootRequest.getChildNodes().item(7).getTextContent(),
                        rootRequest.getChildNodes().item(8).getTextContent(),
                        rootRequest.getChildNodes().item(9).getTextContent(),
                        rootRequest.getChildNodes().item(10).getTextContent(),
                        rootRequest.getChildNodes().item(11).getTextContent());
                                        
                    String content=rootRequest.getChildNodes().item(12).getTextContent();
                    String requestAuthor=rootRequest.getChildNodes().item(13).getTextContent();
                    
                    Element idSnippetElement = xmlSnippet.createElement("idSnippet");
                    idSnippetElement.setTextContent(mysnippet.getId());
                    Element creatorElement = xmlSnippet.createElement("creator");
                    creatorElement.setTextContent(mysnippet.getCreator());
                    Element titleElement = xmlSnippet.createElement("title");
                    titleElement.setTextContent(mysnippet.getTitle());
                    Element codeElement = xmlSnippet.createElement("code");
                    Element languageElement = xmlSnippet.createElement("language");
                    languageElement.setTextContent(mysnippet.getLanguage());
                    Element date_creationElement = xmlSnippet.createElement("date_creation");
                    date_creationElement.setTextContent(mysnippet.getDate_creation());
                    Element modElement = xmlSnippet.createElement("mod");
                    Element code_modElement = xmlSnippet.createElement("code_mod");
                    Element lastusermodElement = xmlSnippet.createElement("lastusermod");
                    Element date_lastmodpropElement = xmlSnippet.createElement("date_lastmodprop");
                    
                    //controllo se è una proposta di modifica o una modifica dell'autore
                    if(mysnippet.getCreator().equals(requestAuthor)){
                        codeElement.setTextContent(content);
                        code_modElement.setTextContent(mysnippet.getCode_mod());    
                        modElement.setTextContent(mysnippet.getMod());
                        lastusermodElement.setTextContent(mysnippet.getLastusermod());
                        date_lastmodpropElement.setTextContent(Util.convertDateToString(new Date()));
                    }else{
                        codeElement.setTextContent(mysnippet.getCode());
                        code_modElement.setTextContent(content);
                        modElement.setTextContent("Y");
                        lastusermodElement.setTextContent(requestAuthor);
                        date_lastmodpropElement.setTextContent(mysnippet.getDate_lastmodprop());
                    }
                    
                    Element user_modElement = xmlSnippet.createElement("user_mod");
                    //imposto a vuoto per liberare il "lock" del record
                    user_modElement.setTextContent("");
                    Element date_lastmodElement = xmlSnippet.createElement("date_lastmod");
                    date_lastmodElement.setTextContent(Util.convertDateToString(new Date()));
                    
                    //Sovrascrivo record nel database xml
                    Element singleSnippet=xmlSnippet.createElement("snippet");
                    singleSnippet.appendChild(idSnippetElement);
                    singleSnippet.appendChild(creatorElement);
                    singleSnippet.appendChild(titleElement);
                    singleSnippet.appendChild(codeElement);
                    singleSnippet.appendChild(languageElement);
                    singleSnippet.appendChild(date_creationElement);
                    singleSnippet.appendChild(modElement);
                    singleSnippet.appendChild(code_modElement);
                    singleSnippet.appendChild(user_modElement);
                    singleSnippet.appendChild(lastusermodElement);
                    singleSnippet.appendChild(date_lastmodpropElement);
                    singleSnippet.appendChild(date_lastmodElement);

                    for (int i = 0; i < snippet.getLength(); i++) {
                        if (snippet.item(i).getChildNodes().item(0).getTextContent().equals(""+mysnippet.getId())) {
                            //sostituzione nodo modificato dall'utente
                            xmlSnippet.getDocumentElement().replaceChild(singleSnippet,snippet.item(i));  
                            break;
                        }
                    }
                    
                    rootResponse.setTextContent("ok");
                    
                    break;
            }
            
            DataOutputStream dos=new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fileSnippet)));
            mngXML.transform(dos, xmlSnippet);
            dos.close();
            
            answer.appendChild(rootResponse);
            Util.mutexSnippetFile.release();
            
        } catch (Exception ex) {
            rootResponse.setTextContent("error");
            answer.appendChild(rootResponse);
            Logger.getLogger(ModifyServlet.class.getName()).log(Level.SEVERE, null, ex);
            Util.mutexSnippetFile.release();
        }
        return answer;
    }

}
