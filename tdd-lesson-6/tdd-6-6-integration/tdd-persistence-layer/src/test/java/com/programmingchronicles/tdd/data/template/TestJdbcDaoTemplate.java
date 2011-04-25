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

package com.programmingchronicles.tdd.data.template;

import com.programmingchronicles.tdd.data.template.PreparedCallback;
import com.programmingchronicles.tdd.data.template.ColumnMapper;
import com.programmingchronicles.tdd.data.template.JdbcDaoTemplate;
import com.programmingchronicles.tdd.data.template.ParameterMapper;
import com.programmingchronicles.tdd.data.SQLDaoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import javax.sql.DataSource;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.junit.matchers.JUnitMatchers.*;

/**
 * Test de JdbcDaoTemplate que muestra un ejemplo de cómo el evitar el código
 * repetido (en este caso mediante el <b>patrón de diseño template</b>) permite
 * diseñar test unitarios completamente independientes de la base de datos.
 *
 * <p>Siguiendo la idea de evitar al máximo la verificación por interacción, en
 * estos tests se observan ejemplos de validación por estado, que en una primera
 * aproximación solo parecen viables con validación por interacción.</p>
 *
 * <p>No obstante la validación por interacción es util para verificar código
 * que no devuelve resultados, como el cerrado o liberación de recursos,
 * o updates que no devuelven nada.</p>
 *
 * <p>La decisión entre interacción y estado es principalmente un esfuerzo por
 * evitar verificaciones redundantes que pueden ser probadas de una forma
 * más útil mediante la buena programación de respuestas en los stubs.</p>
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class TestJdbcDaoTemplate {

    // Object Under Test
    JdbcDaoTemplate template;

    // Stubs y Mocks
    Connection mockConnection;
    DataSource mockDataSource;

    @Before
    public void setUp() throws SQLException {
        mockConnection = mock(Connection.class);       

        // Programa el stub del datasource para devolver un mock de
        // la conexión.
        mockDataSource = mock(DataSource.class);
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        when(mockDataSource.getConnection(anyString(), anyString()))
             .thenThrow(new AssertionError("Not Allowed."));

        // Configura el Object Under Test.
        template = new JdbcDaoTemplate(mockDataSource);
    }

    @Test
    public void testExecute() throws SQLException {
        // Programa un PreparedStatement que sólo se devuelve cuando
        // NO se usa generación de claves. El test solo funcionará
        // si el template obtiene PreparedStatement como se ha programado.
        final PreparedStatement mockStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement("SQL")).thenReturn(mockStatement);       
        
        // Test. El método execute necesitará dos callbacks.
        final StringBuffer parameter = new StringBuffer();
        Object result = template.execute("SQL", false,
            new ParameterMapper() {
                @Override
                public void mapParameters(PreparedStatement statement) throws SQLException {
                    // Verifica que se recibe el PreparedStatement programado.
                    assertSame(mockStatement, statement);                    
                    // Simula que se establecen los parametros.
                    parameter.append("parameter");
                }
            }, new PreparedCallback() {
                @Override
                public Object process(PreparedStatement statement) throws SQLException {
                    // Verifica que se recibe el PreparedStatement programado.
                    assertSame(mockStatement, statement);
                    return("result");
                }
        });

        // Verifica que el resultado es el mismo que devuelve el callback.
        assertEquals("result", result);

        // Verifica que se ha usado el callback de inicio de parametros.
        assertEquals("parameter", parameter.toString());

        // Verifica por interacción la liberación de recursos.
        verify(mockStatement).close();
        verify(mockConnection).close();
    }

    @Test
    public void testExecuteWithoutParams() throws SQLException {
        // Programa un PreparedStatement que sólo se devuelve cuando
        // NO se usa generación de claves. El test solo funcionará
        // si el template obtiene PreparedStatement como se ha programado.
        final PreparedStatement mockStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement("SQL")).thenReturn(mockStatement);
        
        // Test. El método execute necesitará un callback.
        Object result = template.execute("SQL", false, null,
            new PreparedCallback() {
                @Override
                public Object process(PreparedStatement statement) throws SQLException {
                    // Verifica que se recibe el PreparedStatement programado.
                    assertSame(mockStatement, statement);
                    return("result");
                }
        });

        // Verifica que el resultado es el mismo que devuelve el callback.
        assertEquals("result", result);

        // Verifica por interacción la liberación de recursos.
        verify(mockStatement).close();
        verify(mockConnection).close();
    }

    @Test
    public void testExecuteWithReturnKeys() throws SQLException {
        // Programa un PreparedStatement que sólo se devuelve si se usa
        // la generación de claves.
        
        // NOTA: El test solo funcionará si el template obtiene el PreparedStatement
        // usando RETURN_GENERATED_KEYS.
        final PreparedStatement mockStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement("SQL", Statement.RETURN_GENERATED_KEYS))
             .thenReturn(mockStatement);

        // Test. El método execute necesitará un callbacks.
        Object result = template.execute("SQL", true, null,
               new PreparedCallback() {
                @Override
                public Object process(PreparedStatement statement) throws SQLException {
                    // Verifica que se recibe el PreparedStatement programado.
                    assertSame(mockStatement, statement);
                    return("result");
                }
        });

        // NOTA: El test es practicamente igual que "testExecute" pero en este
        // el stub de la conexión solo funciona si se usa RETURN_GENERATED_KEYS
        // para el PreparedStatement. En otro caso los tests fallaran.

        // Verifica que el resultado es el mismo que devuelve el callback.
        assertEquals("result", result);      

        // Verifica por interacción la liberación de recursos.
        verify(mockStatement).close();
        verify(mockConnection).close();
    }

    @Test
    public void testExecuteThrowException() throws SQLException {
        final PreparedStatement mockStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement("SQL")).thenReturn(mockStatement);

        try {
            // Test.
            template.execute("SQL", false, null,
                new PreparedCallback() {
                    @Override
                    public Object process(PreparedStatement statement) throws SQLException {
                        // Verifica que se recibe el PreparedStatement programado.
                        assertSame(mockStatement, statement);
                        // Simula una excepción SQL.
                        throw new SQLException();
                    }
            });
            // Si no se lanza una excepción el test ha fallado.
            fail("SQLDaoException expected");

        } catch(SQLDaoException ex) {
            // Verifica la liberación de recursos.
            verify(mockStatement).close();
            verify(mockConnection).close();
        }
    }

    @Test
    public void testUpdate() throws SQLException {
        final PreparedStatement mockStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement("SQL")).thenReturn(mockStatement);

        // Test
        final StringBuffer parameter = new StringBuffer();
        List<Number> keys = template.update("SQL", false, 
            new ParameterMapper() {
                @Override
                public void mapParameters(PreparedStatement statement) throws SQLException {
                    // Verifica que se recibe el PreparedStatement programado.
                    assertSame(mockStatement, statement);                    
                    // Simula que se establecen los parametros.
                    parameter.append("parameter");
                }
            });

        // Verifica que se ha ejecutado el update.
        verify(mockStatement).execute();

        // No se ha solicitado la obtención de las claves.
        assertNull(keys);       

        // Verifica que se ha usado el callback de inicio de parametros.
        assertEquals("parameter", parameter.toString());

        // Verifica la liberación de recursos.
        verify(mockStatement).close();
        verify(mockConnection).close();
    }

    @Test
    public void testUpdateReturnKeys() throws SQLException {
        final PreparedStatement mockStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement("SQL", Statement.RETURN_GENERATED_KEYS))
             .thenReturn(mockStatement);

        // Se crea un stub del "next()" del resulset de claves para simular que
        // el update devuelve la lista de claves.
        ResultSet resultset = mock(ResultSet.class);
        when(mockStatement.getGeneratedKeys()).thenReturn(resultset);

        // Se programan tres respuestas consecutivas del "next()" para
        // simular que se reciben dos claves.
        when(resultset.next()).thenReturn(true, true, false);
        when(resultset.getObject(anyInt())).thenReturn(new Long(1001), new Long(2002));

        // Test
        List<Number> keys = template.update("SQL", true, null);

        // Verifica que se ha ejecutado el update.
        verify(mockStatement).execute();

        // Verifica que se han recibido las dos claves programadas.
        assertEquals(2, keys.size());
        assertThat(keys, hasItems((Number)new Long(1001), (Number)new Long(2002)));

        // Verifica la liberación de recursos.
        verify(resultset).close();
        verify(mockStatement).close();
        verify(mockConnection).close();
    }

    @Test
    public void testUpdateReturnKeysThrowException() throws SQLException {
        final PreparedStatement mockStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement("SQL", Statement.RETURN_GENERATED_KEYS))
             .thenReturn(mockStatement);

        // Se crea un stub del "next()" del resulset de claves para simular que
        // lanza una excepción SQL.
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenThrow(new SQLException());

        // Test
        try {
            template.update("SQL", true, null);
            fail("SQLDaoException expected");
        } catch(SQLDaoException ex) {
            // Verifica que se ha ejecutado el update.
            verify(mockStatement).execute();
            // Verifica la liberación de recursos.
            verify(mockResultSet).close();
            verify(mockStatement).close();
            verify(mockConnection).close();
        }            
    }

    @Test
    public void testQuery() throws SQLException {
        final PreparedStatement mockStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement("SQL")).thenReturn(mockStatement);

        final ResultSet mockResultSet = mock(ResultSet.class);
        // El método "query()" utiliza "executeQuery()" para obtener
        // el resultset con los resultados.
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);

        // Se crea un stub del "next()" del resulset para simular que
        // se devuelve una lista de 2 resultados.
        when(mockResultSet.next()).thenReturn(true, true, false);
        // Se programan un "getString()" para devolver dos string
        // consecutivos en el resultset.
        when(mockResultSet.getString(anyInt())).thenReturn("string1", "string2");

        // Test
        List<String> rows = template.query("SQL",
            new ColumnMapper<String>() {
                @Override
                public String mapColumns(ResultSet resultset) throws SQLException {
                    // Devuelve el valor configurado en el mock del resultset.
                    return resultset.getString(0);
                }
            });

        // Se debe haber obtenido una lista con los dos strings programados
        // en el stub del resultset.
        assertEquals(2, rows.size());
        assertThat(rows, hasItems("string1", "string2"));

        // Verifica la liberación de recursos.
        verify(mockResultSet).close();
        verify(mockStatement).close();
        verify(mockConnection).close();
    }

    @Test
    public void testQueryThrowException() throws SQLException {
        final PreparedStatement mockStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement("SQL")).thenReturn(mockStatement);

        final ResultSet mockResultSet = mock(ResultSet.class);
        // El método "query()" utiliza "executeQuery()" para obtener
        // el resultset con los resultados.
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);

        // Se crea un stub del "next()" del resulset para simular que
        // se devuelve una lista de 2 resultados.
        when(mockResultSet.next()).thenReturn(true, true, false);

        // Test
        try {
            template.query("SQL",
                new ColumnMapper<String>() {
                    @Override
                    public String mapColumns(ResultSet resultset) throws SQLException {
                        // Simula una excepción SQL para comprobar que se cierran
                        // recursos y se convierte a un SQLDaoException.
                        throw new SQLException();
                    }
                });
            fail("SQLDaoException expected");
        } catch(SQLDaoException ex) {
            // Verifica la liberación de recursos.
            verify(mockResultSet).close();
            verify(mockStatement).close();
            verify(mockConnection).close();
        }
    }
}
