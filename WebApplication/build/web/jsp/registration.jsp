<%--     
    Esame ASW 2014-2015
    Autori: Luca Santandrea
    Matricola: 0900050785
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
        <!-- TODO: definire il path relativo!! -->
        <link rel="stylesheet" href="http://localhost:8080/WebApplication/style-sheets/style.css" type="text/css">
        <title>Home</title>
    </head>

    <body>
        
        <% 
            // Controllo se ho un utente loggato, in caso affermativo faccio redirect alla home
            if ((session.getAttribute("user") != null)) {
                System.out.println("someone is logged!");
                
                //TODO: utilizzo realpath invece che path assoluto!
                response.sendRedirect("http://localhost:8080/WebApplication/index.jsp");
            }
        %>
        
        <div id="container">
            <div id="navbar">
                <%@ include file="../WEB-INF/jspf/navbar.jspf" %>
            </div>
            <div id="content">
                <div class="miniInsideContent">
                    <form name="registrationForm" method="POST" class="contentBox" action="<%= Util.BASE %>RegistrationServlet" onsubmit="return validateForm();">

                        First name: <input type="text" name="nome" value=<%if(request.getAttribute("nome")!=null){%> <%= request.getAttribute("nome")%><%} else {%>""<%}%> ><br> 
                        Last name: <input type="text" name="cognome" value=<%if(request.getAttribute("cognome")!=null){%> <%= request.getAttribute("cognome")%><%} else {%>""<%}%> ><br>
                        User name <input type="text" name="user">
                        <%
                        // Controllo se l'username immesso è univoco
                        if(request.getAttribute("errorUserRegistration")!=null){ %>
                            <label id="errorRegistration">Username "<%= request.getAttribute("errorUserRegistration") %>" gia' utilizzato!</label>
                        <% } %><br>
                        Password: <input type="password" name="pass"><br>
                        <input type="submit" value="Conferma">
                    </form>

                </div>
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
         * Funzione che permette di controllare che siano presenti tutti i valori nei campi di registrazione
         */
        function validateForm(){
            var name=document.forms["registrationForm"]["nome"].value;
            var lastname=document.forms["registrationForm"]["cognome"].value;
            var user=document.forms["registrationForm"]["user"].value;
            var psw=document.forms["registrationForm"]["pass"].value;
            
            if(name===null || name==="" || lastname===null || lastname==="" || user===null || user==="" || psw===null || psw==="" ){  
                alert("Si prega di compilare tutto il form");
                return false;              
            }else{
                return true;
            }    
        }
    </script>
    </body>
</html>