<%-- 
    Document   : viewSnippet
    Created on : 16-gen-2015, 15.59.02
    Author     : Francesco
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
        <title>View snippet</title>
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
  
        <div id="container">
            <div id="navbar">
                <%@ include file="../WEB-INF/jspf/navbar.jspf" %>
            </div>
            <div id="content">
                <div class="miniInsideContent">
                            <% if(request.getParameter("id")!=null){ %>
                            Title: <input type="text" name="Title" value="<%= request.getParameter("title")%>" disabled><br><br>
                            Code: <textarea rows="6" cols="100" name="Code" id="textArea" disabled><%= request.getParameter("code")%></textarea><br><br>
                            Language: <input type="text" name="Title" value="<%= request.getParameter("language")%>" disabled><br><br>
                                <% if(request.getParameter("mod")!=null){ %>
                                 Code Mod: <textarea rows="6" cols="100" name="Code_mod" id="textArea" disabled><%= request.getParameter("code_mod")%></textarea><br><br>
                                 By: <input type="text" name="User_mode" value="<%= request.getParameter("user_mod")%>" disabled><br><br>
                               
                                <%
                                 }
                                %>
                            <%--
                                <select name="languageResearch">
                                    <option name="Java" <% if(((String)request.getParameter("language")).equals("Java")) { %> <%= "selected" %><% } %>>Java</option>
                                     <option name="Javascript"  <% if(((String)request.getParameter("language")).equals("Javascript")) { %> <%= "selected" %><% } %> >Javascript</option>     
                                    <option name="C++"  <% if(((String)request.getParameter("language")).equals("C++")) { %> <%= "selected" %><% } %> >C++</option>
                                    <option name="C#"  <% if(((String)request.getParameter("language")).equals("C#")) { %> <%= "selected" %><% } %> >C#</option>
                                    <option name="PHP"  <% if(((String)request.getParameter("language")).equals("PHP")) { %> <%= "selected" %><% } %> >PHP</option>                                
                                </select><br>
                                --%>
                                <input type="hidden" name="idSnippet" value="<%= request.getParameter("id")%>"><br>

                            <%} else {%> 
                          
                           
                                Error
                                <%--
                                Title: <input type="text" name="Title" ><br><br>
                                Code: <textarea rows="6" cols="100" name="Code"></textarea><br><br>
                                Language: <select name="languageResearch">
                                    <option name="Java" >Java</option>
                                    <option name="JavaScript"  >JavaScript</option>
                                    <option name="C++" >C++</option>
                                    <option name="C#"  >C#</option>
                                    <option name="PHP"  >PHP</option>                                    
                                </select><br>
                                <input type="hidden" name="idSnippet" value=""><br>                         
                            <input type="submit" value="Salva">
                            --%>
                            <%
                              }
                            %>
                            
                </div>
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
        
        /*
         * Funzione che permette di controllare che siano presenti tutti i valori nei campi di registrazione
         */
        function validateForm(){
            var titolo=document.forms["uploadForm"]["Titolo"].value;
            var testo=document.forms["uploadForm"]["Testo"].value;
            
            if(titolo===null || titolo==="" || testo===null || testo==="" ){
                
                alert("Dati mancanti!");
                return false;
                
            }else{
                return true;
            }
        }
    </script>
--%>
    
</html>
