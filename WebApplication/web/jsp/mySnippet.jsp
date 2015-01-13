<%-- 
    Document   : mySnippet
    Created on : 13-gen-2015, 20.46.30
    Author     : Francesco
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="javax.servlet.*" %>
<%@page import="asw1025.Util"%>
<%@page import="asw1025.SnippetData"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="<%=Util.BASE%>style-sheets/style.css" type="text/css">
        <title>My Snippet</title>
    </head>
    <%--
        Registrazione degli eventi di Onload e OnUnload della pagina, 
        per chiamare le funzioni Javascript per la registrazione/deregistrazione al servizio di notifiche
    
    <body onload="registerToReceiveNotification();" onunload="deregisterNotification();">        
         <% 
            // Controllo se la sessione è scaduata, in caso affermativo si ripresenta la home nella quale si deve rifare il login
            if (session == null || session.getAttribute("user") == null) {
                System.out.println("timeout session expired!!");
                RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
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
                    <!--CASO: sono presenti poesie dell'utente (loggato) nel dbsnippet-->
                    <% 
                       if (request.getAttribute("mySnippet")!=null){
                       ArrayList<SnippetData> snippetList = (ArrayList<SnippetData>)request.getAttribute("mySnippet");
                       if (snippetList.size() > 0) {
                    %>
                        <div class="contentBox">
                            <table border="1" class="tableMyPoemResult">
                                <tr>
                                    <td>Title</td>
                                    <td>Language</td>
                                    <td>Data</td>
                                    <td>Modify</td>
                                    <td>Delete</td>
                                </tr>
                                <% for(int i=0; i<snippetList.size(); i++){ %>
                                    <tr>
                                        <td> <%= snippetList.get(i).getTitle()%></td>
                                        <td> <%= snippetList.get(i).getLanguage() %></td>
                                        <td> <%= snippetList.get(i).getDate() %></td>
                                        <td>
                                            <form name="submitPoemForm<%=i%>" action="<%= Util.BASE %>jsp/loadSnippet.jsp" method="POST">
                                                 <input type="hidden" name="id" value="<%=snippetList.get(i).getId()%>">
                                                 <input type="hidden" name="titolo" value="<%=snippetList.get(i).getTitle()%>">
                                                 <input type="hidden" name="testo" value="<%=snippetList.get(i).getCode()%>">
                                                 <input type="hidden" name="argomento" value="<%=snippetList.get(i).getLanguage()%>"> 
                                                 <div id="buttonModifica" class="buttonBackground">
                                                    <a HREF="javascript:document.submitPoemForm<%=i%>.submit()">Modifica</a>
                                                 </div>
                                            </form><br>
                                        </td>
                                        <td>
                                            <form name="deletePoemForm<%=i%>" action="<%= Util.BASE %>DeleteServlet" method="POST">
                                                 <input type="hidden" name="id" value="<%=snippetList.get(i).getId()%>">
                                                 <div id="buttonElimina" class="buttonBackground">
                                                    <a HREF="javascript:document.deletePoemForm<%=i%>.submit()">Elimina</a>
                                                 </div>
                                            </form><br>
                                        </td>
                                    </tr>
                                <%
                                }
                                %>
                            </table>
                        </div>
                    <!--CASO: non esistono snippet dell'utente loggato-->
                    <% }} else {%>
                        <div class="contentBox">
                            You don't have snippet.. MARIO BRAVET
                        </div>
                    <% } %>
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
