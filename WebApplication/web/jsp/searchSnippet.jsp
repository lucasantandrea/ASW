<%-- 
    Document   : loadSnippet
    Created on : 13-gen-2015, 16.58.12
    Author     : Francesco
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
        <title>Search snippet</title>
    </head>
    
      <div id="container"> 
          <div id="navbar">
          <%@ include file="../WEB-INF/jspf/navbar.jspf" %>
          </div>
           <div id="content">           
               
                <div class="miniInsideContent">
                        <form name="searchForm" action="<%=Util.BASE%>searchSnippetServlet" method="POST" onsubmit="return validateForm();" class="contentBox">
                   
                            <!-- New snippet -->
                                Title: <input type="text" name="title" ><br><br>
                                
                                Author : <input type="text" name="author" ><br><br>
                                
                                Language: <select name="languageResearch">
                                    <option name="---" >---</option>
                                    <option name="Java" >Java</option>
                                    <option name="JavaScript"  >JavaScript</option>
                                    <option name="C++" >C++</option>
                                    <option name="C#"  >C#</option>
                                    <option name="PHP"  >PHP</option>                                    
                                </select><br><br>   
                                OrderBy: <select name="orderResearch">
                                    <option name="---" >---</option>
                                    <option name="Creation" >Creation Data</option>Owner Update Data
                                    <option name="OwnerUpdate"  >Owner Update Data</option>      
                                    <option name="Modification"  >Users Update Data</option>  
                                </select><br><br>   
                            <input type="submit" value="Find">
                        </form>
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