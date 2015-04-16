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
    </head>
    <body onload="totalSnippet()">
        <div id="container">
            <%@ include file="../WEB-INF/jspf/header.jspf" %>
            <div id="content">
            
                <p> PROVA -------------------</p>
                <!-- View -- TODO aggiungere corretto conteggio -->
                <div data-bind="foreach: snippet">
                    <div class="singleItem class">
                        <h2 id="title" data-bind="text: title"></h2>
                        <p>Scritto da <span data-bind="text: creator"></span></p>
                        <div class="information">
                                <p>Linguaggio: <span data-bind="text: language"></span></p>
                                <p>Data di creazione: <span data-bind="text: dateCreation"></span></p>
                                <p>Data di ultima modifica (generale): <span data-bind="text: dateLastMod"></span></p>
                                <p>Data di ultima modifica (proprietario): <span data-bind="text: dateLastModProp"></span></p>
                        </div>
                        <div id="cd1" class="information">
                                <p>Codice: </p>
                                <textarea rows="13" cols="100" name="code" id="textArea1" disabled="" data-bind="text: code"></textarea>
                                <p>Proposta di modifica:</p>
                                <textarea rows="13" cols="100" name="codeMod" id="textAreaMod3" disabled="" data-bind="text: codeMod"></textarea>
                                <p>di: <span data-bind="text: lastUserMod"></span></p>
                        </div>
                        <div class="actions">
                                <button type="button" value="Vedi" id="btn1" onclick="hide(cd1.id, btn1.id);">Vedi</button>
                        </div>
                <div class="clear"></div>
                </div>
                </div>

                
                <script>
                // ViewModel
                var snippetViewModel ={
                    snippet: ko.observableArray([
                        {creator: 'creator', title: 'Title', code: 'code', language :'language', dateCreation : 'dateCreation', mod: 'mod', codeMod: 'codeMod', userMod:'userMod', lastUserMod:'lastUserMod',dateLastModProp:'dateLastModProp',dateLastMod:'dateLastMod'},
                        {creator: 'creator', title: 'Title', code: 'code', language :'language', dateCreation : 'dateCreation', mod: 'mod', userMod:'userMod', lastUserMod:'lastUserMod',dateLastModProp:'dateLastModProp',dateLastMod:'dateLastMod'}  
                    ])
                }
                ko.applyBindings(snippetViewModel);
                </script>
                <p> FINE PROVA -------------------</p>
                
                <div id="result"></div>            
                
                <script>

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

                 function alertContents() {
                    if (xmlhttp.readyState === 4) {
                        if (xmlhttp.status === 200) {
                            var x = xmlhttp.responseXML.getElementsByTagName("dbSnippet")[0];
                            document.getElementById("result").innerHTML = "";
                            if (x.childNodes.length > 0) {
                                
                                for (loop = 0; loop < x.childNodes.length; loop++) {
                                    var snippet = x.childNodes[loop];
                                    var idSnippet = snippet.getElementsByTagName("idSnippet")[0];
                                    var creator = snippet.getElementsByTagName("creator")[0];
                                    var title = snippet.getElementsByTagName("title")[0];
                                    var code = snippet.getElementsByTagName("code")[0];
                                    var language = snippet.getElementsByTagName("language")[0];
                                    var creationDate = snippet.getElementsByTagName("dateCreation")[0];
                                    var mod = snippet.getElementsByTagName("mod")[0];
                                    var codeMod = snippet.getElementsByTagName("codeMod")[0];
                                    var lastUserMod = snippet.getElementsByTagName("lastUserMod")[0];
                                    var lastMod = snippet.getElementsByTagName("dateLastMod")[0];
                                    var lastModProp = snippet.getElementsByTagName("dateLastModProp")[0];
                                    var cont = document.createElement("DIV");
                                    cont.setAttribute("class", "singleItem class" + loop % 2);
                                    //titolo snippet
                                    var titolo = document.createElement("H2"); //input element
                                    titolo.setAttribute("id", "title");
                                    titolo.innerHTML = title.childNodes[0].nodeValue;
                                    //autore snippet
                                    var scrittoDa = document.createElement("P");
                                    var scrittoDaText = document.createTextNode("Scritto da " + creator.childNodes[0].nodeValue);
                                    scrittoDa.appendChild(scrittoDaText);
                                    //linguaggio snippet
                                    var linguaggio = document.createElement("P");
                                    var linguaggioText = document.createTextNode("Linguaggio: " + language.childNodes[0].nodeValue);
                                    linguaggio.appendChild(linguaggioText);
                                    //data creazione
                                    var dataCreazione = document.createElement("P");
                                    var dataCreazioneText = document.createTextNode("Data di creazione: " + creationDate.childNodes[0].nodeValue);
                                    dataCreazione.appendChild(dataCreazioneText);
                                    //data ultima modifica generale
                                    var dataLastMod = document.createElement("P");
                                    var dataLastModText = document.createTextNode("Data di ultima modifica (generale): " + lastMod.childNodes[0].nodeValue);
                                    dataLastMod.appendChild(dataLastModText);
                                    //data ultima modifica proprietario
                                    var dataLastModProp = document.createElement("P");
                                    var dataLastModPropText = document.createTextNode("Data di ultima modifica (proprietario): " + lastModProp.childNodes[0].nodeValue);
                                    dataLastModProp.appendChild(dataLastModPropText);
                                    //codice snippet
                                    var codeP = document.createElement("P");
                                    var codePText = document.createTextNode("Codice: ");
                                    codeP.appendChild(codePText);
                                    var codice = document.createElement("TEXTAREA");
                                    codice.setAttribute("rows", "13");
                                    codice.setAttribute("cols", "100");
                                    codice.setAttribute("name", "code");
                                    codice.setAttribute("id", "textArea" + loop);
                                    codice.disabled = true;
                                    codice.innerHTML = code.childNodes[0].nodeValue;

                                    if ((mod.childNodes[0].nodeValue.toString()) === "Y") {
                                        //codie proposta modifica
                                        var codeModP = document.createElement("P");
                                        var codeModPText = document.createTextNode("Proposta di modifica:");
                                        codeModP.appendChild(codeModPText);
                                        var codiceMod = document.createElement("TEXTAREA");
                                        codiceMod.setAttribute("rows", "13");
                                        codiceMod.setAttribute("cols", "100");
                                        codiceMod.setAttribute("name", "codeMod");
                                        codiceMod.setAttribute("id", "textAreaMod" + loop);
                                        codiceMod.disabled = true;
                                        codiceMod.innerHTML = codeMod.childNodes[0] != undefined ? codeMod.childNodes[0].nodeValue : "";
                                        //autore modifica
                                        var autoreModP = document.createElement("P");
                                        var autoreModPText = document.createTextNode("di: " + lastUserMod.childNodes[0].nodeValue);
                                        autoreModP.appendChild(autoreModPText);
                                    }


                                    //bottone per visualizzare il codice dello snippet
                                    var btnView = document.createElement("BUTTON");
                                    var btnViewText = document.createTextNode("Vedi");
                                    btnView.setAttribute("type", "button");
                                    btnView.setAttribute("value", "Vedi");
                                    btnView.setAttribute("id", "btn" + loop);
                                    btnView.setAttribute("onclick", "hide(cd" + loop + ".id, btn" + loop + ".id);");
                                    btnView.appendChild(btnViewText);

                                    //Struttura visualizzazione snippet
                                    cont.appendChild(titolo);
                                    cont.appendChild(scrittoDa);
                                    var info = document.createElement("DIV");
                                    info.setAttribute("class", "information");
                                    info.appendChild(linguaggio);
                                    info.appendChild(dataCreazione);
                                    info.appendChild(dataLastMod);
                                    info.appendChild(dataLastModProp);
                                    cont.appendChild(info);
                                    var cd = document.createElement("DIV");
                                    cd.setAttribute("id", "cd" + loop);
                                    cd.setAttribute("class", "information");
                                    cd.style.display = 'none';
                                    cd.appendChild(codeP);
                                    cd.appendChild(codice);

                                    if ((mod.childNodes[0].nodeValue.toString()) === "Y") {
                                        cd.appendChild(codeModP);
                                        cd.appendChild(codiceMod);
                                        cd.appendChild(autoreModP);
                                    }
                                    cont.appendChild(cd);


                                    var actions = document.createElement("DIV");
                                    actions.setAttribute("class", "actions");
                                    actions.appendChild(btnView);
                                    cont.appendChild(actions);
                                    var clear = document.createElement("DIV");
                                    clear.setAttribute("class", "clear");
                                    cont.appendChild(clear);
                                    document.getElementById('result').appendChild(cont);
                                }

                            }
                            else {
                                var noSnippet = document.createElement("P");
                                var noSnippetText = document.createTextNode("Non è ancora stato inserito uno snippet in Snippet share!");
                                noSnippet.appendChild(noSnippetText);
                                document.getElementById('result').appendChild(noSnippet);
                            }
                        }
                    }

                }

                function hide(idCnt, idBtn) {
                    var div = document.getElementById(idCnt);

                    if (div.style.display !== 'none') {
                        div.style.display = 'none';
                        var btn = document.getElementById(idBtn);
                        btn.value = "Vedi";
                        btn.textContent = "Vedi";
                    }
                    else {
                        div.style.display = 'block';
                        var btn = document.getElementById(idBtn);
                        btn.value = "Riduci";
                        btn.textContent = "Riduci";
                    }


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
