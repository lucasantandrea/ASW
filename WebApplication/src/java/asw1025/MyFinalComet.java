/*    
    Esame ASW 2014-2015
    Autori: Luca Santandrea, Matteo Mariani, Antonio Leo Folliero, Francesco Degli Angeli
    Matricola: 0900050785
    Gruppo: 1025
*/
package asw1025;

import static asw1025.ModifyServlet.hc;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.w3c.dom.*;

@WebServlet(name = "MyFinalComet", urlPatterns = {"/MyFinalComet"},asyncSupported=true)
public class MyFinalComet extends HttpServlet {

    private HashMap<String, Object> hcontexts = new HashMap<String, Object>();
    private LinkedList<AsyncContext> contexts = new LinkedList<AsyncContext>();

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        InputStream is = request.getInputStream();
        response.setContentType("text/xml;charset=UTF-8");

        try {
            ManageXML mngXML = new ManageXML();
            Document data = mngXML.parse(is);
            is.close();

            operations(data, request, response, mngXML);
            
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    private void operations(Document data, HttpServletRequest request, HttpServletResponse response, ManageXML mngXML) throws Exception {

        HttpSession session = request.getSession();
        //name of operation is message root
        Element root = data.getDocumentElement();
        String operation = root.getTagName();
        String userToUnlock=root.getTextContent();
        LinkedList<AsyncContext> list;
        switch (operation) {
            case "push":
                System.out.println("push received");
                synchronized (this) {
                    list = (LinkedList<AsyncContext>) hcontexts.get(userToUnlock); // TODO: il nodo!            
                    /*for (AsyncContext asyncContext : contexts) {
                        OutputStream aos = asyncContext.getResponse().getOutputStream();
                        mngXML.transform(aos, data);
                        //QUI CAMBIO LA RISPOSTA!!!
                        //Document data2 = mngXML.newDocument();        
                        //Element rootResponse= data2.createElement("something");
                        //Element singleSnippet=data2.createElement("snippet");
                        //rootResponse.appendChild(singleSnippet);
                        //data2.appendChild(rootResponse);
                        aos.close();
                        asyncContext.complete();
                    }
                    contexts.clear();*/
                    
                    for (AsyncContext asyncContext : list) {        
                        if(asyncContext.getResponse()!=null){
                            OutputStream aos = asyncContext.getResponse().getOutputStream();
                            Document topass=myfunction(mngXML,userToUnlock);
                            mngXML.transform(aos,topass);
                            aos.close();
                            asyncContext.complete();
                        }
                    }
                    hcontexts.remove(userToUnlock);
                    //list.clear();
                    //hcontexts.remove("rete");
                }
                Document answer = mngXML.newDocument("ok");
                OutputStream os = response.getOutputStream();
                mngXML.transform(os, answer);
                os.close();
                break;
            case "pop":
                //System.out.println("pop received");
                
                
                String user = (String) session.getAttribute("user");
                System.out.println("pop received from: " + user);
                synchronized (this) {
                    if(hcontexts==null || hcontexts.size()==0){
                        list = new LinkedList<AsyncContext>();
                    }
                    else{
                        list = (LinkedList<AsyncContext>) hcontexts.get(user);
                        if (list==null){
                            list = new LinkedList<AsyncContext>();    
                        }
                    } 
                }
                
                
                AsyncContext asyncContext = request.startAsync();
                asyncContext.setTimeout(10 * 1000);
                asyncContext.addListener(new AsyncAdapter() {
                    @Override
                    public void onTimeout(AsyncEvent e) {
                        try {
                            ManageXML mngXML = new ManageXML();
                            
                            System.out.println("timeout event launched");
                            
                            Document answer = mngXML.newDocument("timeout");
                            AsyncContext asyncContext = e.getAsyncContext();
                            boolean confirm=false;
                            
                            HttpServletRequest reqAsync = (HttpServletRequest) asyncContext.getRequest();
                            String user = (String) reqAsync.getSession().getAttribute("user");
                            synchronized (MyFinalComet.this) {
                                
                                LinkedList<AsyncContext>  mylist = (LinkedList<AsyncContext>) hcontexts.get(user);
                                
                                /*if ((confirm = contexts.contains(asyncContext))) {
                                    contexts.remove(asyncContext);
                                }*/
                                
                                //controllo che la lista non sia nulla (se ho rimosso da hcontexts in seguito a push)
                                System.out.println(hcontexts.size());
                                if (mylist != null) {                                
                                    if ((confirm = mylist.contains(asyncContext))) {
                                        mylist.remove(asyncContext);
                                    }
                                }
                            }
                            if (confirm) {
                                OutputStream tos = asyncContext.getResponse().getOutputStream();
                                mngXML.transform(tos, answer);
                                tos.close();
                                asyncContext.complete();
                            }
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
                    }
                });
                synchronized (this) {
                    contexts.add(asyncContext);
                    list.add(asyncContext);
                    hcontexts.put(user,list);
                }
                break;
        }
    }

    private Document myfunction(ManageXML mngXML, String userRequester){        
        Document answer= null;
        answer = mngXML.newDocument();
        try {     
            answer = mngXML.newDocument();
            Element rootResponse= answer.createElement("push");
            //HttpSession session = request.getSession();
            
            try {
                // Lettura esclusiva
                //Util.mutexSnippetFile.acquire();
                
                //lettura da file xml
                String fileSnippet = Util.getCorrectFilePath(this, "snippet.xml");
                Document xmlSnippet = null;
                
                DataInputStream dis = null;
                dis = new DataInputStream(new BufferedInputStream(new FileInputStream(fileSnippet)));
                xmlSnippet = mngXML.parse(dis);
                dis.close();
                
                NodeList snippet = xmlSnippet.getDocumentElement().getChildNodes();
                
                //TODO: qui devo passargli l'utente corretto!! ovvero quello proprietario del file, che va a sbloccare i suoi context bloccati nella comet!
                //String userRequester = "rete";
                //session.getAttribute("user").toString();
                
                // Ricerca negli Snippet del proprietario che sono modificati al momento
                //TODO: DEVO CREARE L'XML IN MODO TALE CHE SIA UNA LISTA DI MODIFICATI!
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
                    
                    if (mysnippet.getCreator().equals(userRequester)) {
                        //se non disponibile alla modifica
                        if(!mysnippet.getUserMod().equals("") && !mysnippet.getUserMod().equals(userRequester)){
                            //aggiungo a XML per la risposta
                            Element singleSnippet=answer.createElement("snippetMod");
                            Element titleElement = answer.createElement("title");
                            Element userModElement = answer.createElement("userMod");
                            
                            titleElement.setTextContent(mysnippet.getTitle());
                            userModElement.setTextContent(mysnippet.getUserMod());
                            
                            singleSnippet.appendChild(titleElement);
                            singleSnippet.appendChild(userModElement);
                            
                            rootResponse.appendChild(singleSnippet);
                            
                            
                            //break;
                            //answer.appendChild(rootResponse);
                        }
                    }
                }
                
                DataOutputStream dos=new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fileSnippet)));
                mngXML.transform(dos, xmlSnippet);
                dos.close();
                
                answer.appendChild(rootResponse);
                
                
                //Util.mutexSnippetFile.release();
                
            } catch (Exception ex) {
                rootResponse.setTextContent("error");
                answer.appendChild(rootResponse);
                Logger.getLogger(ModifyServlet.class.getName()).log(Level.SEVERE, null, ex);
                //Util.mutexSnippetFile.release();
            }
            return answer;
        } catch (Exception ex) {
            Logger.getLogger(ModifyServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return answer;
    }
}
