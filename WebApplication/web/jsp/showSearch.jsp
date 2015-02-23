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
        <title>Cerca snippet | Social code</title>
    </head>
        <div id="container">
            <%@ include file="../WEB-INF/jspf/header.jspf" %>
            <div id="content">
                <!--Se sono presenti snippet dell'utente loggato nel dbsnippet-->
                <% 
                   ArrayList<SnippetData> snippetList = (ArrayList<SnippetData>)request.getAttribute("mySnippet");
                   if (snippetList.size() > 0) {
                %>
                <p><b><%=snippetList.size()%></b> risultati:</p>
                <% for(int i=0; i<snippetList.size(); i++){ %>
                <div class="singleItem class<%=i%2 %>">
                    <h2><%= snippetList.get(i).getTitle()%></h2>
                    <p>scritto da <%= snippetList.get(i).getCreator()%></p>
                    <div class="information">
                        <p>Linguaggio: <%= snippetList.get(i).getLanguage() %><br/>
                        Data di creazione: <%= snippetList.get(i).getDate_creation() %><br/>
                        Data ultima modifica (proprietario): <%= snippetList.get(i).getDate_lastmodprop() %><br/>
                        Data ultima modifica (generale): <%= snippetList.get(i).getDate_lastmod() %>
                        </p>
                    </div>
                    <div class="actions">
                        <form name="submitViewForm<%=i%>" action="<%= Util.BASE %>ViewServlet" method="POST" onsubmit="return validateForm();">
                             <input type="hidden" name="id" value="<%=snippetList.get(i).getId()%>">
                             <div id="buttonView" class="buttonBackground">
                                <a HREF="javascript:document.submitViewForm<%=i%>.submit()">Vedi</a>
                             </div>
                        </form>
                        <form name="submitModifyForm<%=i%>" action="<%= Util.BASE %>EditServlet" method="POST" onsubmit="return validateForm();">
                            <input type="hidden" name="id" value="<%=snippetList.get(i).getId()%>">
                            <input type="hidden" name="user" value="<%=session.getAttribute("user")%>">
                             <div id="buttonModify" class="buttonBackground">
                                <a HREF="javascript:document.submitModifyForm<%=i%>.submit()">Modifica</a>
                             </div>
                        </form>
                    </div>
                    <div class="clear"></div>
                </div>
                <%
                }
                %>
                <!--CASO: non esistono snippet dell'utente loggato-->
                <% } else {%>
                <p>Non ci sono risultati.</p>
                <% } %>
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
