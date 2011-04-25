/*
 * Copyright (C) 2010-2011, Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 *
 * This file is part of The Programming Chronicles Test-Driven Development
 * Exercises(http://theprogrammingchronicles.com/)
 *
 * This copyrighted material is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This material is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this material. This copy is available in LICENSE-GPL.txt
 * file. If not, see <http://www.gnu.org/licenses/>.
 */

package com.programmingchronicles.tdd.testing.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Fake de HttpServletResponse que añade metodos para crear parametros y que
 * no forman parte de la interfaz. Nos permiten evitar la verificación
 * por interación.
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class FakeHttpServletResponse implements HttpServletResponse {

    private String sendRedirectLocation;
    private String forwardLocation;

    // Para que el fake del rquestDispatcher pueda actualizar el resultado del
    // forward sobre el response cuando se invoca el forward.
    protected void setForwardLocation(String forwardLocation) {
        this.forwardLocation = forwardLocation;
    }

    /**
     * Devuelve el path pasado en un forward de un requestDispatcher, devolverá
     * null si el forward no se ha invocado, aunque se pidiera el dispatcher.
     *
     * @return path del forward de un dispatcher.
     */
    public String getForwardLocation() {
        return forwardLocation;
    }

    /**
     * El Fake ahora guarda los datos entregados en la respuesta para que
     * podamos verificarlos por estado.
     *
     * @param location
     * @throws IOException
     */
    @Override
    public void sendRedirect(String location) throws IOException {
        this.sendRedirectLocation = location;
    }

    /**
     * Devuelve el redirect devuelto en el response a través del sendRedirect.
     *
     * @return
     */
    public String getSendRedirectLocation() {
        return sendRedirectLocation;
    }


    @Override
    public void addCookie(Cookie cookie) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean containsHeader(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String encodeURL(String url) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String encodeRedirectURL(String url) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String encodeUrl(String url) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String encodeRedirectUrl(String url) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void sendError(int sc) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }



    @Override
    public void setDateHeader(String name, long date) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addDateHeader(String name, long date) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setHeader(String name, String value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addHeader(String name, String value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setIntHeader(String name, int value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addIntHeader(String name, int value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setStatus(int sc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setStatus(int sc, String sm) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getCharacterEncoding() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getContentType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setCharacterEncoding(String charset) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setContentLength(int len) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setContentType(String type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setBufferSize(int size) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getBufferSize() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void flushBuffer() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void resetBuffer() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isCommitted() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setLocale(Locale loc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Locale getLocale() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
