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
        <link rel="stylesheet" href="<%= Util.BASE %>style-sheets/style.css" type="text/css">
        <title>Home</title>
    </head>
    

    <body>
        <div id="container">
            <div id="navbar">
                <%@ include file="../WEB-INF/jspf/navbar.jspf" %>
            </div>
            <div id="content">
                <p>Welcome, we have to do the description. Please start with login / registration...</p>
                <%@ include file="../WEB-INF/jspf/login.jspf" %>
            </div>

            <div id="sidebar">
                <%@ include file="../WEB-INF/jspf/sidebar.jspf"%> 

            </div>
            <div id="footer">
                <%@ include file="../WEB-INF/jspf/footer.jspf" %>
            </div>           
        </div>
    </body>
</html>
