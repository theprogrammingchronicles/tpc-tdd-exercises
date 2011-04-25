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
    Vista que muestra la lista de contactos completa.
    Modelo:
        List<Contacts> contacts

    Author: Pedro Ballesteros <pedro@theprogrammingchronicles.com>
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Wikiagenda</title>
        <link rel="stylesheet" type="text/css" href="../styles/common.css">
    </head>
    <body>
        <h1>Bienvenido a Wikiagenda</h1>

        <div class="actions">
            <a  href="addcontact.do">Nuevo contacto</a>
        </div>

        <table class="contacts">
            <col class="firstname">
            <col class="null">
            <col class="birthday">
            <thead>
                <tr class="contacts">
                    <td class="contacts headers">Nombre</td>
                    <td class="contacts headers">Apellidos</td>
                    <td class="contacts headers">Cumpleaños</td>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="contact" items="${contacts}">
                    <tr class="contactrow">
                        <td class="contacts">${contact.firstName}</td>
                        <td class="contacts">${contact.surname}</td>
                        <td class="contacts"><fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${contact.birthday}"/></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <%@include file="fotter.jspf"%>        
    </body>
</html>
