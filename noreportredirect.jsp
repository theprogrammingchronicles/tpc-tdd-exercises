<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%--
    Envía una redirección al navegador para abrir la página de informeno
    encontrado con el path correcto.

    Author     : Pedro Ballesteros <pedro@theprogrammingchronicles.com>
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page session="false" %>

<%
    // La redireccion se hace relativa al contexto en el que se despliega la aplicacion.
    String redirect = application.getContextPath() + "/noreport.html";
    response.sendRedirect(redirect);
%>

