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

package com.programmingchronicles.tdd.it.utils;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import javax.sql.DataSource;
import static org.mockito.Mockito.*;

/**
 * Clase que proporciona utilidades de soporte a los tests de
 *
 * <p><b>Refactorización:</b>Se ha extraído desde la clase de test para
 * hacer el código más legible
 *
 * En este caso ya se trabaja con una base de datos física, que podría estar
 * incluso en una máquina remota.
 *
 * Para el ejemplo se sigue usando un driver de base de datos embebida, pero
 * que accede a una base de datos persistente en disco. Para conectar con
 * un sistema de gestión de base de datos tan sólo habría que cambiar el
 * driver y la url de la conexión (MySQL, Derby Server, Oracle, etc.).
 *
 * Para hacer el ejercicio fácilmente reproducible también se crea la base
 * de datos y sus tablas si se detecta que aún no existe. En un entorno
 * real se podría contar con una base de datos ya existente, o lanzar
 * los script de creación de forma externa.
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class DatabaseTestSupport {    

    // Para el ejemplo se utiliza un driver de base de datos embebida, pero
    // se podría cambiar por un driver de cliente para el acceso al sistema
    // de gestión de base de datos, ya sea MySQL, Derby, Oracle, etc.
    private static final String JDBC_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";

    // Los test de integración accederán a una base de datos física de
    // desarrollo o de tests. En este ejemplo la base de datos está embebida.
    private static final String URL_CONNECTION = "jdbc:derby:dev-databases";

    static {
        // Inicialización del driver de la base de datos.
        initJdbcDriver();
    }

    public static Connection newConnection() throws SQLException {
        try {
           return DriverManager.getConnection(URL_CONNECTION);
        } catch(SQLException ex) {
            System.out.println(ex.getCause());
            System.out.println(ex.getClass());
            System.out.println(ex.getErrorCode());
            System.out.println(ex.getSQLState());
            throw ex;
        }
           /*   Connection conn = newConnection();
        try {
           final String SQL_CREATE_CONTACT_TABLE =
            "CREATE TABLE CONTACTS ("
            + "ID INT NOT NULL GENERATED ALWAYS AS IDENTITY, "
            + "FIRSTNAME VARCHAR(255), "
            + "SURNAME VARCHAR(255), "
            + "BIRTHDAY DATE, "
            + "PHONE VARCHAR(255))"; *

            Statement stm = conn.createStatement();
            try {
                stm.execute(SQL_CREATE_CONTACT_TABLE);
            } finally {
                stm.close();
            }
        } finally {
            conn.close();
        } */
    }

    /**
     * Crea un stub del datasource que devuelve siempre un wrapper de la conexión
     * entregada, que ignora las transacciones y el cierre de conexión, para que
     * los tests puedan realizar el rollback de las modificaciones.
     *
     * <p>
     * El wrapper de la conexión también permitirá realizar comprobaciones
     * de que el DAO las cierra correctamente. Si se usa un DataSource basado
     * en un pool de conexiones, el garbage collection no las podrá cerrar
     * nunca, lo que puede agotar las conexiones del pool.</p>
     *
     * @see ConnectionWrapper
     *
     * @param connection
     * @return
     * @throws SQLException
     */
    public static DataSource newSingleNonTransactionalDataSource(Connection connection) throws SQLException {

        // Crea un wrapper de la conexión para ignorar transacciones.
        final Connection nonTransactionalConnection = new ConnectionWrapper(connection) {
            @Override
            public void close() throws SQLException {
            }

            @Override
            public void commit() throws SQLException {
            }

            @Override
            public void rollback() throws SQLException {
            }

            @Override
            public void setAutoCommit(boolean autoCommit) throws SQLException {
            }
        };

        // Crea un stub de un datasource que devuelve el wrapper de la conexión.
        DataSource mockDatasource = mock(DataSource.class);

        // El datasource devuelve el connection spy.
        when(mockDatasource.getConnection()).thenReturn(nonTransactionalConnection);
        when(mockDatasource.getConnection(anyString(), anyString()))
             .thenReturn(nonTransactionalConnection);

        return mockDatasource;
    }

    public static void createTables() throws SQLException {
        Connection conn = newConnection();
        try {
            Statement stm = conn.createStatement();
            try {
                stm.execute(SQL_CREATE_CONTACT_TABLE);
            } finally {
                stm.close();
    }
        } finally {
            conn.close();
        }
    }

    private static void initJdbcDriver() {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
}

/**
 * Crea un conexión implementando el <b>Delegation pattern</b> sobre la
 * conexión real entregada.
 *
 * <p>
 * El patrón de delegación permitirá implementar un partial mock de la conexión
 * que ignorará los métodos de gestión de transacciones y el cierre de la
 * conexión, para que los test puedan realizar el rollback de las
 * modificaciones de los DAOS.</p>
 *
 * <p>
 * <b>IMPORTANTE:</b><br/>
 *    No se puede utilizar {@link org.mockito.Mockito#spy(java.lang.Object)} ya
 *    que se necesita control sobre la conexión real y mockito se basa en la
 *    creación de proxies que realizan una copia de la instancia. Por tanto
 *    las acciones sobre la conexión real no afectarían al proxy.</p>
 *
 * <p>
 * <b>IMPLEMENTACIÓN DE LA DELEGACIÓN</b><br/>
 *    Java no proporciona soporte para la implementación automática del patrón
 *    de delegación (que otros lenguajes si proporcionan). Por tanto la delegación
 *    de interfaces muy extensos es muy propenso a errores por descuido. En este
 *    caso se ha generado automáticamente con la opción <i>"Insert Code: Delegate
 *    Methods"</i> del Netbeans IDE, una opción existente en todos los IDE.</p>
 *
 * @param connection
 * @return
 * @throws SQLException
 */
abstract class ConnectionWrapper implements Connection {
    private Connection connection;

    public ConnectionWrapper(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        connection.setTypeMap(map);
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        connection.setTransactionIsolation(level);
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return connection.setSavepoint(name);
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return connection.setSavepoint();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        connection.setReadOnly(readOnly);
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        connection.setHoldability(holdability);
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        connection.setClientInfo(properties);
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        connection.setClientInfo(name, value);
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        connection.setCatalog(catalog);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        connection.setAutoCommit(autoCommit);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        connection.rollback(savepoint);
    }

    @Override
    public void rollback() throws SQLException {
        connection.rollback();
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        connection.releaseSavepoint(savepoint);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return connection.prepareStatement(sql, columnNames);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return connection.prepareStatement(sql, columnIndexes);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return connection.prepareStatement(sql, autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return connection.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return connection.prepareCall(sql);
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        return connection.nativeSQL(sql);
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        return connection.isValid(timeout);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return connection.isReadOnly();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return connection.isClosed();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return connection.getWarnings();
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return connection.getTypeMap();
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return connection.getTransactionIsolation();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return connection.getMetaData();
    }

    @Override
    public int getHoldability() throws SQLException {
        return connection.getHoldability();
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return connection.getClientInfo();
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return connection.getClientInfo(name);
    }

    @Override
    public String getCatalog() throws SQLException {
        return connection.getCatalog();
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return connection.getAutoCommit();
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return connection.createStruct(typeName, attributes);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return connection.createStatement(resultSetType, resultSetConcurrency);
    }

    @Override
    public Statement createStatement() throws SQLException {
        return connection.createStatement();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return connection.createSQLXML();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return connection.createNClob();
    }

    @Override
    public Clob createClob() throws SQLException {
        return connection.createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        return connection.createBlob();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return connection.createArrayOf(typeName, elements);
    }

    @Override
    public void commit() throws SQLException {
        connection.commit();
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }

    @Override
    public void clearWarnings() throws SQLException {
        connection.clearWarnings();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return connection.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return isWrapperFor(iface);
    }
}