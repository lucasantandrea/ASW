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
        <title>Tutti gli Snippet | Snippet share</title>
        <script type="text/javascript" src="<%=Util.BASE%>multimedia/knockout.js"></script>
        <script type="text/javascript" src="<%=Util.BASE%>multimedia/jquery.min.js"></script>
    </head>
    <body onload="totalSnippet()">
        <div id="container">
            <%@ include file="../WEB-INF/jspf/header.jspf" %>
            <div id="content">
            
                <div id="snippetList" data-bind="foreach: snippets">
                    <div class="singleItem" data-bind="css: { class0: $index()%2 }">
                        <h2 id="title" data-bind="text: title"></h2>
                        <p>Scritto da <span data-bind="text: creator"></span></p>
                        <div class="information">
                                <p>Linguaggio: <span data-bind="text: language"></span></p>
                                <p>Data di creazione: <span data-bind="text: dateCreation"></span></p>
                                <p>Data di ultima modifica (generale): <span data-bind="text: dateLastMod"></span></p>
                                <p>Data di ultima modifica (proprietario): <span data-bind="text: dateLastModProp"></span></p>
                        </div>
                        <div class="information codeDiv">
                                <p>Codice: </p>
                                <textarea rows="13" cols="100" name="code" id="textArea1" disabled="" data-bind="text: code"></textarea>
                                <div data-bind="visible: isMod()">
                                    <p>Proposta di modifica:</p>
                                    <textarea rows="13" cols="100" name="codeMod" id="textAreaMod3" disabled="" data-bind="text: codeMod"></textarea>
                                    <p>di: <span data-bind="text: lastUserMod"></span></p>
                                </div>
                        </div>
                        <div class="actions">
                                <button type="button" value="Vedi" class="vedi">Vedi</button>
                        </div>
                        <div class="clear"></div>
                    </div>
                </div>
                
                <script>
                    //array globale di snippet che andranno a popolare l'observableArray
                    var SnippetArray=[];
                    
                    //classe che descrive ogni snippet (view)
                    function Snippet(creator,title,code,language,dateCreation,mod,codeMod,lastUserMod,dateLastModProp,dateLastMod) {
                        var self = this;
                        self.creator= creator;
                        self.title= title;
                        self.code= code;
                        self.language= language;
                        self.dateCreation= dateCreation;
                        self.mod= mod;
                        self.codeMod= codeMod; 
                        self.lastUserMod= lastUserMod;
                        self.dateLastModProp= dateLastModProp;
                        self.dateLastMod= dateLastMod;

                        //metodo della classe che verifica se mostrare la parte della modifica
                        self.isMod = ko.computed(function() {
                           return (self.mod=="Y");
                        });
                    }
                    
                    //viewmodel (gli passo come parametro un array di istanze della classe Snippet)
                    function SnippetViewModel(ByValSnippetArray) {
                        var self = this;
                        self.snippets = ko.observableArray(ByValSnippetArray);
                    }


                    function totalSnippet() {
                        if (window.XMLHttpRequest)
                        {// code for IE7+, Firefox, Chrome, Opera, Safari
                            xmlhttp = new XMLHttpRequest();
                        }
                        else
                        {// code for IE6, IE5
                            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
                        }

                        xmlhttp.open("POST", "<%=Util.BASE%>SearchSnippetServlet", true);
                        xmlhttp.onreadystatechange = alertContents;
                        xmlhttp.setRequestHeader("Content-Type",
                                "application/x-www-form-urlencoded");

                        var data = document.implementation.createDocument("", "total", null);
                        xmlhttp.send(data);
                    }
                    
                    function alertContents(){
                        if (xmlhttp.readyState === 4) {
                            if (xmlhttp.status === 200) {
                                var x = xmlhttp.responseXML.getElementsByTagName("dbSnippet")[0];
                                if (x.childNodes.length > 0) {
                                
                                    for (loop = 0; loop < x.childNodes.length; loop++) {
                                        var snippet = x.childNodes[loop];
                                        var idSnippet = snippet.getElementsByTagName("idSnippet")[0].childNodes[0] != undefined ? snippet.getElementsByTagName("idSnippet")[0].childNodes[0].nodeValue:"";
                                        var creator = snippet.getElementsByTagName("creator")[0].childNodes[0] != undefined ? snippet.getElementsByTagName("creator")[0].childNodes[0].nodeValue:"";
                                        var title = snippet.getElementsByTagName("title")[0].childNodes[0]!= undefined ? snippet.getElementsByTagName("title")[0].childNodes[0].nodeValue:"";
                                        var code = snippet.getElementsByTagName("code")[0].childNodes[0] != undefined ? snippet.getElementsByTagName("code")[0].childNodes[0].nodeValue:"";
                                        var language = snippet.getElementsByTagName("language")[0].childNodes[0] != undefined ? snippet.getElementsByTagName("language")[0].childNodes[0].nodeValue:"";
                                        var creationDate = snippet.getElementsByTagName("dateCreation")[0].childNodes[0] != undefined ? snippet.getElementsByTagName("dateCreation")[0].childNodes[0].nodeValue:"";
                                        var mod = snippet.getElementsByTagName("mod")[0].childNodes[0] != undefined ? snippet.getElementsByTagName("mod")[0].childNodes[0].nodeValue:"";
                                        var codeMod = snippet.getElementsByTagName("codeMod")[0].childNodes[0] != undefined ? snippet.getElementsByTagName("codeMod")[0].childNodes[0].nodeValue:"";
                                        var lastUserMod = snippet.getElementsByTagName("lastUserMod")[0].childNodes[0] != undefined ? snippet.getElementsByTagName("lastUserMod")[0].childNodes[0].nodeValue:"";
                                        var lastMod = snippet.getElementsByTagName("dateLastMod")[0].childNodes[0] != undefined ? snippet.getElementsByTagName("dateLastMod")[0].childNodes[0].nodeValue:"";
                                        var lastModProp = snippet.getElementsByTagName("dateLastModProp")[0].childNodes[0] != undefined ? snippet.getElementsByTagName("dateLastModProp")[0].childNodes[0].nodeValue:"";

                                        SnippetArray.push(new Snippet(creator,title,code,language,creationDate,mod,codeMod,lastUserMod,lastModProp,lastMod));
                                    }
                                    ko.applyBindings(new SnippetViewModel(SnippetArray));
                                    activateButtons();
                                }
                                else {
                                    document.getElementById('snippetList').style.display="none";
                                    var noSnippet = document.createElement("P");
                                    var noSnippetText = document.createTextNode("Non è ancora stato inserito uno snippet in Snippet share!");
                                    noSnippet.appendChild(noSnippetText);
                                    document.getElementById('content').appendChild(noSnippet);
                                }
                            }
                        }
                    }
                    
                    //funzione utilizzata per attivare il jquery del toggle dei bottoni mostra/nascondi
                    function activateButtons(){
                        $(".vedi").each(function(){
                            $(this).click(function(){
                                var btn=$(this);

                                if(!$(this).parents(".singleItem").find(".codeDiv").is(":visible")){
                                    btn.text("Nascondi");
                                }
                                $(this).parents(".singleItem").find(".codeDiv").fadeToggle(100,function(){
                                    if(!$(this).parents(".singleItem").find(".codeDiv").is(":visible")){
                                        btn.text("Vedi");
                                    }
                                });                       
                            });
                        });
                    }
                </script> 
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
