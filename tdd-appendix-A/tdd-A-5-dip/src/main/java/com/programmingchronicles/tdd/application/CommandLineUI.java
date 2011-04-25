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

package com.programmingchronicles.tdd.application;

import com.programmingchronicles.tdd.addressbook.GlobalAddressBook;
import com.programmingchronicles.tdd.addressbook.support.DbAddressBook;
import com.programmingchronicles.tdd.addressbook.support.IncrementIdGenerator;
import com.programmingchronicles.tdd.addressbook.support.MemAddressBook;
import com.programmingchronicles.tdd.addressbook.support.RandomIdGenerator;
import com.programmingchronicles.tdd.domain.Contact;
import java.util.List;

/**
 * Implementación de una interfaz CLI (Command Line Interfaz) para
 * la aplicación de la Agenda Global.
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class CommandLineUI {
    
    GlobalAddressBook addressBookL2Cache;
    GlobalAddressBook addressBookL1Cache;
    GlobalAddressBook addressBook;

    public CommandLineUI() {
        // Creamos un addressbook basado en memoria para la caché
        MemAddressBook memAddressBook = new MemAddressBook();
        // Se configura con un generador de ids aleatorio.
        memAddressBook.setIdGenerator(new RandomIdGenerator());

        addressBookL2Cache = memAddressBook;

        // Creamos un addressbook de nivel 1 basado en memoria
        memAddressBook = new MemAddressBook();
        // Estamos usando la misma implementación, pero ahora decidimos
        // usar un generador no aletario, el cliente decide que
        // implementación de las dependencias interesa más.
        memAddressBook.setIdGenerator(new IncrementIdGenerator());

        addressBookL1Cache = memAddressBook;

        // Creamos un addressbook basado en base de datos, usamos
        // exactamente la misma interfaz, pero con otra implementación que
        // no requiere configurar un IdGenerator.
        addressBook = new DbAddressBook();
    }


    // mvn exec:java -Dexec.mainClass=com.programmingchronicles.tdd.application.CommandLineUI -Dexec.args="add hola"
    // java com.programmingchronicles.tdd.application.CommandLineUI add hola
    public static void main(String [] args) {
        CommandLineUI cli = new CommandLineUI();

        if(args.length == 2 && "add".equals(args[0]) && args[1] != null && args[1].length() > 0) {
            cli.add(new Contact(args[1]));
            return;
        } else if(args.length == 1 && "read".equals(args[0])) {
            cli.printAll();
            return;
        } 
        cli.printUsage();
    }

    private void add(Contact contact) {
        addressBookL2Cache.addContact(contact);
        addressBookL1Cache.addContact(contact);
   //     addressBook.addContact(contact);
        System.out.println(contact.getName() + " added." );
        System.out.println();
    }

    private void printAll() {
        List<Contact> contacts = addressBookL1Cache.getAll();
        if(contacts.size() == 0) {
          System.out.println("Wikiagenda is Empty" );
          System.out.println();
          return;
        }

        System.out.println("Wikiagenda:");
        System.out.println();
        
        for(Contact contact: contacts) {
            System.out.println(contact.getName());
        }
        System.out.println();
    }

    private void printUsage() {
        System.out.println("Usage:");
        System.out.println();
        System.out.println("CommandLineUI add name");
        System.out.println("CommandLineUI read");
    }
}
