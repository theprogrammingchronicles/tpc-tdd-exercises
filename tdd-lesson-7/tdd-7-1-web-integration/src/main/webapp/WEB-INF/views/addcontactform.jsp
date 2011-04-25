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
  Vista que muestra el formulario de creación de contactos.
  Modelo:
      List<String, String> errors : Solo en caso de error en los parametros.

  Author: Pedro Ballesteros <pedro@theprogrammingchronicles.com>
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Wikiagenda - Añadir Contactos</title>
    <link rel="stylesheet" type="text/css" href="../styles/common.css">
</head>
<body>
    <h1>Nuevo contacto</h1>

    <form action="" method="post">
     <table>
        <tbody>
            <tr>
                <td class="${not empty errors.firstName ? 'formerror': ''}">Nombre:</td>
                <td><input name="firstName" value="${param.firstName}"></td>
                <c:if test="${not empty errors.firstName}">
                    <td><span class="formerror">${errors.firstName}</span></td>
                </c:if>
            </tr>
            <tr>
                <td>Apellidos:</td>
                <td><input name="surname" value="${param.surname}"></td>              
            </tr>
            <tr>
                <td class="${not empty errors.birthday ? 'formerror': ''}">Cumpleaños:</td>
                <td><input name="birthday" value="${param.birthday}"></td>
                <c:if test="${not empty errors.birthday}">
                    <td><span class="formerror">${errors.birthday}</span></td>
                </c:if>
            </tr>
            <tr>
                <td>Telefono:</td>
                <td><input name="phone" value="${param.phone}"></td>              
            </tr>
        </tbody>
    </table>

    <div class="banner">
        <button type="submit">Insertar</button>
        <a href="show.do"><button type="button">Cancelar</button></a>
    </div>
    </form>

    <%@include file="fotter.jspf"%>
</body>
</html>
