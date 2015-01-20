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
        <title>All Snippet</title>
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
                        
                                <% for(int i=0; i<snippetList.size(); i++){ %>
                                    Title: <%= snippetList.get(i).getTitle()%><br><br>
                                    Author: <%= snippetList.get(i).getUser() %><br><br>
                                    Code: <textarea rows="6" cols="100" name="Code" id="textArea" disabled><%= snippetList.get(i).getCode()%></textarea><br><br>
                                    Language: <%= snippetList.get(i).getLanguage() %><br><br>
                                <% if(snippetList.get(i).getMod()!=""){ %>
                                 Code Mod: <textarea rows="6" cols="100" name="Code_mod" id="textArea" disabled><%= snippetList.get(i).getCode_mod()%></textarea><br><br>
                                 By: <%= snippetList.get(i).getUser_mod()%><br><br>
                               
                                <%
                                 }
                             }
                       }
                      
                                %>
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
