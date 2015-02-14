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
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@WebServlet(urlPatterns = {"/ModifyServlet"})
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
        
        try {
            //name of operation is message root
            Element rootRequest = data.getDocumentElement();
            String operationRequest = rootRequest.getTagName();
            switch (operationRequest) {
                case "getRequest":
                    
                    //prova salvataggio variabile di sessione
                    Object luca=session.getAttribute("counter");
                    if (luca== null || luca.toString().equals("")){
                        session.setAttribute("counter", new Integer(0));    
                    }
                    
                    
                    //lettura da file xml
                    String fileSnippet = Util.getCorrectFilePath(this, "snippet.xml");
                    Document xmlSnippet = null;
                    
                    String idSnippet=rootRequest.getChildNodes().item(0).getTextContent();
                    String userRequester = rootRequest.getChildNodes().item(1).getTextContent();
                    
                    // Lettura esclusiva
                    Util.mutexSnippetFile.acquire();
                    
                    // Caricamento database xml Snippet
                    DataInputStream dis = null;
                    dis = new DataInputStream(new BufferedInputStream(new FileInputStream(fileSnippet)));
                    xmlSnippet = mngXML.parse(dis);
                    dis.close();
                    
                    NodeList snippet = xmlSnippet.getDocumentElement().getChildNodes();
                    Element rootResponse= answer.createElement("snippet");
                    
                    // RICERCA negli Snippet e del nodo snippet da visualizzare
                    for (int i = 0; i < snippet.getLength(); i++) {
                        String id = snippet.item(i).getChildNodes().item(0).getTextContent();
                        String usermod = snippet.item(i).getChildNodes().item(8).getTextContent();
                        
                        //((thisUser.equals(username)) && 
                        if (id.equals(idSnippet)) {
                            
                            //TODO: CONTROLLO CHE LA RISORSA NON SIA BLOCCATA! CHECK SU PARAMETRO id modificante
                            if(!usermod.equals("")&& !usermod.equals(userRequester)){
                                Element availableElement = answer.createElement("available");
                                availableElement.setTextContent("N");
                                rootResponse.appendChild(availableElement);
                            }
                            else{
                                //aggiungo a XML per la risposta
                                Element availableElement = answer.createElement("available");
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
                                creatorElement.setTextContent(snippet.item(i).getChildNodes().item(1).getTextContent());
                                titleElement.setTextContent(snippet.item(i).getChildNodes().item(2).getTextContent());
                                codeElement.setTextContent(snippet.item(i).getChildNodes().item(3).getTextContent());
                                languageElement.setTextContent(snippet.item(i).getChildNodes().item(4).getTextContent());
                                date_creationElement.setTextContent(snippet.item(i).getChildNodes().item(5).getTextContent());
                                modElement.setTextContent(snippet.item(i).getChildNodes().item(6).getTextContent());
                                code_modElement.setTextContent(snippet.item(i).getChildNodes().item(7).getTextContent());
                                user_modElement.setTextContent(snippet.item(i).getChildNodes().item(8).getTextContent());
                                lastusermodElement.setTextContent(snippet.item(i).getChildNodes().item(9).getTextContent());
                                date_lastmodpropElement.setTextContent(snippet.item(i).getChildNodes().item(10).getTextContent());
                                date_lastmodElement.setTextContent(snippet.item(i).getChildNodes().item(11).getTextContent());

                                rootResponse.appendChild(availableElement);
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
                                
                                
                                
                                
                                //impost l'utente corrente come modificatore del record
                                xmlSnippet.getChildNodes().item(0).getChildNodes().item(i).getChildNodes().item(8).setTextContent(userRequester);
                                DataOutputStream dos = null;
                                dos=new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fileSnippet)));
                                mngXML.transform(dos, xmlSnippet);
                                dos.close();
                                
                                break;
                            }
                        }
                    }
                    answer.appendChild(rootResponse);
                    Util.mutexSnippetFile.release();
                    break;
                case "setRequest":
                    //lettura da file xml
                    String fileSnippet2 = Util.getCorrectFilePath(this, "snippet.xml");
                    Document xmlSnippet2 = null;
                    DataOutputStream dos = null;
                    
                    // Lettura esclusiva
                    Util.mutexSnippetFile.acquire();
                    
                    // Caricamento xml Snippet
                    DataInputStream dis2 = null;
                    dis2 = new DataInputStream(new BufferedInputStream(new FileInputStream(fileSnippet2)));
                    xmlSnippet2 = mngXML.parse(dis2);
                    dis2.close();
                    
                    NodeList snippet2 = xmlSnippet2.getDocumentElement().getChildNodes();
                    
                    //parametri in arrivo dalla request
                    String idSnippet2=data.getDocumentElement().getChildNodes().item(0).getTextContent();

                    String creator=data.getDocumentElement().getChildNodes().item(1).getTextContent();
                    String requestAuthor=data.getDocumentElement().getChildNodes().item(13).getTextContent();
                    
                    Element idSnippetElement = xmlSnippet2.createElement("idSnippet");
                    idSnippetElement.setTextContent(idSnippet2);
                    Element creatorElement = xmlSnippet2.createElement("creator");
                    creatorElement.setTextContent(creator);
                    Element titleElement = xmlSnippet2.createElement("title");
                    titleElement.setTextContent(data.getDocumentElement().getChildNodes().item(2).getTextContent());
                    Element codeElement = xmlSnippet2.createElement("code");
                    Element languageElement = xmlSnippet2.createElement("language");
                    languageElement.setTextContent(data.getDocumentElement().getChildNodes().item(4).getTextContent());
                    Element date_creationElement = xmlSnippet2.createElement("date_creation");
                    date_creationElement.setTextContent(data.getDocumentElement().getChildNodes().item(5).getTextContent());
                    Element modElement = xmlSnippet2.createElement("mod");
                    modElement.setTextContent(data.getDocumentElement().getChildNodes().item(6).getTextContent());
                    Element code_modElement = xmlSnippet2.createElement("code_mod");
                    
                    //controllo se è una proposta di modifica o una modifica dell'autore
                    if(creator.equals(requestAuthor)){
                        codeElement.setTextContent(data.getDocumentElement().getChildNodes().item(12).getTextContent());
                        code_modElement.setTextContent(data.getDocumentElement().getChildNodes().item(7).getTextContent());    
                    }else{
                        codeElement.setTextContent(data.getDocumentElement().getChildNodes().item(3).getTextContent());
                        code_modElement.setTextContent(data.getDocumentElement().getChildNodes().item(12).getTextContent());
                    }
                    
                    Element user_modElement = xmlSnippet2.createElement("user_mod");
                    //imposto a vuoto per liberare il "lock" del record
                    user_modElement.setTextContent("");
                    Element lastusermodElement = xmlSnippet2.createElement("lastusermod");
                    lastusermodElement.setTextContent(data.getDocumentElement().getChildNodes().item(9).getTextContent());
                    Element date_lastmodpropElement = xmlSnippet2.createElement("date_lastmodprop");
                    date_lastmodpropElement.setTextContent(data.getDocumentElement().getChildNodes().item(10).getTextContent());
                    Element date_lastmodElement = xmlSnippet2.createElement("date_lastmod");
                    date_lastmodElement.setTextContent(data.getDocumentElement().getChildNodes().item(11).getTextContent());
                    
                    /*SCRITTURA XML FILE*/
                    Element singleSnippet=xmlSnippet2.createElement("snippet");
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

                    //sovrascrivo record
                    for (int i = 0; i < snippet2.getLength(); i++) {
                        if (snippet2.item(i).getChildNodes().item(0).getTextContent().equals(""+idSnippet2)) {
                            //sostituzione nodo modificato dall'utente
                            xmlSnippet2.getDocumentElement().replaceChild(singleSnippet,snippet2.item(i));  
                            break;
                        }
                    }
                    
                    dos=new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fileSnippet2)));
                    mngXML.transform(dos, xmlSnippet2);
                    dos.close();
                    
                    Element rootResponse2= answer.createElement("snippet");
                    rootResponse2.setTextContent("ok");
                    answer.appendChild(rootResponse2);
                    
                    Util.mutexSnippetFile.release();
                    break;
            }
        } catch (Exception ex) {
            Element rootResponse2= answer.createElement("snippet");
            rootResponse2.setTextContent("error");
            answer.appendChild(rootResponse2);
            Logger.getLogger(ModifyServlet.class.getName()).log(Level.SEVERE, null, ex);
            Util.mutexSnippetFile.release();
        }
        return answer;
    }


}
