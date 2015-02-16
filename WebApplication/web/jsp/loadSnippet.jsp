<%-- 
    Esame ASW 2014-2015
    Autori: Luca Santandrea, Matteo Mariani, Antonio Leo Folliero, Francesco Degli Angeli
    Matricola: 0900050785
    Gruppo: 1025
--%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="javax.servlet.*" %>
<%@page import="asw1025.Util"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="<%=Util.BASE%>style-sheets/style.css" type="text/css">
        <title>Scrivi nuovo snippet | Social Code</title>
    </head>
    
    <%--
        Registrazione degli eventi di Onload e OnUnload della pagina, 
        per chiamare le funzioni Javascript per la registrazione/deregistrazione al servizio di notifiche,
        e per il controllo del scadenza della sessione.
    
    <body  onload="DisplaySessionTimeout(); registerToReceiveNotification();" onunload="deregisterNotification();">
        
        <% 
            // Controllo se la sessione è scaduata, in caso affermativo si ripresenta la home nella quale si deve rifare il login
            if (session == null || session.getAttribute("user") == null) {
                System.out.println("timeout session expired!!");
                RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
                rd.forward(request, response);            
            }
        %>
    --%>
    <body>
        <div id="container">
            <%@ include file="../WEB-INF/jspf/header.jspf" %>
            <div id="content">
                <p>Inserisci un nuovo snippet compilando il seguente form:</p>
                <form name="uploadForm" action="<%=Util.BASE%>AddSnippetServlet" method="POST" onsubmit="return validateForm();" class="formClass">
                    <!-- New snippet -->
                    <p><label for="title">Titolo: </label><input type="text" name="Title" id="title" onfocus="clearForm()"></p>
                    <p><label for="language">Linguaggio: </label><select name="languageResearch" id="code">
                        <option name="Java" >Java</option>
                        <option name="JavaScript"  >JavaScript</option>
                        <option name="C++" >C++</option>
                        <option name="C#"  >C#</option>
                        <option name="PHP"  >PHP</option>                                    
                    </select></p>
                    <p><label for="code">Codice: </label><br/><textarea rows="6" cols="100" name="Code" id="code" onfocus="clearForm()"></textarea></p>
                        <input type="hidden" name="idSnippet" value="">
                    <input type="submit" value="Salva" class="submit">
                </form>
                <div id="errorDiv">Si prega di compilare tutti i campi!</div>
            </div>            
            <div id="sidebar">
                <%@ include file="../WEB-INF/jspf/sidebar.jspf" %>
            </div>            
            <div id="footer">
                <%@ include file="../WEB-INF/jspf/footer.jspf" %>
            </div>
        </div>
    </body>
    
    <%--     TODO 
    <script>
        // Assunzione del valore di timeout massimo consentito dal server
        var sessionTimeout = "<%= session.getMaxInactiveInterval()%>";

        /*
        * Funzione che viene invocata ogni minuto e controlla se la sessione è scaduta, 
        * in tal caso ridireziona l'utente alla homepage dove potrà riloggarsi.
        */ 
       function DisplaySessionTimeout()
        {
            console.log("chiamo session timeout val: ",sessionTimeout);
            
            if (sessionTimeout >= 0){
                sessionTimeout = sessionTimeout - 60;
            }else
            {
                window.location = "<%= Util.BASE %>index.jsp";
            }
        }

        setInterval("DisplaySessionTimeout()",60000);
        

    </script>
--%>
    
    <script>
    
        /*
         * Funzione che permette di controllare che siano presenti tutti i valori nei campi di inserimentoo
         */
        function validateForm(){
            var title=document.getElementById("title").value;
            var code=document.getElementById("code").value;    
            
            if(title===null || title==="" || title===null || title==="" ){
                document.getElementById("errorDiv").style.display="block";
                return false;               
            }else{
                return true;
            }
        }
        
        //funzione che nasconde il messaggio di errore
        function clearForm(){
            if(document.getElementById("errorDiv").style.display!="none"){
                document.getElementById("errorDiv").style.display="none";
            }
        }
    </script>
</html>