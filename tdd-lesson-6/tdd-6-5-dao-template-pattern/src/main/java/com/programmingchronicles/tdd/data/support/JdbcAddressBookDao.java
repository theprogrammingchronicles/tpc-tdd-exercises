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
import com.programmingchronicles.tdd.data.template.ColumnMapper;
import com.programmingchronicles.tdd.data.template.JdbcDaoTemplate;
import com.programmingchronicles.tdd.data.template.ParameterMapper;
import com.programmingchronicles.tdd.domain.Contact;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;

/**
 * Implementación de AddressBookDao basada en JDBC.
 *
 * <p><b>Refactorización</b><br/>
 * Se realiza una refactorización para eliminar todo el código repetitivo
 * de manipulación de objetos de acceso a base de datos y control del
 * cierre de recursos (se disminuyen unas 50 líneas de código).</p>
 *
 * <p>El datasource ahora se utiliza a través del template implementado, pero
 * el DAO se sigue usando exactamente igual a través de la propiedad
 * <i>datasource</i> proporcionada por la abstracción {@link #getDataSource() }
 * & {@link #setDataSource(javax.sql.DataSource) }.
 *
 * <p>El código repetido se extrae en una clase reutilizable que utiliza el
 * patrón de diseño <b>Template</b>. También se centraliza la conversión
 * de claves generadas por la base de datos en claves del objeto del dominio.</p>
 *
 * <p>Los test de los DAOs ahora se pueden despreocupar completamente de la
 * gestión de recursos, que ahora está enteramente probada por test unitarios,
 * verdaderamente aislados de la base de datos.<p>
 *
 * <p><b>Nota:</b><br/>
 * Existen librerías que ya proporcionan este tipo de templates para JDBC,
 * como <i>JDBC Spring Templates</i>.</p>
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class JdbcAddressBookDao implements AddressBookDao {

    // El datasource ahora se utiliza a través del template implementado,
    // pero el DAO se sigue usando exactamente igual a través de la propiedad
    // datasource (getDataSource & setDataSource).
    private JdbcDaoTemplate template;

    // Sentencias SQL utilizadas por el DAO.
    private static final String SQL_INSERT_CONTACT =
            "INSERT INTO CONTACTS(FIRSTNAME, SURNAME, BIRTHDAY, PHONE) "
            + "VALUES(?, ?, ?, ?)";
    private static final String SQL_SELECT_ALL =
            "SELECT ID, FIRSTNAME, SURNAME, BIRTHDAY, PHONE FROM CONTACTS";
    private static final String SQL_SELECT_CONTACT =
            "SELECT ID, FIRSTNAME, SURNAME, BIRTHDAY, PHONE "
            + "FROM CONTACTS "
            + "WHERE ID = ?";
    private static final String SQL_DELETE_CONTACT =
            "DELETE FROM CONTACTS "
            + "WHERE ID = ?";

    /**
     * Obtiene el DataSource configurado que se utilizará para la obtención
     * de conexiones a la base de datos.
     *
     * @return
     */
    public DataSource getDataSource() {
        return template.getDatasource();
    }

    /**
     * Configura el DataSource utilizado para la obtención de conexiones a
     * la base de datos.
     *
     * @param ds
     */
    public void setDataSource(DataSource ds) {
        this.template = new JdbcDaoTemplate(ds);
    }

    @Override
    public String addContact(final Contact contact) {
        List<Number> keys;
        keys = template.insert(SQL_INSERT_CONTACT,
            new ParameterMapper() {
                @Override
                public void mapParameters(PreparedStatement statement) throws SQLException {
                    statement.setString(1, contact.getFirstName());
                    statement.setString(2, contact.getSurname());
                    if (contact.getBirthday() != null) {
                        statement.setDate(3, new java.sql.Date(contact.getBirthday().getTime()));
                    } else {
                        statement.setDate(3, null);
                    }
                    statement.setString(4, contact.getPhone());
                }
            });
        // Como sólo se inserta una fila, sólo debe haber una clave.
        return keyToId(keys.get(0).longValue());
    }

    @Override
    public List<Contact> getAll() {
        List<Contact> result;
        result = template.query(SQL_SELECT_ALL,
            new ColumnMapper<Contact>() {
                @Override
                public Contact mapColumns(ResultSet rsContacts) throws SQLException {
                    Contact contact = new Contact();

                    contact.setId(keyToId(rsContacts.getLong("ID")));
                    contact.setFirstName(rsContacts.getString("FIRSTNAME"));
                    contact.setSurname(rsContacts.getString("SURNAME"));
                    contact.setBirthday(rsContacts.getDate("BIRTHDAY"));
                    contact.setPhone(rsContacts.getString("PHONE"));

                    return contact;
                }
            });
        return result;
    }

    @Override
    public Contact getContact(String contactId) {

        // Si el id es invalido el DAO se comporta igual que si no existiera
        // el contacto, devolviendo null.
        final long key;
        try {
            key = idToKey(contactId);
        } catch(NumberFormatException ex) {
            return null;
        }

        List<Contact> contacts;
        contacts = template.query(SQL_SELECT_CONTACT,
            new ParameterMapper() {
                @Override
                public void mapParameters(PreparedStatement statement) throws SQLException {
                    statement.setLong(1, key);
                }
            },
            new ColumnMapper<Contact>() {
                @Override
                public Contact mapColumns(ResultSet rsContacts) throws SQLException {
                    Contact contact = new Contact();

                    contact.setId(keyToId(rsContacts.getLong("ID")));
                    contact.setFirstName(rsContacts.getString("FIRSTNAME"));
                    contact.setSurname(rsContacts.getString("SURNAME"));
                    contact.setBirthday(rsContacts.getDate("BIRTHDAY"));
                    contact.setPhone(rsContacts.getString("PHONE"));

                    return contact;
                }
            });

        // Si el contacto no existe se de debe devolver null
        if (contacts.isEmpty()) {
            return null;
        } else {
            // Se devuelve solo el primer contacto ya que la SQL solo
            // busca un contacto por id.
            return contacts.get(0);
        }
    }

    @Override
    public void deleteContact(String contactId) {
        // Si el id es invalido el DAO se comporta igual que si no existiera.
        final long key;
        try {
            key = idToKey(contactId);
        } catch(NumberFormatException ex) {
            return;
        }

        template.update(SQL_DELETE_CONTACT,
            new ParameterMapper() {
                @Override
                public void mapParameters(PreparedStatement statement) throws SQLException {
                    statement.setLong(1, key);
                }
            });
    }

    private String keyToId(long key) {
        return Long.toString(key);
    }

    private long idToKey(String id) throws NumberFormatException {
        return Long.parseLong(id);
    }
}
