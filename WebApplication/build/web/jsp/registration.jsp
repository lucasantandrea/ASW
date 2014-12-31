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
        <link rel="stylesheet" href="../style-sheets/style.css" type="text/css">
        <title>Home</title>
    </head>

    <body>
        <div id="container">
            <div id="navbar">
                <%@ include file="../WEB-INF/jspf/navbar.jspf" %>
            </div>
            <div id="content">
                <div class="miniInsideContent">
                    <form name="registrationForm" method="POST" class="contentBox" action="../RegistrationServlet">

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
    </body>
</html>