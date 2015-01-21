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
        <div id="container">
            <div id="navbar">
                <%@ include file="../WEB-INF/jspf/navbar.jspf" %>
            </div>
            <div id="content">
                <div class="miniInsideContent">
                    <!--<form name="snippetForm" action="%=Util.BASE%>MySnippetServlet" method="POST"></form>-->
                    
                    <!--CASO: sono presenti poesie dell'utente (loggato) nel dbsnippet-->
                    <% 
           
                      
                       ArrayList<SnippetData> snippetList = (ArrayList<SnippetData>)request.getAttribute("mySnippet");
                       if (snippetList.size() > 0) {
                    %>
                        <div class="contentBox">
                            <table border="1" class="tableMyPoemResult">
                                <tr>
                                    <td>Title</td>
                                    <td>Language</td>
                                    <td>Author</td>
                                    <td>Creation Data</td>
                                    <td>Last Mod</td>
                                    <td>View</td>
                                </tr>
                                <% for(int i=0; i<snippetList.size(); i++){ %>
                                    <tr>
                                        <td> <%= snippetList.get(i).getTitle()%></td>
                                        <td> <%= snippetList.get(i).getLanguage() %></td>
                                        <td> <%= snippetList.get(i).getUser() %></td>
                                        <td> <%= snippetList.get(i).getDate_creation() %></td>
                                        <td> <%= snippetList.get(i).getDate_lasmod() %></td>
                                        <td>
                                            <form name="submitViewForm<%=i%>" action="<%= Util.BASE %>jsp/viewSnippet.jsp" method="POST">
                                                 <input type="hidden" name="id" value="<%=snippetList.get(i).getId()%>">
                                                 <input type="hidden" name="title" value="<%=snippetList.get(i).getTitle()%>">
                                                 <input type="hidden" name="code" value="<%=snippetList.get(i).getCode()%>">
                                                 <input type="hidden" name="language" value="<%=snippetList.get(i).getLanguage()%>"> 
                                                 <%if(snippetList.get(i).getMod().equals("Y")){%>
                                                 <input type="hidden" name="mod" value="<%=snippetList.get(i).getMod()%>">
                                                 <input type="hidden" name="code_mod" value="<%=snippetList.get(i).getCode_mod()%>">
                                                 <input type="hidden" name="user_mod" value="<%=snippetList.get(i).getUser_mod()%>">
                                                  <%
                                                   }
                                                   %>
                                                 <div id="buttonView" class="buttonBackground">
                                                    <a HREF="javascript:document.submitViewForm<%=i%>.submit()">View</a>
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
                    <% } else {%>
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
