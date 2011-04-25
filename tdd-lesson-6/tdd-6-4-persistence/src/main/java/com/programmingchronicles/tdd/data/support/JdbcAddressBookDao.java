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

package com.programmingchronicles.tdd.data.support;

import com.programmingchronicles.tdd.data.AddressBookDao;
import com.programmingchronicles.tdd.data.SQLDaoException;
import com.programmingchronicles.tdd.domain.Contact;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

/**
 * Implementación de AddressBookDao basada en JDBC.
 *
 * <p>
 * La implementación depende a su vez un DataSource que se configurará
 * mediante IoC / DI</p>
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class JdbcAddressBookDao implements AddressBookDao {

    // La implementación requiere un datasource desde el
    // que se obtienen las conexiones.
    private DataSource ds;


    private static final String SQL_INSERT_CONTACT =
            "INSERT INTO CONTACTS(FIRSTNAME, SURNAME, BIRTHDAY, PHONE) "
            + "VALUES(?, ?, ?, ?)";

    private static final String SQL_SELECT_ALL =
            "SELECT ID, FIRSTNAME, SURNAME, BIRTHDAY, PHONE FROM CONTACTS";

    private static final String SQL_SELECT_CONTACT =
            "SELECT ID, FIRSTNAME, SURNAME, BIRTHDAY, PHONE " +
            "FROM CONTACTS " +
            "WHERE ID = ?";

    private static final String SQL_DELETE_CONTACT =
            "DELETE FROM CONTACTS " +
            "WHERE ID = ?";

    /**
     * Obtiene el DataSource configurado que se utilizará para la obtención
     * de conexiones a la base de datos.
     *
     * @return
     */
    public DataSource getDataSource() {
        return ds;
    }

    /**
     * Configura el DataSource utilizado para la obtención de conexiones a
     * la base de datos.
     *
     * @param ds
     */
    public void setDataSource(DataSource ds) {
        this.ds = ds;
    }
   
    @Override
    public String addContact(Contact contact) {
        try {
            Connection conn = ds.getConnection();
            try {
                PreparedStatement statement = conn.prepareStatement(SQL_INSERT_CONTACT, Statement.RETURN_GENERATED_KEYS);
                try {
                    statement.setString(1, contact.getFirstName());
                    statement.setString(2, contact.getSurname());
                    if (contact.getBirthday() != null) {
                        statement.setDate(3, new java.sql.Date(contact.getBirthday().getTime()));
                    } else {
                        statement.setDate(3, null);
                    }
                    statement.setString(4, contact.getPhone());
                    statement.execute();

                    // Obtiene la clave generada y la devuelve como string.
                    ResultSet rsKey = statement.getGeneratedKeys();
                    try {
                        rsKey.next();
                        return Long.toString(rsKey.getLong(1));
                    } finally {
                        rsKey.close();
                    }                    
                } finally {
                    statement.close();
                }
            } finally {
               conn.close();
            }
        } catch (SQLException ex) {
            // Las excepciones SQL serán principalmente fallos irrecuperables,
            // por lo que se deben propagar si el DAO no las soluciona.
            throw new SQLDaoException(ex);
        }
    }
   
    @Override
    public List<Contact> getAll() {
        try {
            Connection conn = ds.getConnection();
            try {
                Statement statement = conn.createStatement();
                try {
                    ResultSet rsContacts = statement.executeQuery(SQL_SELECT_ALL);
                    try {
                        List<Contact> contacts = new ArrayList();

                        while (rsContacts.next()) {
                            Contact contact = new Contact();

                            contact.setId(Long.toString(rsContacts.getInt("ID")));
                            contact.setFirstName(rsContacts.getString("FIRSTNAME"));
                            contact.setSurname(rsContacts.getString("SURNAME"));
                            contact.setBirthday(rsContacts.getDate("BIRTHDAY"));
                            contact.setPhone(rsContacts.getString("PHONE"));

                            contacts.add(contact);                        
                        }

                        return contacts;

                    } finally {
                        rsContacts.close();
                    }
                } finally {
                    statement.close();
                }
            } finally {
                conn.close();
            }
        } catch (SQLException ex) {
            // Las excepciones SQL serán principalmente fallos irrecuperables,
            // por lo que se deben propagar si el DAO no las soluciona.
            throw new SQLDaoException(ex);
        }
    }
  
    @Override
    public Contact getContact(String contactId) {

        // Si el id es invalido el DAO se comporta igual que si no existiera
        // el contacto, devolviendo null.
        long id;
        try {
            id= Long.parseLong(contactId);
        } catch(NumberFormatException ex) {
            return null;
        }
        
        try {
            Connection conn = ds.getConnection();
            try {
                PreparedStatement statement = conn.prepareStatement(SQL_SELECT_CONTACT);
                statement.setLong(1, id);
                try {
                    ResultSet rsContacts = statement.executeQuery();

                    try {
                        if(rsContacts.next()) {
                            Contact contact = new Contact();

                            contact.setId(Integer.toString(rsContacts.getInt("ID")));
                            contact.setFirstName(rsContacts.getString("FIRSTNAME"));
                            contact.setSurname(rsContacts.getString("SURNAME"));
                            contact.setBirthday(rsContacts.getDate("BIRTHDAY"));
                            contact.setPhone(rsContacts.getString("PHONE"));

                            return contact;
                        }

                        return null;

                    } finally {
                        rsContacts.close();
                    }
                } finally {
                    statement.close();
                }
            } finally {
                 conn.close();
            }
        } catch (SQLException ex) {
           // Las excepciones SQL serán principalmente fallos irrecuperables,
           // por lo que se deben propagar si el DAO no las soluciona.
           throw new SQLDaoException(ex);
        }
    }
  
    @Override
    public void deleteContact(String contactId) {
        // Si el id es invalido el DAO se comporta igual que si no existiera
        // el contacto.
        long id;
        try {
            id= Long.parseLong(contactId);
        } catch(NumberFormatException ex) {
            return;
        }
        
        try {
            Connection conn = ds.getConnection();
            try {
                PreparedStatement statement = conn.prepareStatement(SQL_DELETE_CONTACT);
                statement.setLong(1, id);
                try {
                    statement.execute();
                } finally {
                    statement.close();
                }
            } finally {
                 conn.close();
            }
        } catch (SQLException ex) {
            // Las excepciones SQL serán principalmente fallos irrecuperables,
            // por lo que se deben propagar si el DAO no las soluciona.
            throw new SQLDaoException(ex);
        }
    }
}
