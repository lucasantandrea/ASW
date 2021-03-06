<%-- 
    Esame ASW 2014-2015
    Autori: Luca Santandrea, Matteo Mariani, Antonio Leo Folliero, Francesco Degli Angeli
    Matricola: 0900050785
    Gruppo: 1025
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
        <title>Scrivi nuovo snippet | Snippet share</title>
    </head>
    <body>
        <div id="container">
            <%@ include file="../WEB-INF/jspf/header.jspf" %>
            <div id="content">
                <p>Inserisci un nuovo snippet compilando il seguente form:</p>
                <form name="uploadForm" action="<%=Util.BASE%>AddSnippetServlet" method="POST" onsubmit="return validateForm();" class="formClass">
                    <!-- New snippet -->
                    <p>
                        <label for="title">Titolo: </label><input type="text" name="title" id="title" onfocus="clearForm()">
                    </p>
                    <p>
                        <label for="language">Linguaggio: </label>
                        <select name="language" id="language">
                            <option name="Java" >Java</option>
                            <option name="JavaScript"  >JavaScript</option>
                            <option name="C++" >C++</option>
                            <option name="C#"  >C#</option>
                            <option name="PHP"  >PHP</option>                                    
                        </select>
                    </p>
                    <p>
                        <label for="code">Codice: </label><br/>
                        <textarea rows="6" cols="100" name="code" id="code" onfocus="clearForm()"></textarea>
                    </p>
                    <input type="hidden" name="idSnippet" value="">
                    <input type="submit" value="Salva" class="submit">
                </form>
                <div id="errorDiv">Si prega di compilare tutti i campi!</div>
            </div>            
            <div id="sidebar">
                <%@ include file="../WEB-INF/jspf/sidebar.jspf" %>
            </div>            
            <div id="footer">
                <%@ include file="../WEB-INF/jspf/footer.jspf" %>
            </div>
        </div>

    <script>
         /*
         * Funzione che permette di controllare che siano presenti tutti i valori nei campi di inserimentoo
         */
        function validateForm(){
            var title=document.getElementById("title").value;
            var code=document.getElementById("code").value;    
            if(title===null || title==="" || code===null || code==="" ){
                document.getElementById("errorDiv").style.display="block";
                return false;               
            }else{
                return true;
            }
        }
        //funzione che nasconde il messaggio di errore
        function clearForm(){
            if(document.getElementById("errorDiv").style.display!="none"){
                document.getElementById("errorDiv").style.display="none";
            }
        }
    </script>
    </body>
</html>