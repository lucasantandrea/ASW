package asw1025;

/*    
    Esame ASW 2014-2015
    Autori: Luca Santandrea, Matteo Mariani, Antonio Leo Folliero, Francesco Degli Angeli
    Matricola: 0900050785
    Gruppo: 1025
*/


import asw1025.HTTPClient;
import asw1025.ManageXML;
import asw1025.SnippetData;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
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

public class Applet extends JApplet {
    /*elementi grafici globali*/
    JPanel workingPanel,messagePanel;
    JPanel titlePanel, centerPanel,ownerPanel,ownerModificationPanel;
    static JTextArea ownerTextarea, editorTextarea;
    JTextArea editorModification,ownerCode;
    JButton salva,copy,continua;
    JLabel titleLabel,langLabel,message,done;
    static int textToSend=0;
    static SnippetData mySnippet;
    
    //final String BASE = "http://isi-tomcat.csr.unibo.it:8080/~luca.santandrea6/";    
    final String BASE = "http://localhost:8080/WebApplication/";    
    
    HTTPClient hc;
    ManageXML mx;
    
    static String username="";
    static String idSnippet="";


    /**
     * Inizializzazione dell'applet
     */
    public void init() {
        try {
            username=getParameter("username");
            idSnippet=getParameter("idSnippet");
            mx=new ManageXML();
            hc=new HTTPClient();
            hc.setBase(new URL(BASE));
            
            /*Caricamento del layout iniziale*/
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    drawInitialLayout();
                }
            });
            
            loadSnippet(false);
            
            /*Comportamento del bottone di copia*/
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

            /*Comportamento del bottone continua*/
            continua.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) { 
                    loadSnippet(true);
                }
            });
            
            /*Comportamento del bottone salva*/
            salva.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) { 
                    //faccio il salvataggio della modifica sul file xml
                    try {
                        final Document answer = callService(hc, mx, "setRequest");
                        
                        //gestione risposta
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                if(answer.getDocumentElement().getChildNodes().item(0)!=null){
                                    if(answer.getDocumentElement().getChildNodes().item(0).getTextContent().equals("ok")){
                                        message.setText("Salvataggio effettuato con successo");
                                        continua.setText("Modifica di nuovo");
                                        continua.setVisible(true);
                                    }else{
                                        message.setText("Si è verificato un errore. Salvataggio non effettuato");
                                    }
                                    workingPanel.setVisible(false);
                                    messagePanel.setVisible(true);
                                }
                            }
                        });
                        mx.transform(System.out,answer);
                        
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

    /**
     * funzione utilizzata per il load dello snippet (chiama il servizio web)
     * @param secondCall booleano da impostare a true se si tratta di una seconda chiamata (non deve aggiungere nuovamente elementi grafici)
     */
    private void loadSnippet(final boolean secondCall){
        try {                                                               
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        centerPanel.removeAll();
                    }
                });
                
                final Document answer = callService(hc, mx, "getRequest");
                //controllo che la risposta non sia nulla
                if(answer.getDocumentElement().getChildNodes().item(0)!=null){                         
                    if(answer.getDocumentElement().getChildNodes().item(0).getTextContent().equals("error")){
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                workingPanel.setVisible(false);
                                message.setText("Si è verificato un errore");
                                messagePanel.setVisible(true);    
                            }
                        });
                    }
                    else if(answer.getDocumentElement().getChildNodes().item(0).getTextContent().equals("Y")){
                        /*parsing della risposta ricevuta*/
                        mySnippet =new SnippetData(answer.getDocumentElement().getChildNodes().item(1).getTextContent(), 
                        answer.getDocumentElement().getChildNodes().item(2).getTextContent(), 
                        answer.getDocumentElement().getChildNodes().item(3).getTextContent(), 
                        answer.getDocumentElement().getChildNodes().item(4).getTextContent(), 
                        answer.getDocumentElement().getChildNodes().item(5).getTextContent(),
                        answer.getDocumentElement().getChildNodes().item(6).getTextContent(),
                        answer.getDocumentElement().getChildNodes().item(7).getTextContent(),
                        answer.getDocumentElement().getChildNodes().item(8).getTextContent(),
                        answer.getDocumentElement().getChildNodes().item(9).getTextContent(),
                        answer.getDocumentElement().getChildNodes().item(10).getTextContent(),
                        answer.getDocumentElement().getChildNodes().item(11).getTextContent(),
                        answer.getDocumentElement().getChildNodes().item(12).getTextContent());  

                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                titleLabel.setText(mySnippet.getTitle());
                                ownerTextarea.setText(mySnippet.getCode());
                                langLabel.setText(mySnippet.getLanguage());
                            }
                        });

                        //modifico la visualizzazione in base all'utente
                        if(mySnippet.getCreator().equals(username)){
                            //se sono autore e il testo è modificato, mostro l'ultima modifica
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    if(mySnippet.getMod().equals("Y")){  
                                        editorModification.setText(mySnippet.getCodeMod());
                                        String editorTitle="Proposta di modifica ("+mySnippet.getLastUserMod()+")";                                                
                                        TitledBorder editorBorder = BorderFactory.createTitledBorder(editorTitle);
                                        editorModification.setBorder(editorBorder);
                                        editorModification.setVisible(true);
                                        centerPanel.add(BorderLayout.CENTER,new JScrollPane(ownerModificationPanel));
                                        ownerModificationPanel.setVisible(true);
                                    }
                                    else{
                                        ownerModificationPanel.setVisible(false);
                                    }
                                    centerPanel.add(BorderLayout.NORTH,ownerPanel);
                                }
                            });
                            textToSend=1;
                        }
                        else{
                            //caso in cui non sono l'autore
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    ownerTextarea.setVisible(false);
                                    ownerCode.setText(mySnippet.getCode());

                                    centerPanel.add(BorderLayout.NORTH,new JScrollPane(ownerCode));
                                    centerPanel.add(BorderLayout.CENTER,new JScrollPane(editorTextarea));

                                    ownerModificationPanel.setVisible(false);

                                    String editorTitle="Proposta di modifica";
                                    TitledBorder editorBorder = BorderFactory.createTitledBorder(editorTitle);
                                    editorTextarea.setBorder(editorBorder);
                                    if(mySnippet.getLastUserMod().equals(username)){
                                        editorTextarea.setText(mySnippet.getCodeMod());   
                                    }
                                }
                            });
                            textToSend=2;
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                if(secondCall){
                                    workingPanel.setVisible(true);
                                    messagePanel.setVisible(false);   
                                }
                            }
                        });
                    }
                    else{
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                workingPanel.setVisible(false);
                                message.setText("Lo snippet è attualmente in modifica da parte di un'altro utente");
                                messagePanel.setVisible(true);                            
                                continua.setText("Riprova");
                                continua.setVisible(true);
                            }
                        });
                    }
                }
                else{
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            workingPanel.setVisible(false);
                            messagePanel.setVisible(true);
                        }
                    });
                }
                mx.transform(System.out,answer);
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
    
    
    /**
     * Funzione utilizzata per gestire le chiamate a servizio
     * @param hc oggetto Httpclient
     * @param mx oggetto ManageXml
     * @param tipoRequest tipo di richiesta
     * @return
     * @throws Exception 
     */
    static Document callService(HTTPClient hc,ManageXML mx,String tipoRequest) throws Exception{
	Document question = mx.newDocument();
	Document answer;
	Element questionRoot;
	switch(tipoRequest){
		case "getRequest":
                    questionRoot= question.createElement("getRequest");

                    Element id = question.createElement("id");
                    id.setTextContent(idSnippet);
                    questionRoot.appendChild(id);

                    Element user = question.createElement("user");
                    user.setTextContent(username);
                    questionRoot.appendChild(user);
		break;
                case "setRequest":
                    questionRoot= question.createElement("setRequest");

                    Element idSnippetElement = question.createElement("idSnippet");
                    idSnippetElement.setTextContent(idSnippet);
                    questionRoot.appendChild(idSnippetElement);

                    Element creatorElement = question.createElement("creator");
                    creatorElement.setTextContent(mySnippet.getCreator());
                    questionRoot.appendChild(creatorElement);

                    Element titleElement = question.createElement("title");
                    titleElement.setTextContent(mySnippet.getTitle());
                    questionRoot.appendChild(titleElement);

                    Element codeElement = question.createElement("code");
                    codeElement.setTextContent(mySnippet.getCode());
                    questionRoot.appendChild(codeElement);

                    Element languageElement = question.createElement("language");
                    languageElement.setTextContent(mySnippet.getLanguage());
                    questionRoot.appendChild(languageElement);

                    Element dateCreationElement = question.createElement("dateCreation");
                    dateCreationElement.setTextContent(mySnippet.getDateCreation());
                    questionRoot.appendChild(dateCreationElement);

                    Element modElement = question.createElement("mod");
                    modElement.setTextContent(mySnippet.getMod());
                    questionRoot.appendChild(modElement);

                    Element codeModElement = question.createElement("codeMod");
                    codeModElement.setTextContent(mySnippet.getCodeMod());
                    questionRoot.appendChild(codeModElement);

                    Element userModElement = question.createElement("userMod");
                    userModElement.setTextContent(mySnippet.getUserMod());
                    questionRoot.appendChild(userModElement);

                    Element lastusermodElement = question.createElement("lastusermod");
                    lastusermodElement.setTextContent(mySnippet.getLastUserMod());
                    questionRoot.appendChild(lastusermodElement);                        

                    Element dateLastModProp = question.createElement("dateLastModProp");
                    dateLastModProp.setTextContent(mySnippet.getDateLastMod());
                    questionRoot.appendChild(dateLastModProp);                        

                    Element dateLastModElement = question.createElement("dateLastMod");
                    dateLastModElement.setTextContent(mySnippet.getDateLastMod());
                    questionRoot.appendChild(dateLastModElement);                                                


                    Element content = question.createElement("content");
                    Element usernameElement = question.createElement("username");

                    if(textToSend==1){
                        content.setTextContent(ownerTextarea.getText());
                    }else if (textToSend==2){
                        content.setTextContent(editorTextarea.getText());
                    }
                    usernameElement.setTextContent(username);

                    questionRoot.appendChild(content);
                    questionRoot.appendChild(usernameElement);
		break;
                default:
                    questionRoot= question.createElement("getRequest");      
                break;
	}
	
	question.appendChild(questionRoot);
	mx.transform(System.out,question);
	answer = hc.execute("ModifyServlet",question);    
	return answer;
    }

    /**
     * funzione utilizzata per la creazione di elementi grafici della applet
     */
    private void drawInitialLayout(){
        workingPanel=new JPanel(new BorderLayout());
        messagePanel=new JPanel();
        titlePanel=new JPanel(new GridLayout(1,2));
        centerPanel=new JPanel(new BorderLayout());
        ownerPanel=new JPanel(new BorderLayout());
        ownerModificationPanel=new JPanel(new BorderLayout());
        titleLabel=new JLabel();
        langLabel=new JLabel();
        ownerTextarea=new JTextArea(5, 15);
        editorTextarea=new JTextArea(5, 15);
        editorModification=new JTextArea(5,5);
        editorModification.setEditable(false);
        editorModification.setBackground(new Color(238,238,238));
        ownerCode=new JTextArea(5,15);
        ownerCode.setEditable(false);
        ownerCode.setBackground(new Color(238,238,238));
        message=new JLabel("Contenuto non disponibile");
        done=new JLabel("Salvataggio effettuato con successo");

        Container container=getContentPane();
        container.setLayout(new BorderLayout());

        titlePanel.add(titleLabel);
        titlePanel.add(langLabel);

        ownerPanel.add(BorderLayout.NORTH,new JScrollPane(ownerTextarea));
        ownerModificationPanel.add(BorderLayout.NORTH,new JScrollPane(editorModification));

        salva=new JButton("Salva");
        copy=new JButton("Copia");
        continua=new JButton();
        
        ownerModificationPanel.add(BorderLayout.SOUTH,copy);

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
        JPanel lastButtons=new JPanel(new GridLayout());
        lastButtons.add(salva);
        workingPanel.add(BorderLayout.SOUTH,lastButtons);
        
        messagePanel.add(message);
        messagePanel.add(continua);
        continua.setVisible(false);
        messagePanel.setVisible(false);
        
        container.add(BorderLayout.NORTH,workingPanel);
        container.add(BorderLayout.CENTER,messagePanel);    
    }
}
