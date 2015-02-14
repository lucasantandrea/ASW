/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import asw1025_lib.HTTPClient;
import asw1025_lib.ManageXML;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Luca
 */
public class Applet extends JApplet {
    /*elementi grafici globali*/
    JPanel workingPanel,messagePanel; //?
    JPanel titlePanel, centerPanel, ownerPanel, editorPanel, ownerModificationPanel;
    JLabel titleLabel, langLabel;
    JTextArea ownerTextarea, editorTextarea;
    JLabel editorModification, ownerCode;
    JButton salva, copy,continua;
    JLabel message,done;
    int TextToSend=0;
    
    final String BASE = "http://localhost:8080/WebApplication/";
    HTTPClient hc = new HTTPClient();
    
    //TODO: CAMBIARE: PASSARE COME PARAMETRO (anche il sessionId!)
    String username="rete";
    String idSnippet="3";

    /*variabili per l salvataggio dei dati*/
    String creator="";
    String title="";
    String code="";
    String language="";
    String date_creation="";
    String mod="";
    String code_mod="";
    String user_mod="";
    String lastusermod="";
    String date_lastmodprop="";
    String date_lastmod="";
    

    //TODO:CREARE UNA FUNZIONE CHE CONTENGA IL CODICE COMUNE PER LE CHIAMATE (VEDERE Client3)
    public void init() {
        try {
            //TODOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
            username=getParameter("username");
            idSnippet=getParameter("idSnippet");
            hc.setBase(new URL(BASE));
            hc.setSessionId(getParameter("sessionid"));
            
            /*Loading starting layout*/
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    drawInitialLayout();
                }
            });
            
            //TODO: how to surround?
            loadSnippet(false);
            
            /*Behaviour of the copy button*/
            copy.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {   
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            ownerTextarea.append(editorModification.getText());
                        }
                    });
                }
            });

            /*Behaviour of continue button*/
            //TODO: how to surround?
            continua.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) { 
                    loadSnippet(true);
                }
            });
            
            salva.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) { 
                    //faccio il salvataggio della modifica sul file xml
                    ManageXML mx;
                    try {
                        mx = new ManageXML();
                        Document question = mx.newDocument();
                        
                        Element rootRequest = question.createElement("setRequest");
                        Element idSnippetElement = question.createElement("idSnippet");
                        idSnippetElement.setTextContent(idSnippet);
                        rootRequest.appendChild(idSnippetElement);
                        
                        Element creatorElement = question.createElement("creator");
                        creatorElement.setTextContent(creator);
                        rootRequest.appendChild(creatorElement);
                        
                        Element titleElement = question.createElement("title");
                        titleElement.setTextContent(title);
                        rootRequest.appendChild(titleElement);
                        
                        Element codeElement = question.createElement("code");
                        codeElement.setTextContent(code);
                        rootRequest.appendChild(codeElement);
                        
                        Element languageElement = question.createElement("language");
                        languageElement.setTextContent(language);
                        rootRequest.appendChild(languageElement);
                        
                        Element date_creationElement = question.createElement("date_creation");
                        date_creationElement.setTextContent(date_creation);
                        rootRequest.appendChild(date_creationElement);
                        
                        Element modElement = question.createElement("mod");
                        modElement.setTextContent(mod);
                        rootRequest.appendChild(modElement);

                        Element code_modElement = question.createElement("code_mod");
                        code_modElement.setTextContent(code_mod);
                        rootRequest.appendChild(code_modElement);
                        
                        Element user_modElement = question.createElement("user_mod");
                        user_modElement.setTextContent(user_mod);
                        rootRequest.appendChild(user_modElement);
                        
                        Element lastusermodElement = question.createElement("lastusermod");
                        lastusermodElement.setTextContent(lastusermod);
                        rootRequest.appendChild(lastusermodElement);                        

                        Element date_lastmodpropElement = question.createElement("date_lastmodprop");
                        date_lastmodpropElement.setTextContent(date_lastmodprop);
                        rootRequest.appendChild(date_lastmodpropElement);                        

                        Element date_lastmodElement = question.createElement("date_lastmod");
                        date_lastmodElement.setTextContent(date_lastmod);
                        rootRequest.appendChild(date_lastmodElement);                                                
                        
                        rootRequest.appendChild(idSnippetElement);
                        rootRequest.appendChild(creatorElement);
                        rootRequest.appendChild(titleElement);
                        rootRequest.appendChild(codeElement);
                        rootRequest.appendChild(languageElement);
                        rootRequest.appendChild(date_creationElement);
                        rootRequest.appendChild(modElement);
                        rootRequest.appendChild(code_modElement);
                        rootRequest.appendChild(user_modElement);
                        rootRequest.appendChild(lastusermodElement);
                        rootRequest.appendChild(date_lastmodpropElement);
                        rootRequest.appendChild(date_lastmodElement);
                        
                        Element content = question.createElement("content");
                        Element usernameElement = question.createElement("username");
                        
                        if(TextToSend==1){
                            content.setTextContent(ownerTextarea.getText());
                        }else if (TextToSend==2){
                            content.setTextContent(editorTextarea.getText());
                        }
                        usernameElement.setTextContent(username);
                        
                        rootRequest.appendChild(content);
                        rootRequest.appendChild(usernameElement);
                        
                        question.appendChild(rootRequest);
                        final Document answer = hc.execute("ModifyServlet",question);    
                        
                        
                        //gestione risposta
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                if(answer.getDocumentElement().getChildNodes().item(0)!=null){
                                    if(answer.getDocumentElement().getChildNodes().item(0).getTextContent().equals("ok")){
                                        message.setText("Salvataggio effettuato con successo");
                                        continua.setVisible(true);
                                    }else{
                                        message.setText("Si è verificato un errore. Salvataggio non effettuato");
                                    }
                                    workingPanel.setVisible(false);
                                    messagePanel.setVisible(true);
                                }
                            }
                        });
                        
                    } catch (Exception ex) {
                        System.out.println("Errore!"+ex.getMessage().toString());
                    }
                }
            });
        } 
        catch (Exception ex) {
            System.out.println(ex);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    message.setText("Si è verificato un errore");
                }
            });
        }			
    }
    
    public void destroy(ActionEvent e) { 
        //faccio il salvataggio della modifica sul file xml
        ManageXML mx;
        try {
            mx = new ManageXML();
            Document question = mx.newDocument();

            Element rootRequest = question.createElement("destroyRequest");
            Element idSnippetElement = question.createElement("idSnippet");
            idSnippetElement.setTextContent(idSnippet);
            rootRequest.appendChild(idSnippetElement);

            rootRequest.appendChild(idSnippetElement);

            question.appendChild(rootRequest);
            final Document answer = hc.execute("ModifyServlet",question);    

        } catch (Exception ex) {
            System.out.println("Errore!"+ex.getMessage().toString());
        }
    }
	
    private void loadSnippet(final boolean SecondCall){
        try {                                                
            ManageXML mx;
            try {
                mx = new ManageXML();
                Document question = mx.newDocument();
                Element rootRequest = question.createElement("getRequest");

                Element id = question.createElement("id");
                id.setTextContent(idSnippet);
                rootRequest.appendChild(id);

                Element user = question.createElement("user");
                user.setTextContent(username);
                rootRequest.appendChild(user);

                question.appendChild(rootRequest);
                final Document answer = hc.execute("ModifyServlet",question);

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        //controllo che la risposta non sia nulla
                        if(answer.getDocumentElement().getChildNodes().item(0)!=null){
                            if(answer.getDocumentElement().getChildNodes().item(0).getTextContent().equals("error")){
                                workingPanel.setVisible(false);
                                message.setText("Si è verificato un errore");
                                messagePanel.setVisible(true);    
                            }
                            else if(answer.getDocumentElement().getChildNodes().item(0).getTextContent().equals("Y")){

                                /*parsing della risposta ricevuta*/
                                creator=answer.getDocumentElement().getChildNodes().item(1).getTextContent();
                                title=answer.getDocumentElement().getChildNodes().item(2).getTextContent();
                                code=answer.getDocumentElement().getChildNodes().item(3).getTextContent();
                                language=answer.getDocumentElement().getChildNodes().item(4).getTextContent();
                                date_creation=answer.getDocumentElement().getChildNodes().item(5).getTextContent();
                                mod=answer.getDocumentElement().getChildNodes().item(6).getTextContent();
                                code_mod=answer.getDocumentElement().getChildNodes().item(7).getTextContent();
                                user_mod=answer.getDocumentElement().getChildNodes().item(8).getTextContent();
                                lastusermod=answer.getDocumentElement().getChildNodes().item(9).getTextContent();
                                date_lastmodprop=answer.getDocumentElement().getChildNodes().item(10).getTextContent();
                                date_lastmod=answer.getDocumentElement().getChildNodes().item(11).getTextContent();

                                titleLabel.setText(title);
                                ownerTextarea.setText(code);
                                langLabel.setText(language);

                                //modifico la visualizzazione in base all'utente
                                if(creator.equals(username)){
                                    //se sono autore e il testo è modificato, mostro l'ultima modifica
                                    if(mod.equals("Y")){    
                                        editorModification.setText(code_mod);
                                    }
                                    else{
                                        ownerModificationPanel.setVisible(false);
                                    }
                                    centerPanel.add(BorderLayout.NORTH,ownerPanel);
                                    TextToSend=1;
                                }
                                else{
                                    //caso in cui non sono l'autore
                                    ownerTextarea.setVisible(false);
                                    ownerCode.setText(code);
                                    centerPanel.add(BorderLayout.NORTH,new JScrollPane(ownerCode));
                                    centerPanel.add(BorderLayout.CENTER,new JScrollPane(editorTextarea));
                                    ownerModificationPanel.setVisible(false);

                                    String editorTitle="Proposta di modifica";

                                    //se il testo è modificato lo visualizzo già all'interno della textarea
                                    if(mod.equals("Y")){    
                                        editorTextarea.setText(code_mod);
                                        editorTitle+=" ("+user_mod+")";
                                    }
                                    TitledBorder editorBorder = BorderFactory.createTitledBorder(editorTitle);
                                    editorTextarea.setBorder(editorBorder);

                                    TextToSend=2;
                                }

                                ownerModificationPanel.add(copy);
                                centerPanel.add(BorderLayout.SOUTH,ownerModificationPanel);
                            }
                            else{
                                workingPanel.setVisible(false);
                                message.setText("Lo snippet è attualmente in modifica da parte di un'altro utente");
                                messagePanel.setVisible(true);                            
                            }
                        }
                        else{
                            workingPanel.setVisible(false);
                            messagePanel.setVisible(true);
                        }

                        if(SecondCall){
                            workingPanel.setVisible(true);
                            messagePanel.setVisible(false);                            
                        }
                    }
                });
                mx.transform(System.out,answer);
            } catch (Exception ex) {
                Logger.getLogger(Applet.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        catch (Exception ex){
            System.out.println(ex);
            SwingUtilities.invokeLater(new Runnable(){
                @Override
                public void run() {
                    workingPanel.setVisible(false);
                    message.setText("Si è verificato un errore");
                    messagePanel.setVisible(true);
                }
            });
        }
    }
    
    private void drawInitialLayout(){
        //creazione elementi    
        workingPanel=new JPanel(new BorderLayout());
        messagePanel=new JPanel();
        titlePanel=new JPanel(new GridLayout(1,2));
        centerPanel=new JPanel(new BorderLayout());
        ownerPanel=new JPanel(new BorderLayout());
        ownerModificationPanel=new JPanel(new GridLayout());
        editorPanel=new JPanel(new GridLayout());
        titleLabel=new JLabel();
        langLabel=new JLabel();
        ownerTextarea=new JTextArea(10, 15);
        editorTextarea=new JTextArea(10, 15);
        editorModification=new JLabel();
        ownerCode=new JLabel();
        message=new JLabel("Contenuto non disponibile");
        done=new JLabel("Salvataggio effettuato con successo");

        Container container=getContentPane();
        container.setLayout(new BorderLayout());

        titlePanel.add(titleLabel);
        titlePanel.add(langLabel);

        ownerPanel.add(BorderLayout.NORTH,new JScrollPane(ownerTextarea));
        ownerModificationPanel.add(editorModification);

        salva=new JButton("Salva");
        copy=new JButton("Copia");
        continua=new JButton("Modifica di nuovo");

        TitledBorder titleBorder = BorderFactory.createTitledBorder("Titolo");
        titleLabel.setBorder(titleBorder);
        TitledBorder languagedBorder = BorderFactory.createTitledBorder("Linguaggio");
        langLabel.setBorder(languagedBorder);
        TitledBorder modificationBorder = BorderFactory.createTitledBorder("Proposta di modifica");
        editorModification.setBorder(modificationBorder);
        TitledBorder ownerBorder = BorderFactory.createTitledBorder("Codice originale");
        ownerCode.setBorder(ownerBorder);
        ownerTextarea.setBorder(ownerBorder);
        
        workingPanel.add(BorderLayout.NORTH,titlePanel);
        workingPanel.add(BorderLayout.CENTER,centerPanel);
        workingPanel.add(BorderLayout.SOUTH,salva);
        
        messagePanel.add(message);
        messagePanel.add(continua);
        continua.setVisible(false);
        messagePanel.setVisible(false);
        
        container.add(BorderLayout.NORTH,workingPanel);
        container.add(BorderLayout.CENTER,messagePanel);    
    }
}
