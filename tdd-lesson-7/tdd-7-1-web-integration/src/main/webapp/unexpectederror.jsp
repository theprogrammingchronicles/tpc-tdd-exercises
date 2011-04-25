<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%--
 ~ Copyright (C) 2010-2011, Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 ~
 ~ This file is part of The Programming Chronicles Test-Driven Development
 ~ Exercises(http://theprogrammingchronicles.com/)
 ~
 ~ This copyrighted material is free software: you can redistribute it
 ~ and/or modify it under the terms of the GNU General Public License as
 ~ published by the Free Software Foundation, either version 3 of the
 ~ License, or (at your option) any later version.
 ~
 ~ This material is distributed in the hope that it will be useful,
 ~ but WITHOUT ANY WARRANTY; without even the implied warranty of
 ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 ~ GNU General Public License for more details.
 ~
 ~ You should have received a copy of the GNU General Public License
 ~ along with this material. This copy is available in LICENSE-GPL.txt
 ~ file. If not, see <http://www.gnu.org/licenses/>.
--%>

<%-- 
    Pagina para errores no controlados en los servlets, como el error 500
    devuelto por el contenedor al recibir un ServletException.
    NOTA: Los servlets no tiene porque controlar todos los errores
          ya que muchas veces la única solución es informar al usuario,
          Por ejemplo un error de base de datos normalmente es un fallo
          de la conexión, lo que indica que hay un problema en el despliegue.

    Author: Pedro Ballesteros <pedro@theprogrammingchronicles.com>
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- Se obtiene el context path para entregar urls correctas al navegador --%>
<c:url var="contextPath" value="/"/>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Wikiagenda - Fallo de Wikiagenda.</title>
    <link rel="stylesheet" type="text/css" href="${contextPath}styles/common.css">
    <link rel="stylesheet" type="text/css" href="styles/common.css">

    <script type="text/javascript">
        function showDetails() {
            var details = document.getElementById("details");
            if(details.style.display == "") {
               details.style.display = "none";
            } else {
               details.style.display = "";
            }
        }
    </script>

    <style type="text/css">
        #details {
            position: fixed;
            top: 20ex;
            bottom: 10ex;
            right: 4em;
            left: 4em;
            border: 2px solid silver;
            overflow: auto;
            font-size: 80%;
        }       
    </style>

</head>
<body>

    <h1>Fallo de Wikiagenda.</h1>
    <div>
        Disculpe las molestias, pero ha producido un fallo inesperado en Wikiagenda.<br/>
        Puede volver a la página inicial desde <a href="${contextPath}homeredirect.jsp">aquí</a>
    </div>
    <div>&nbsp;</div>
    <div class="actions" onclick="showDetails();">Ver Detalles</div>
   
    <div id="details" style="display: none;">
        <b>Status Code: </b> ${pageContext.errorData.statusCode} <br>&nbsp;<br>
        <b>Request URI: </b> ${pageContext.errorData.requestURI} <br>&nbsp;<br>
        <b>Servlet Name: </b> ${pageContext.errorData.servletName} <br>&nbsp;<br>
        <b>Exception: </b>${pageContext.errorData.throwable.class} <br>&nbsp;<br>
        <b>Message: </b>${pageContext.errorData.throwable.message} <br>&nbsp;<br>
        <b>Stack Trace: </b><br>
        <c:forEach var="trace" items="${pageContext.errorData.throwable.stackTrace}">
            ${trace}<br>
        </c:forEach>
        <br>
    </div>

    <%@include file="/WEB-INF/views/fotter.jspf"%>
</body>
</html>
