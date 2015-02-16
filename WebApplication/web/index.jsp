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
<%@page import="asw1025.Util"%>
<%@page import="asw1025.SnippetData"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="<%= Util.BASE %>style-sheets/style.css" type="text/css">
        <title>Home | Social Code</title>
    </head>
    
    <body>
        <div id="container">
            <%@ include file="WEB-INF/jspf/header.jspf" %>
            <div id="content">
                <p>Benvenuto, su questo sito è possibile condividere le proprie idee in codice con gli atri utenti.</p>
                <p>Aggiungi i tuoi snippet o visualizza quelli degli altri utenti. Puoi anche aiutare la comunità proponendo le tue modifiche!</p>
                <% String user=null; 
                if(session.getAttribute("user")!=null){
                    user = session.getAttribute("user").toString();
                } 
                if(user==null){ 
                %>
                <p>Per cominciare, esegui il login alla piattaforma:</p>
                <% } %>
                <%@ include file="WEB-INF/jspf/login.jspf" %>
            </div>

            <div id="sidebar">
                <%@ include file="WEB-INF/jspf/sidebar.jspf"%> 
            </div>
            <div id="footer">
                <%@ include file="WEB-INF/jspf/footer.jspf" %>
            </div>           
        </div>
    </body>
</html>
