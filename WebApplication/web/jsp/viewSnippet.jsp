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
        <title>Vedi snippet | Snippet share</title>
    </head>
    <body>
        <div id="container">
            <%@ include file="../WEB-INF/jspf/header.jspf" %>
            <div id="content">
                <div class="formClass">
                 <%                       
                       ArrayList<SnippetData> snippetList = (ArrayList<SnippetData>)request.getAttribute("IdSnippet");
                       if (snippetList.size() == 1 ) {
                    %>
                
                            
                        <label>Titolo:</label><%= snippetList.get(0).getTitle()%><br/><br/>
                        <label>Linguaggio:</label><%= snippetList.get(0).getLanguage()%><br/><br/>
                        <label>Codice:</label>
                        <textarea rows="13" cols="100" name="code" id="textArea" disabled><%=snippetList.get(0).getCode()%></textarea><br/><br/>
                        <%if(snippetList.get(0).getMod().equals("Y")){%>
                        <label><b>Proposta di modifica: </b></label>
                        <textarea rows="6" cols="100" name="codeMod" id="textArea" disabled><%=snippetList.get(0).getCodeMod()%></textarea><br/>
                        <label>di: </label><%=snippetList.get(0).getLastUserMod()%><br/>
                        <label>data: </label><%=snippetList.get(0).getDateLastMod() %>
                                <%
                                 }
                                %>
                                
                            <%} else {%> 
                           
                                <p>Si è verificato un errore, si prega di riprovare</p>
                            <%
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
