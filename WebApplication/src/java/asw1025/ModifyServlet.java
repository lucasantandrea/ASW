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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;
import static javax.servlet.SessionTrackingMode.URL;
import javax.servlet.annotation.WebServlet;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

@WebServlet(name = "ModifyServlet", urlPatterns = {"/ModifyServlet"},asyncSupported=true)
public class ModifyServlet extends HttpServlet {
    @Override
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
        boolean sendNotification=false;
        String userToNotify="";
        
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
                            if(!mysnippet.getUserMod().equals("") && !mysnippet.getUserMod().equals(userRequester)){
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
                                Element dateCreationElement = answer.createElement("dateCreation");
                                Element modElement = answer.createElement("mod");
                                Element codeModElement = answer.createElement("codeMod");
                                Element userModElement = answer.createElement("userMod");
                                Element lastUserModElement = answer.createElement("lastUserMod");
                                Element dateLastModPropElement = answer.createElement("dateLastModProp");
                                Element dateLastModElement = answer.createElement("dateLastMod");

                                availableElement.setTextContent("Y");                                
                                idSnippetElement.setTextContent(mysnippet.getId());
                                creatorElement.setTextContent(mysnippet.getCreator());
                                titleElement.setTextContent(mysnippet.getTitle());
                                codeElement.setTextContent(mysnippet.getCode());
                                languageElement.setTextContent(mysnippet.getLanguage());
                                dateCreationElement.setTextContent(mysnippet.getDateCreation());
                                modElement.setTextContent(mysnippet.getMod());
                                codeModElement.setTextContent(mysnippet.getCodeMod());
                                userModElement.setTextContent(mysnippet.getUserMod());
                                lastUserModElement.setTextContent(mysnippet.getLastUserMod());
                                dateLastModPropElement.setTextContent(mysnippet.getDateLastModProp());
                                dateLastModElement.setTextContent(mysnippet.getDateLastMod());

                                rootResponse.appendChild(availableElement);
                                rootResponse.appendChild(idSnippetElement);
                                rootResponse.appendChild(creatorElement);
                                rootResponse.appendChild(titleElement);
                                rootResponse.appendChild(codeElement);
                                rootResponse.appendChild(languageElement);
                                rootResponse.appendChild(dateCreationElement);
                                rootResponse.appendChild(modElement);
                                rootResponse.appendChild(codeModElement);
                                rootResponse.appendChild(userModElement);
                                rootResponse.appendChild(lastUserModElement);
                                rootResponse.appendChild(dateLastModPropElement);
                                rootResponse.appendChild(dateLastModElement);
                                
                                //imposta l'utente corrente come modificatore del record                                
                                xmlSnippet.getChildNodes().item(0).getChildNodes().item(i).getChildNodes().item(8).setTextContent(userRequester);
                                
                                sendNotification=true;
                                userToNotify=mysnippet.getCreator();
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
                    Element dateCreationElement = xmlSnippet.createElement("dateCreation");
                    dateCreationElement.setTextContent(mysnippet.getDateCreation());
                    Element modElement = xmlSnippet.createElement("mod");
                    Element codeModElement = xmlSnippet.createElement("codeMod");
                    Element lastUserModElement = xmlSnippet.createElement("lastUserMod");
                    Element dateLastModPropElement = xmlSnippet.createElement("dateLastModProp");
                    
                    //controllo se è una proposta di modifica o una modifica dell'autore
                    if(mysnippet.getCreator().equals(requestAuthor)){
                        codeElement.setTextContent(content);
                        codeModElement.setTextContent(mysnippet.getCodeMod());    
                        modElement.setTextContent(mysnippet.getMod());
                        lastUserModElement.setTextContent(mysnippet.getLastUserMod());
                        dateLastModPropElement.setTextContent(Util.convertDateToString(new Date()));
                    }else{
                        codeElement.setTextContent(mysnippet.getCode());
                        codeModElement.setTextContent(content);
                        modElement.setTextContent("Y");
                        lastUserModElement.setTextContent(requestAuthor);
                        dateLastModPropElement.setTextContent(mysnippet.getDateLastModProp());
                    }
                    
                    Element userModElement = xmlSnippet.createElement("userMod");
                    //imposto a vuoto per liberare il "lock" del record
                    userModElement.setTextContent("");
                    Element dateLastModElement = xmlSnippet.createElement("dateLastMod");
                    dateLastModElement.setTextContent(Util.convertDateToString(new Date()));
                    
                    //Sovrascrivo record nel database xml
                    Element singleSnippet=xmlSnippet.createElement("snippet");
                    singleSnippet.appendChild(idSnippetElement);
                    singleSnippet.appendChild(creatorElement);
                    singleSnippet.appendChild(titleElement);
                    singleSnippet.appendChild(codeElement);
                    singleSnippet.appendChild(languageElement);
                    singleSnippet.appendChild(dateCreationElement);
                    singleSnippet.appendChild(modElement);
                    singleSnippet.appendChild(codeModElement);
                    singleSnippet.appendChild(userModElement);
                    singleSnippet.appendChild(lastUserModElement);
                    singleSnippet.appendChild(dateLastModPropElement);
                    singleSnippet.appendChild(dateLastModElement);

                    for (int i = 0; i < snippet.getLength(); i++) {
                        if (snippet.item(i).getChildNodes().item(0).getTextContent().equals(""+mysnippet.getId())) {
                            //sostituzione nodo modificato dall'utente
                            xmlSnippet.getDocumentElement().replaceChild(singleSnippet,snippet.item(i));  
                            break;
                        }
                    }
                    
                    rootResponse.setTextContent("ok");
                    
                    sendNotification=true;
                    userToNotify=mysnippet.getCreator();
                    
                    break;
            }
            
            DataOutputStream dos=new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fileSnippet)));
            mngXML.transform(dos, xmlSnippet);
            dos.close();
            
            answer.appendChild(rootResponse);
            Util.mutexSnippetFile.release();
            
            if(sendNotification==true){
                //chiamo la comet per notificare al proprietario il fatto che essa è modificata
                hc.setBase(new URL(Util.BASE)); 
                Document cometdata = mngXML.newDocument("push");
                //todo: al posto di "rete" dovro passargli l'utente proprietario dello snippet attualmente aperto
                //cometdata.getDocumentElement().appendChild(data.createTextNode(mysnippet.getCreator())); 
                cometdata.getDocumentElement().setTextContent(userToNotify); 
                hc.execute("MyFinalComet",cometdata);
            }
            
        } catch (Exception ex) {
            rootResponse.setTextContent("error");
            answer.appendChild(rootResponse);
            Logger.getLogger(ModifyServlet.class.getName()).log(Level.SEVERE, null, ex);
            Util.mutexSnippetFile.release();
        }
        return answer;
    }

    /*prova invocazione get*/
    static HTTPClient hc = new HTTPClient();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {     

        
        try {     
            ManageXML mngXML = new ManageXML();
//            Document answer = mngXML.newDocument();
//            Element rootResponse= answer.createElement("push");
//            HttpSession session = request.getSession();
            
//            try {
//            mngXML = new ManageXML();
//            hc.setBase(new URL(Util.BASE));   
//            Document data = mngXML.newDocument("push");
//            data.getDocumentElement().appendChild(data.createTextNode("rete")); 
//            Document answer = hc.execute("myfinalcomet",data);
//            
//            } catch (Exception ex) {
//            Logger.getLogger(ModifyServlet.class.getName()).log(Level.SEVERE, null, ex);
//            
//            }
            
            try {
                // Lettura esclusiva
                //Util.mutexSnippetFile.acquire();
                
                //lettura da file xml
//                String fileSnippet = Util.getCorrectFilePath(this, "snippet.xml");
//                Document xmlSnippet = null;
//                
//                DataInputStream dis = null;
//                dis = new DataInputStream(new BufferedInputStream(new FileInputStream(fileSnippet)));
//                xmlSnippet = mngXML.parse(dis);
//                dis.close();
//                
//                NodeList snippet = xmlSnippet.getDocumentElement().getChildNodes();
//                
//                //TODO: qui devo passargli l'utente corretto!! ovvero quello proprietario del file, che va a sbloccare i suoi context bloccati nella comet!
//                String userRequester = "rete";
//                //session.getAttribute("user").toString();
//                
//                // Ricerca negli Snippet del proprietario che sono modificati al momento
//                //TODO: DEVO CREARE L'XML IN MODO TALE CHE SIA UNA LISTA DI MODIFICATI!
//                for (int i = 0; i < snippet.getLength(); i++) {
//                    SnippetData mysnippet = new SnippetData(snippet.item(i).getChildNodes().item(0).getTextContent(),
//                            snippet.item(i).getChildNodes().item(1).getTextContent(),
//                            snippet.item(i).getChildNodes().item(2).getTextContent(),
//                            snippet.item(i).getChildNodes().item(3).getTextContent(),
//                            snippet.item(i).getChildNodes().item(4).getTextContent(),
//                            snippet.item(i).getChildNodes().item(5).getTextContent(),
//                            snippet.item(i).getChildNodes().item(6).getTextContent(),
//                            snippet.item(i).getChildNodes().item(7).getTextContent(),
//                            snippet.item(i).getChildNodes().item(8).getTextContent(),
//                            snippet.item(i).getChildNodes().item(9).getTextContent(),
//                            snippet.item(i).getChildNodes().item(10).getTextContent(),
//                            snippet.item(i).getChildNodes().item(11).getTextContent());
//                    
//                    if (mysnippet.getCreator().equals(userRequester)) {
//                        //se non disponibile alla modifica
//                        if(!mysnippet.getUserMod().equals("") && !mysnippet.getUserMod().equals(userRequester)){
//                            //aggiungo a XML per la risposta
//                            Element singleSnippet=answer.createElement("snippetMod");
//                            Element titleElement = answer.createElement("title");
//                            Element userModElement = answer.createElement("userMod");
//                            
//                            titleElement.setTextContent(mysnippet.getTitle());
//                            userModElement.setTextContent(mysnippet.getUserMod());
//                            
//                            singleSnippet.appendChild(titleElement);
//                            singleSnippet.appendChild(userModElement);
//                            
//                            rootResponse.appendChild(singleSnippet);
//                            
//                            
//                            //break;
//                            //answer.appendChild(rootResponse);
//                        }
//                    }
//                }
//                
//                DataOutputStream dos=new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fileSnippet)));
//                mngXML.transform(dos, xmlSnippet);
//                dos.close();
//                
//                answer.appendChild(rootResponse);
                
                hc.setBase(new URL(Util.BASE));   
                //hc.execute("MyFinalComet",answer);
                Document data = mngXML.newDocument("push");
                //TODO: QUI GLI PASSO L'UTENTE SULLA BASE DELLO SNIPPET CHE STO APRENDO!!
                data.getDocumentElement().appendChild(data.createTextNode("rete"));                            
                hc.execute("MyFinalComet",data);
                
                
                //Util.mutexSnippetFile.release();
                
            } catch (Exception ex) {
                //rootResponse.setTextContent("error");
                //answer.appendChild(rootResponse);
                Logger.getLogger(ModifyServlet.class.getName()).log(Level.SEVERE, null, ex);
                //Util.mutexSnippetFile.release();
            }
        } catch (Exception ex) {
            Logger.getLogger(ModifyServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}