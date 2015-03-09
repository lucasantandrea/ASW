<%-- 
    Esame ASW 2014-2015
    Autori: Luca Santandrea, Matteo Mariani, Antonio Leo Folliero, Francesco Degli Angeli
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
        <title>Cerca codice | Snippet share</title>
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
                <form class="formClass" action="" >

                    <!-- New snippet -->
                    <p><label for="title">Titolo: </label><input type="text" name="title" id="title"></p>
                    <p><label for="author">Autore: </label><input type="text" name="author" id="author"></p>
                    <p><label for="language">Linguaggio: </label><select name="language" id="language">
                            <option value="0" >---</option>

                            <option name="java" >Java</option>
                            <option name="javascript"  >JavaScript</option>
                            <option name="c++" >C++</option>
                            <option name="c#"  >C#</option>
                            <option name="php"  >PHP</option>                                    
                        </select></p>   
                    <p><label for="order">Ordina per: </label><select name="order" id="order">
                            <option value="0" >---</option>
                            <option name="creation" value="1">Data creazione</option>
                            <option name="ownerUpdate" value="2">Ultima modifica proprietario</option>      
                            <option name="modification" value="3">Ultima modifica generale</option>  
                        </select></p>
                    <input type="button" class="submit" value="Cerca" onclick="search()">

                </form>
                <div  id="result">


                </div>

            </div>   
            <div id="sidebar">
                <%@ include file="../WEB-INF/jspf/sidebar.jspf" %>
            </div>            
            <div id="footer">
                <%@ include file="../WEB-INF/jspf/footer.jspf" %>
            </div>   
          <script>
                /*
                 * Funzione che permette di inviare l'xml coi parametri di ricerca alla servlet
                 * e di visualizzare l'xml dei risultati restituito dalla servlet 
                 */
                function search() {
                    if (window.XMLHttpRequest)
                    {// code for IE7+, Firefox, Chrome, Opera, Safari
                        xmlhttp = new XMLHttpRequest();
                    }
                    else
                    {// code for IE6, IE5
                        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
                    }
                    var value1 = document.getElementById("title").value;
                    var value2 = document.getElementById("author").value;
                    var sel = document.getElementById('language');
                    var value3 = (sel.options[sel.selectedIndex]).value;
                    sel = document.getElementById('order');
                    var value4 = (sel.options[sel.selectedIndex]).value;
                    xmlhttp.open("POST", "<%=Util.BASE%>SearchSnippetServlet", true);
                    xmlhttp.onreadystatechange = alertContents;
                    xmlhttp.setRequestHeader("Content-Type",
                            "application/x-www-form-urlencoded");
                    var data = document.implementation.createDocument("", "find", null);
                    var datad = document.createElement("title");
                    var datai = document.createTextNode(value1);
                    data.documentElement.appendChild(datad);
                    datad.appendChild(datai);
                    data.documentElement.appendChild(datad);
                    var datad = document.createElement("author");
                    var datai = document.createTextNode(value2);
                    data.documentElement.appendChild(datad);
                    datad.appendChild(datai);
                    var datad = document.createElement("language");
                    var datai = document.createTextNode(value3);
                    data.documentElement.appendChild(datad);
                    datad.appendChild(datai);
                    var datad = document.createElement("order");
                    var datai = document.createTextNode(value4);
                    data.documentElement.appendChild(datad);
                    datad.appendChild(datai);
                    xmlhttp.send(data);
                }

                function alertContents() {
                    if (xmlhttp.readyState === 4) {
                        if (xmlhttp.status === 200) {
                            var x = xmlhttp.responseXML.getElementsByTagName("dbSnippet")[0];
                            console.log(x);
                            document.getElementById("result").innerHTML = "";
                            if (x.childNodes.length > 0) {
                                var numSnippet = document.createElement("P");
                                var numSnippetText = document.createTextNode(x.childNodes.length + " risultati:");
                                numSnippet.appendChild(numSnippetText);
                                document.getElementById('result').appendChild(numSnippet);
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
                                        codiceMod.innerHTML = codeMod.childNodes[0].nodeValue;
                                        //autore modifica
                                        var autoreModP = document.createElement("P");
                                        var autoreModPText = document.createTextNode("di: " + lastUserMod.childNodes[0].nodeValue);
                                        autoreModP.appendChild(autoreModPText);
                                    }

                                    //form modifica snippet
                                    var formMod = document.createElement("form");
                                    formMod.setAttribute("name", "submitModifyForm" + loop);
                                    formMod.setAttribute("action", "<%= Util.BASE%>EditServlet");
                                    formMod.setAttribute("method", "POST");
                                    var inputIdSnippet2 = document.createElement("INPUT");
                                    inputIdSnippet2.setAttribute("type", "hidden");
                                    inputIdSnippet2.setAttribute("name", "id");
                                    inputIdSnippet2.setAttribute("value", idSnippet.childNodes[0].nodeValue);
                                    var inputUser = document.createElement("INPUT");
                                    inputUser.setAttribute("type", "hidden");
                                    inputUser.setAttribute("name", "user");
                                    inputUser.setAttribute("value", "<%=session.getAttribute("user")%>");
                                    var aM = document.createElement("A");
                                    aM.href = "javascript:document.submitModifyForm" + loop + ".submit()";
                                    aM.text = "Modifica";

                                    var btnMod = document.createElement("BUTTON");
                                    var btnModText = document.createTextNode("Modifica");
                                    btnMod.setAttribute("type", "submit");
                                    btnMod.setAttribute("value", "Modifica");
                                    btnMod.setAttribute("id", "btnMod" + loop);
                                    btnMod.appendChild(btnModText);

                                    formMod.appendChild(inputIdSnippet2);
                                    formMod.appendChild(inputUser);
                                    formMod.appendChild(btnMod);

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
                                    actions.appendChild(formMod);
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
                                var noSnippetText = document.createTextNode("Nessun risultato trovato.");
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
    </body>
</html>