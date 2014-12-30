<%@page import="com.sun.xml.bind.Util"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="javax.servlet.*" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>

    
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Registrazione</title>
    
 
    <body>
        <div id="container">
            <div id="navbar">
                <%@ include file="../WEB-INF/jspf/navbar.jspf" %>
            </div>
            <div id="content">
                <div class="miniInsideContent">
                    <form name="registrationForm" method="POST" class="contentBox">
                        
                        First name: <input type="text" name="firstname" value=<%if(request.getAttribute("nome")!=null){%> <%= request.getAttribute("nome")%><%} else {%>""<%}%> ><br> 
                        Last name: <input type="text" name="lastname" value=<%if(request.getAttribute("cognome")!=null){%> <%= request.getAttribute("cognome")%><%} else {%>""<%}%> ><br>
                        User name <input type="text" name="user">
                        <%
                        // Controllo se l'username immesso è univoco
                        if(request.getAttribute("errorUserRegistration")!=null){ %>
                            <label id="errorRegistration">Username "<%= request.getAttribute("errorUserRegistration") %>" gia' utilizzato!</label>
                        <% } %><br>
                        Password: <input type="password" name="pass"><br>
                        <input type="submit" value="Conferma">
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
    

