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
<%@page import="asw1025.SnippetData"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="<%=Util.BASE%>style-sheets/style.css" type="text/css">
        <title>I Miei Snippet | Snippet share</title>
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
                <!--Se esistono sinppet dell'utente nel dbsnippet-->
                <%                      
                   ArrayList<SnippetData> snippetList = (ArrayList<SnippetData>)request.getAttribute("mySnippet");
                   if (snippetList.size() > 0) {
                %>
                <p>Hai scritto in totale <b><%=snippetList.size() %></b> snippet, eccoli:</p>
                <div>
                    <% for(int i=0; i<snippetList.size(); i++){ %>
                    
                    <div class="singleItem class<%=i%2 %>">
                        <h2><%= snippetList.get(i).getTitle()%></h2>
                        <div class="information">
                        <p>Linguaggio: <%= snippetList.get(i).getLanguage() %><br/>
                        Data di creazione: <%= snippetList.get(i).getDateCreation() %><br/>
                        Data ultima modifica: <%= snippetList.get(i).getDateLastModProp() %>
                        </p>
                        
                        </div>
                        <div class="actions">
                            <form name="submitViewForm<%=i%>" action="<%= Util.BASE %>DisplayServlet" method="POST">
                                <input type="hidden" name="id" value="<%=snippetList.get(i).getId()%>">
                                <div id="buttonView" class="buttonBackground">
                                   <a HREF="javascript:document.submitViewForm<%=i%>.submit()">Vedi</a>
                                </div>
                            </form>
                            <form name="submitModifyForm<%=i%>" action="<%= Util.BASE %>EditServlet" method="POST">
                                <input type="hidden" name="id" value="<%=snippetList.get(i).getId()%>">
                                <input type="hidden" name="user" value="<%=session.getAttribute("user")%>">
                                 <div id="buttonModify" class="buttonBackground">
                                    <a HREF="javascript:document.submitModifyForm<%=i%>.submit()">Modifica</a>
                                 </div>
                            </form>
                            <form name="deleteSnippetForm<%=i%>" action="<%= Util.BASE %>DeleteServlet" method="POST">
                                 <input type="hidden" name="id" value="<%=snippetList.get(i).getId()%>">
                                 <a HREF="javascript:document.deleteSnippetForm<%=i%>.submit()" onclick="return confirm('Vuoi veramente eliminare questo snippet?');">Elimina</a>
                            </form>
                        </div>
                        <div class="clear"></div>
                        <% 
                            if(snippetList.get(i).getMod().equals("Y")){
                        %>
                        Questo snippet contiene una modifica di <b><%= snippetList.get(i).getLastUserMod() %></b>, del <b><%= snippetList.get(i).getDateLastMod() %></b>
                        <%
                            }
                        %>
                    </div>
                    <%
                    }
                    %>

                </div>
                <!--Se non esistono snippet dell'utente loggato-->
                <% } else {%>
                    <div>
                        <p>Non hai caricato nessuno snippet.</p>
                    </div>
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
