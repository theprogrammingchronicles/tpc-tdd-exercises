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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.spi.DirStateFactory.Result;
import javax.sql.DataSource;

/**
 * Implementación de AddressBookDao basada en JDBC.
 *
 * <p>
 * La implementación depende a su vez un DataSource que se configurará
 * mediante IoC / DI.</p>
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class JdbcAddressBookDao implements AddressBookDao {

    private DataSource ds;

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

                PreparedStatement statement = conn.prepareStatement(
                        "INSERT INTO CONTACTS(FIRSTNAME, SURNAME, BIRTHDAY, PHONE) "
                        + "VALUES(?, ?, ?, ?)");
                try {
                    statement.setString(1, contact.getFirstName());
                    statement.setString(2, contact.getSurname());
                    statement.setDate(3, null);
                    statement.setString(4, contact.getPhone());

                    statement.execute();

                } finally {
                    statement.close();
                }
            } finally {
                conn.close();
            }


        } catch (SQLException ex) {
            throw new SQLDaoException(ex);
        }
        return null;

    }

    @Override
    public void deleteContact(String id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Contact> getAll() {
        return null;
    }

    @Override
    public Contact getContact(String contactId) {
        try {
            Connection conn = ds.getConnection();
            try {
                PreparedStatement statement = conn.prepareStatement(
                        "SELECT ID, FIRSTNAME, SURNAME, BIRTHDAY, PHONE "
                        + "FROM CONTACTS "
                        + "WHERE ID = ?");
                try {
                    statement.setString(1, contactId);

                    ResultSet resultset = statement.executeQuery();
                    try {
                        if (resultset.next()) {
                            Contact contact = new Contact();
                            contact.setFirstName(resultset.getString("FIRSTNAME"));
                            return contact;
                        }
                    } finally {
                        resultset.close();
                    }

                } finally {
                    statement.close();
                }
            } finally {
                conn.close();
            }

        } catch (SQLException ex) {
            throw new SQLDaoException(ex);
        }
        return null;
    }
}
