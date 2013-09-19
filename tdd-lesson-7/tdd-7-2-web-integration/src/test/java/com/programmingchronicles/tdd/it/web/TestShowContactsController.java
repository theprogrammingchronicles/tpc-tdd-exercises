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

package com.programmingchronicles.tdd.it.web;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class TestShowContactsController {

    public TestShowContactsController() {
    }

    @Test
    public void testShowContacts() throws Exception {
        WebClient webClient = new WebClient();
        HtmlPage page = webClient.getPage("http://localhost:8080/tdd-7-1-web-integration-1.0/contactlist/show.do");

        assertEquals("Wikiagenda", page.getTitleText());

        String pageAsXml = page.asXml();
        assertTrue(pageAsXml.contains("<table class=\"contacts\">"));

        String pageAsText = page.asText();
        assertTrue(pageAsText.contains("Bienvenido a Wikiagenda"));

        // page.getElementById("submit").click();
    }
}
