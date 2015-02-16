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
        <title>Cerca codice | Social Code</title>
    </head>
    <body>
        <% 
            // Controllo se ho un utente loggato, in caso affermativo faccio redirect alla home
            if ((session.getAttribute("user") == null)) {
                response.sendRedirect(Util.BASE + "index.jsp");
            }
        %>
        <div id="container"> 
            <%@ include file="../WEB-INF/jspf/header.jspf" %>
            <div id="content">           
                <form class="formClass" action="<%=Util.BASE%>searchSnippetServlet" method="POST">

                    <!-- New snippet -->
                    <p><label for="title">Titolo: </label><input type="text" name="title" id="title"></p>
                    <p><label for="author">Autore: </label><input type="text" name="author" id="author"></p>
                    <p><label for="linguaggio">Linguaggio: </label><select name="languageResearch" id="linguaggio">
                        <option name="---" >---</option>
                        <option name="Java" >Java</option>
                        <option name="JavaScript"  >JavaScript</option>
                        <option name="C++" >C++</option>
                        <option name="C#"  >C#</option>
                        <option name="PHP"  >PHP</option>                                    
                    </select></p>   
                    <p><label for="ordina">Ordina per: </label><select name="orderResearch" id="ordina">
                        <option name="---" >---</option>
                        <option name="Creation" >Creation Data</option>
                        <option name="OwnerUpdate"  >Owner Update Data</option>      
                        <option name="Modification"  >Users Update Data</option>  
                    </select></p>
                    <input type="submit" class="submit" value="Cerca">
                </form>
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