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

import com.programmingchronicles.tdd.data.support.JdbcAddressBookDao;
import com.programmingchronicles.tdd.domain.Contact;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.matchers.TypeSafeMatcher;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import javax.sql.DataSource;
import java.sql.*;
import java.util.Map;
import java.util.Properties;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;
import static org.mockito.Mockito.*;
import static com.programmingchronicles.tdd.data.support.IsContactEqual.*;


/**
 * Test de una implementación AddressBookDao basada en JDBC.
 *
 * <p><b>Ver:</b><br/>
 * Ejemplo de custom matcher: {@link IsContactEqual}.</p>
 *
 * <p><b>Ver:</b><br/>
 * {@link #setUp() }, {@link #tearDown() }: El control de conexiones
 * cerradas muestra otro ejemplo de que es mejor usar nuestra propia
 * implementación, que basarse en el verify de los mocks.</p>
 *
 * <p>Este ejemplo muestra cómo hacer tests unitarios de objetos DAO, sin
 * necesidad de usar una base de datos de desarrollo. El sistema de mock de
 * la base de datos se basa en el uso de una base de datos embebida y en
 * memoria para hacer los test veloces.</p>
 *
 * <p><b>Importante:</b> En un ejemplo posterior se verá que una buena refactorización
 * incluso propicia la creación de test unitarios reales.</p>
 *
 * <p>Esta solución sólo es válida para código JDBC que no utilice SQL específico
 * de un sistema de base de datos concreto y utilice SQL genérico.</p>
 *
 * <p>Si no se puede usar SQL genérico, solo se podrían lanzar los test contra
 * un base de datos externa de desarrollo. Aunque de forma rigurosa ambos
 * serían test de integración, por estar usando una base de datos, en este
 * caso los test se alejan algo más de las propiedades FIRST, y no sería fácil
 * lanzarlos continuamente para el desarrollo TDD.</p>
 *
 * <p>El sistema de independencia entre tests basado en el rollback se puede seguir
 * usando con una base de datos externa. Los datos ya existentes no interfieren
 * ya que al inicializar los tests se puede realizar un borrado que será deshecho
 * en el rollback.</p>
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class TestJdbcAdressBookDAO {

    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    // Aunque podría comprometer la propiedad de independencia si no se
    // tiene cuidado, se reutilizará la misma conexión en todos los tests
    // para mejorar la velocidad.

    // La independencia entre tests se asegura con el rollback de cada test.
    private static Connection connection;

    // Object Under Test
    private JdbcAddressBookDao dao;

    // Contacto por defecto usado en los tests
    private Contact expectedContact;

    // Es importante comprobar que el DAO cierra la conexión. Si se usa un
    // DataSource basado en un pool de conexiones, el garbage collection no las
    // podrá cerrar nunca, lo que puede agotar las conexiones del pool.
    private int openConnections = 0;

    /**
     * Inicializaciones comunes para todos los test (Fixture de Clase).
     *
     * <p>
     * <b>CUIDADO:</b><br/>
     * Peligro de Violación del principio INDEPENDENT. Los tests tienen que
     * seguir siendo independientes. El uso de atributos static o de clase
     * puede interferir en esta independencia.</p>
     *
     * <p>
     * En este caso la creación de las tablas, aunque sea en memoria
     * es lenta, por lo que es mejor hacerlo una sola vez antes de iniciar
     * los tests, y evitar la creación de las tablas en cada test.</p>
     *
     * <p>
     * <b>Test de Integración</b><br/>
     * Si se quisieran usar como test de integración contra la base de datos
     * real, tan sólo habría que eliminar este código de creación de tablas.</p>
     *
     * <p>
     * <b>IMPORTANTE</b><br/>
     * No se require la creación del esquema completo de la base de datos, tan
     * solo las tablas y campos que se necesitan para los tests de este DAO.</b>
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws ClassNotFoundException, SQLException {
        // Inicialización del driver de la base de datos.
        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        Class.forName(driver);

        // Aunque podría comprometer la propiedad de independencia si no se
        // tiene cuidado, se reutilizará la misma conexión en todos los tests
        // para mejorar la velocidad.

        // Creación de las tablas en una base de datos en memoria.
        String connectionURL = "jdbc:derby:memory:myInMemDB;create=true";
        connection = DriverManager.getConnection(connectionURL);

        String sqlCreateContactTable =
                "CREATE TABLE CONTACTS (" +
                  "ID INT NOT NULL GENERATED ALWAYS AS IDENTITY, " +
                  "FIRSTNAME VARCHAR(255), "  +
                  "SURNAME VARCHAR(255), " +
                  "BIRTHDAY DATE, " +
                  "PHONE VARCHAR(255))";

        Statement stm = connection.createStatement();
        try {
            stm.execute(sqlCreateContactTable);
        } finally {
            stm.close();
        }
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        // Como las tablas se crean en memoria, no es necesario realizar
        // el borrado del esquema, tan solo se libera la conexión que se
        // ha reutilizado en todos los tests.
        connection.close();
    }

    /**
     * Crea un stub del datasource que devuelve siempre un wrapper de la conexión
     * entregada, que ignora las transacciones y el cierre de conexión, para que
     * los tests puedan realizar el rollback de las modificaciones.
     *
     * <p>El wrapper de la conexión también permitirá realizar comprobaciones
     * de que el DAO las cierra correctamente. Si usando un DataSource  basado
     * en pool de conexiones no se realiza el close, el garbage collection
     * podría no cerrarlas nunca, lo que puede agotar las conexiones del pool.</p>
     *
     * @see ConnectionWrapper
     *
     * @param connection
     * @return
     * @throws SQLException
     */
    protected DataSource newSingleNonTransactionalDataSource(Connection connection) throws SQLException {

        // Crea un wrapper de la conexión para ignorar transacciones.
        final Connection nonTransactionalConnection = new ConnectionWrapper(connection) {
            // El close actualiza el contador de conexiones del test.
            @Override
            public void close() throws SQLException {
                openConnections--;
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

        // El datasource devuelve el wrapper de la conexión y actualiza el
        // contador de conexiones del test
        when(mockDatasource.getConnection()).thenAnswer(
          new Answer<Connection>() {
                @Override
                public Connection answer(InvocationOnMock invocation) throws Throwable {
                    openConnections++;
                    return nonTransactionalConnection;
                }
          });

        when(mockDatasource.getConnection(anyString(), anyString())).thenAnswer(
          new Answer<Connection>() {
                @Override
                public Connection answer(InvocationOnMock invocation) throws Throwable {
                    openConnections++;
                    return nonTransactionalConnection;
                }
          });

        return mockDatasource;
    }

    /**
     * Antes de cada test se inicia la transacción. Las tablas en la base
     * de datos deben existir.
     *
     * @throws SQLException
     */
    @Before
    public void setUp() throws SQLException {
        // Gestión manual de transacción para hacer el rollback al finalizar
        // cada uno de los tests.
        connection.setAutoCommit(false);

        // Contacto por defecto usado en las pruebas.
        expectedContact = new Contact();

        // Crea el Object Under Test configurado con el stub del datasource.
        dao = new JdbcAddressBookDao();
        dao.setDataSource(newSingleNonTransactionalDataSource(connection));
    }

    /**
     * Despues de cada test se realiza un rollback para asegurar la
     * independencia entre los tests.
     *
     * @throws SQLException
     */
    @After
    public void tearDown() throws SQLException {
        // Es importante comprobar que el DAO cierra la conexión. Si se usa un
        // DataSource basado en pool de conexiones, el garbage collection no
        // podrá cerrarla nunca, con lo que se puede agotar el pool.
        assertEquals(0, openConnections);
        connection.rollback();
    }

    /**
     * Añade un contacto simple y comprueba el resultado obteniendo
     * todos los contactos.
     *
     * <p> Esta implementación utiliza el método {@link JdbcAddressBookDao#getAll() }
     * del DAO para comprobar el estado de la base de datos.</p>
     *
     * <p> Existen argumentos en contra y a favor de este tipo de validaciones,
     * ya que el usar métodos del Object Under Test para obtener los datos
     * de la validación podría conducir a errores ocultos. Pero sería muy
     * difícil que un error combinado del addContact y getAll resultara en
     * un falso positivo.</p>
     *
     * <p> Si se necesitan test de integración con la base de datos rigurosos
     * el resultado se debería comprobar leyendo la base de datos desde
     * los tests sin utilizar el DAO.</p>
     *
     * @throws SQLException
     */
    @Test
    public void testAddSimpleContact() throws SQLException {
        // Test Object
        expectedContact.setFirstName("Pedro");

        // Test
        String contactId = dao.addContact(expectedContact);

        // La verificación se realiza leyendo el contacto con los métodos
        // del lectura del DAO.
        List<Contact> contacts = dao.getAll();
        assertEquals(1, contacts.size());
        assertEquals(contactId, contacts.get(0).getId());
        assertEquals("Pedro", contacts.get(0).getFirstName());
    }

    /**
     * Ejemplo de test de integración que no se apoya en los métodos del
     * propios del DAO para realizar la validación.
     *
     * <p> En este caso el test valida que realmente se guardan los datos en
     * la base de datos, sin apoyarse en los propios métodos del DAO.</p>
     *
     * <p> Hacer este tipo de tests es muy pesado y requiere mucho más código. El
     * utilizar el propio DAO para la lectura de la base de datos aligera
     * los test, y no supone relajar tanto la rigurosidad de la comprobación.</p>
     *
     * <p>No obstante existen librerías especializadas que proporcionan facilidades
     * para este tipo de validaciones. Para minimizar el código necesario para
     * acceder a la base de datos, crear datos de test y validar con lecturas
     * directas.</p>
     *
     * <p> Estas librerías también son útiles para test de integración de más alto
     * nivel que necesitan acceder a la base de datos, y no es posible utilizar
     * un DAO.</p>
     *
     * <p>
     * <b>IMPORTANTE:</b><br/>
     * Como se observa en el ejemplo, este tipo de test son muy laboriosos
     * si no se cuenta con una librería de ayuda al testing de base de datos
     * ya que el código del test es un reflejo práticamente fiel de la
     * implementación que se necesitará en los métodos de lectura</b>.
     *
     * <p><b>Ej.</b> DbUnit o Sprint Test Utilities.</p>
     *
     * @throws SQLException
     */
    @Test
    public void testIntegrationAddContact() throws SQLException, ParseException {
        // Test Object
        expectedContact.setFirstName("Pedro");
        expectedContact.setSurname("Ballesteros");
        Date expectedBirthday = dateFormat.parse("8/1/1974");
        expectedContact.setBirthday(expectedBirthday);
        expectedContact.setPhone("610101010");

        // Test
        String contactId = dao.addContact(expectedContact);


        // La validación debe leer directamente con JDBC la base de datos.
        List<Contact> contacts = new ArrayList();
        Statement statement = connection.createStatement();
        try {
            ResultSet rsContacts = statement
              .executeQuery("SELECT ID, FIRSTNAME, SURNAME, BIRTHDAY, PHONE " +
                            "FROM CONTACTS");
            try {
                while (rsContacts.next()) {
                    Contact contact = new Contact();

                    contact.setId(Long.toString(rsContacts.getInt("ID")));
                    contact.setFirstName(rsContacts.getString("FIRSTNAME"));
                    contact.setSurname(rsContacts.getString("SURNAME"));
                    contact.setBirthday(rsContacts.getDate("BIRTHDAY"));
                    contact.setPhone(rsContacts.getString("PHONE"));

                    contacts.add(contact);
                }
            } finally {
                rsContacts.close();
            }
        } finally {
            statement.close();
        }

        // Verifica que los datos leídos de la base de datos coinciden
        // con los insertados a través del DAO.
        assertEquals(1, contacts.size());
        expectedContact.setId(contactId);
        assertThat(contacts.get(0), contactEq(expectedContact));
    }

    /**
     * Ejemplo de uso de un custom matcher para realizar la comparación
     * de dos contactos.
     *
     * <p>
     * Aunque se podría hacer lo mismo implementando el método Contact#equals,
     * de esta forma podemos hacer comparaciones no ligadas a la lógica, como
     * por ejemplo ignorar el id en las comparaciones.</p>
     *
     * <p>
     * <b>Ver:</b> {@link IsContactEqual }.</p>
     *
     * @throws ParseException
     * @throws SQLException
     */
    @Test
    public void testAddAndGetFullContact() throws ParseException, SQLException {

        // Contacto Completo de Ejemplo.
        expectedContact.setFirstName("Pedro");
        expectedContact.setSurname("Ballesteros");
        Date expectedBirthday = dateFormat.parse("8/1/1974");
        expectedContact.setBirthday(expectedBirthday);
        expectedContact.setPhone("610101010");

        // Test
        String expectedId = dao.addContact(expectedContact);
        Contact contact = dao.getContact(expectedId);

        // Verificación
        expectedContact.setId(expectedId);


        assertThat(contact, contactEq(expectedContact));
    }

    @Test
    public void testDeleteContact() throws ParseException, SQLException {
        // Contacto Completo de Ejemplo.
        expectedContact.setFirstName("Pedro");
        expectedContact.setSurname("Ballesteros");
        Date expectedBirthday = dateFormat.parse("8/1/1974");
        expectedContact.setBirthday(expectedBirthday);
        expectedContact.setPhone("610101010");
        String expectedId = dao.addContact(expectedContact);

        // Test
        dao.deleteContact(expectedId);

        List<Contact> contacts = dao.getAll();
        assertEquals(0, contacts.size());
    }

    /**
     * Ejemplo de uso de un custom matcher para realizar la comparación
     * de dos contactos.
     *
     * <p>
     * Aunque se podría hacer lo mismo implementando el método Contact#equals,
     * de esta forma podemos hacer comparaciones no ligadas a la lógica, como
     * por ejemplo ignorar el id en las comparaciones.</p>
     *
     * <p>
     * En este ejemplo se ve su verdadera utilidad al ser combinado con
     * matchers como <b>"hasItems"</b> que permiten validar el contenido de
     * colecciones ignorando el orden de los elementos.</p>
     *
     * <p>
     * <b>Ver:</b> {@link IsContactEqual }.</p>
     *
     * @throws ParseException
     * @throws SQLException
     */
    @Test
    public void testGetAll() throws ParseException, SQLException {
        Date expedtedBirthday = dateFormat.parse("8/1/1974");

        Contact c1 = new Contact();
        c1.setFirstName("Pedro");
        c1.setSurname("Ballesteros");
        c1.setBirthday(expedtedBirthday);
        c1.setPhone("610101010");

        Contact c2 = new Contact();
        c2.setFirstName("Eduardo");
        c2.setSurname("Ballesteros");
        c2.setBirthday(expedtedBirthday);
        c2.setPhone("610101010");

        String id1 = dao.addContact(c1);
        c1.setId(id1);
        String id2 = dao.addContact(c2);
        c2.setId(id2);

        // Test
        List<Contact> contacts = dao.getAll();       

        // Verifica que la lista contiene unicamente los dos contactos
        // existentes en la base de datos.

        // IMPORTANTE: El matcher hasItems comprueba si los contactos
        // estan incluidos en la coleccion. Por lo que se necesita
        // usar el custom matcher "contactEq".
        assertEquals(2, contacts.size());
        assertThat(contacts, hasItems(contactNoIdEq(c2), contactEq(c1)));
    }

    /**
     * Si la tabla está vacía se debe devolver una lista vacía.
     *
     * @throws SQLException
     */
    @Test
    public void testGetAllEmpty() throws SQLException {
        // Test
        List<Contact> contacts = dao.getAll();
     
        assertEquals(0, contacts.size());
    }

    /**
     * Si el contacto no existe se debe ignorar y devolver null.
     *
     * @throws ParseException
     * @throws SQLException
     */
    @Test
    public void testGetContactDeleted() throws ParseException, SQLException {
        expectedContact.setFirstName("Pedro");
        expectedContact.setSurname("Ballesteros");
        Date expectedBirthday = dateFormat.parse("8/1/1974");
        expectedContact.setBirthday(expectedBirthday);
        expectedContact.setPhone("610101010");

        // Añade y elimina el contacto
        String expectedId = dao.addContact(expectedContact);
        dao.deleteContact(expectedId);

        // Se intenta obtener el contacto eliminado.
        Contact contact = dao.getContact(expectedId);

        // Si el contacto no existe debe devolver null.
        assertNull(contact);
    }

    /**
     * Si el contacto es inválido se entrega un id no admitido por la
     * implementación, no se deben lanzar excepciones, se debe comportar
     * igual que un contacto que no existente.
     *
     * @throws SQLException
     */
    @Test
    public void testGetContactInvalidId() throws SQLException {
        // La implementación trabaja con id numericos, si se entrega
        // un id invalido se debe comportar como si fuera un contacto
        // que no existe.
        Contact contact = dao.getContact("invalidId");
        assertNull(contact);        
    }

    /**
     * Eliminar un contacto que no existe.
     *
     * @throws ParseException
     * @throws SQLException
     */
    @Test
    public void testDeleteContactDeleted() throws ParseException, SQLException {
        expectedContact.setFirstName("Pedro");
        expectedContact.setSurname("Ballesteros");
        Date expectedBirthday = dateFormat.parse("8/1/1974");
        expectedContact.setBirthday(expectedBirthday);
        expectedContact.setPhone("610101010");

        // Añade y elimina el contacto
        String expectedId = dao.addContact(expectedContact);
        dao.deleteContact(expectedId);

        // Se intenta eliminar un contacto ya eliminado.
        dao.deleteContact(expectedId);    
    }

    /**
     * Eliminar un contacto con un id inválido.
     *
     * @throws ParseException
     * @throws SQLException
     */
    @Test
    public void testDeleteContactInvalidId() throws ParseException, SQLException {
        // La implementación trabaja con id numericos, si se entrega
        // un id invalido se debe comportar como si fuera un contacto
        // que no existe.
        dao.deleteContact("invalidId");
    }
}

/**
 * Implementación de un custom matcher de contact que permite realizar
 * comparaciones ignorando o no el id.
 *
 * <p>El matcher se puede usar con el <b>assertThat</b> de JUnit o para el
 * argument matching de los mocks de mockito.</p>
 *
 * <p>Aunque se podría hacer lo mismo implementando el método <i>Contact#equals</i>,
 * de esta forma podemos hacer comparaciones no ligadas a la lógica, como por
 * ejemplo ignorar el id en las comparaciones.</p>
 *
 * <p>Permite reducir el código de los tests, sobre todo cuando hay que
 * verificar listas y colecciones.</p>
 *
 * @throws ParseException
 * @throws SQLException
 */
class IsContactEqual extends TypeSafeMatcher<Contact> {
    private final Contact c2;
    private final boolean ignoreId;

    public IsContactEqual(Contact contact, boolean ignoreId) {
        this.c2 = contact;
        this.ignoreId = ignoreId;
    }

    public IsContactEqual(Contact contact) {
        this.c2 = contact;
        this.ignoreId = false;
    }

    /**
     * Se sobrescribe para definir cuando dos contactos son el mismo.
     *
     * @param c1
     * @return
     */
    @Override
    public boolean matchesSafely(Contact c1) {
        boolean result;
        result = equals(c1.getFirstName(), c2.getFirstName())  &&
                 equals(c1.getSurname(), c2.getSurname()) &&
                 equals(c1.getBirthday(), c2.getBirthday()) &&
                 equals(c1.getPhone(), c2.getPhone());
        
        if(!ignoreId) {
            result = result && equals(c1.getId(), c2.getId());
        }
        return result;
    }

    /**
     * Los hamcrest matchers dan información muy precisa de la causa del
     * fallo. Sobrescribiendo este método se puede mostrar el expected
     * object de forma detallada.
     *
     * <p>
     * Aunque se debería implementar el toString del Contact para mostrar
     * información completa.</p>
     *
     * @param description
     */
    @Override
    public void describeTo(Description description) {
        description.appendText("a contact with ")
                   .appendValueList("[", ", ", "]",
                          c2.getId(), c2.getFirstName(), c2.getSurname(),
                          c2.getBirthday(), c2.getPhone());
    }

    /**
     * Matcher que compara dos contactos completos.
     *
     * @param contact
     * @return
     */
    @Factory
    public static Matcher<Contact> contactEq(Contact contact) {
        return new IsContactEqual(contact);
    }

    /**
     * Matcher que compara dos contactos ignorando los ids.
     *
     * @param contact
     * @return
     */
    @Factory
    public static Matcher<Contact> contactNoIdEq(Contact contact) {
        return new IsContactEqual(contact, true);
    }

    private static boolean equals(Object o1, Object o2) {
        return o1 == o2 || (o1 != null && o1.equals(o2));
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
 * modificaciones de los DAOs.</p>
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