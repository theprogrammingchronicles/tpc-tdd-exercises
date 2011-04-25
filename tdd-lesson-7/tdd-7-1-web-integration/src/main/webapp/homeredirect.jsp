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
    Envía una redirección al navegador para abrir la página principal
    con el path correcto y simplificar el uso de path relativos.

    Author: Pedro Ballesteros <pedro@theprogrammingchronicles.com>
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page session="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- Las paginas listadas en el "welcome-file-list" de web.xml es mejor
     redirigirlas a una url correcta para facilitar el uso de path
     relativos --%>
<c:redirect url="/contactlist/show.do"/>
