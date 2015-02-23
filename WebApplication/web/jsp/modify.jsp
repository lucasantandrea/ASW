<%-- 
    Esame ASW 2014-2015
    Autori: Luca Santandrea, Matteo Mariani, Antonio Leo Folliero, Francesco Degli Angeli
    Matricola: 0900050785
    Gruppo: 1025
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="<%=Util.BASE%>style-sheets/style.css" type="text/css">
        <title>Modifica snippet | Social Code</title>
    </head>
    <body>
        <div id="container">
            <%@ include file="../WEB-INF/jspf/header.jspf" %>
            <div id="content">
                <div class="formClass">
                    <div class="appletContainer">
                        <APPLET codebase="applet" code="asw1025.Applet.class" archive="Applet1.jar,Lib1.jar" width=500 height=350>
                            <param name="username" value='<%=request.getParameter("user")%>'/>
                            <param name="idSnippet" value='<%=request.getParameter("id")%>'/>    
                        </APPLET>
                    </div>
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
