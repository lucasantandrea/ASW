<%-- 
    Esame ASW 2014-2015
    Autori: Luca Santandrea, Matteo Mariani, Antonio Leo Folliero, Francesco Degli Angeli
    Matricola: 0900050785
    Gruppo: 1025
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="javax.servlet.*" %>
<%@page import="asw1025.Util"%>
<%@page import="asw1025_lib.SnippetData"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="<%=Util.BASE%>style-sheets/style.css" type="text/css">
        <title>Tutti gli Snippet | Social Code</title>
    </head>
    <body>
        <div id="container">
            <%@ include file="../WEB-INF/jspf/header.jspf" %>
            <div id="content">
                <!--Se sono presenti snippet dell'utente (loggato) nel dbsnippet-->
                <% 
                   ArrayList<SnippetData> snippetList = (ArrayList<SnippetData>)request.getAttribute("mySnippet");
                   if (snippetList.size() > 0) {
                %>
                <p>Sono presenti <%=snippetList.size() %> snippet, eccoli:</p>
                            <% for(int i=0; i<snippetList.size(); i++){ %>
                            <div class="singleItem class<%=i%2 %>">
                                <h2><%= snippetList.get(i).getTitle()%></h2>
                                scritto da <%= snippetList.get(i).getCreator() %>
                                <p>Linguaggio: <%= snippetList.get(i).getLanguage() %></p>
                                <p class="noBottomMargin">Codice: </p><textarea name="Code" id="textArea" disabled><%= snippetList.get(i).getCode()%></textarea><br/><br/>
                            <% if(snippetList.get(i).getMod().equals("Y")){ %>
                            <h4>Proposta di modifica:</h4> <textarea name="Code_mod" id="textArea" disabled><%= snippetList.get(i).getCode_mod()%></textarea><br/>
                             modifica di: <%= snippetList.get(i).getLastusermod()%><br/><br/>
                            <%
                             }
                            %>
                            </div>
                            <%
                         }
                   }
                            %>
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
