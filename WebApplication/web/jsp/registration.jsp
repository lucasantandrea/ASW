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
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="<%= Util.BASE %>style-sheets/style.css" type="text/css">
        <title>Registrazione | Social Code</title>
    </head>

    <body>
        
        <% 
            // Controllo se ho un utente loggato, in caso affermativo faccio redirect alla home
            if ((session.getAttribute("user") != null)) {
                System.out.println("someone is logged!");
                response.sendRedirect(Util.BASE + "index.jsp");
            }
        %>
        
        <div id="container">
            <%@ include file="../WEB-INF/jspf/header.jspf" %>
            <div id="content">
                <p>Compila il form seguente per completare la registrazione.</p>
                <form name="registrationForm" method="POST" class="formClass" action="<%= Util.BASE %>RegistrationServlet" onsubmit="return validateForm();">

                    <p><label for="nome">Nome: </label><input type="text" name="nome" id="nome" value=<%if(request.getAttribute("nome")!=null){%> <%= request.getAttribute("nome")%><%} else {%>""<%}%> onfocus="clearForm()"></p>
                    <p><label for="cognome">Cognome: </label><input type="text" name="cognome" id="cognome" value=<%if(request.getAttribute("cognome")!=null){%> <%= request.getAttribute("cognome")%><%} else {%>""<%}%> onfocus="clearForm()"></p>
                    <p><label for="user">Username: </label><input type="text" name="user" id="user" onfocus="clearForm()">
                    <%
                    // Controllo se l'username immesso è univoco
                    if(request.getAttribute("errorUserRegistration")!=null){ %>
                        <span id="errorRegistration">Username "<%= request.getAttribute("errorUserRegistration") %>" gia' utilizzato!</span>
                    <% } %></p>
                    <p><label for="pass">Password: </label><input type="password" name="pass" id="pass" onfocus="clearForm()"></p>
                    <p><input type="submit" value="Conferma" class="submit"></p>
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
            
    <script>
        /*
         * Funzione che controlla che siano compilati tutti i valori nel form
         */
        function validateForm(){
            var name=document.forms["registrationForm"]["nome"].value;
            var lastname=document.forms["registrationForm"]["cognome"].value;
            var user=document.forms["registrationForm"]["user"].value;
            var pass=document.forms["registrationForm"]["pass"].value;
            
            if(name===null || name==="" || lastname===null || lastname==="" || user===null || user==="" || pass===null || pass==="" ){  
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
    </body>
</html>