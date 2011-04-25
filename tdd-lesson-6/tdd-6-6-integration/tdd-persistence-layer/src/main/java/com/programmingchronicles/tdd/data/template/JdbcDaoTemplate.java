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

import com.programmingchronicles.tdd.data.SQLDaoException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

/**
 * Implementación del patrón de diseño <b>template</b> para la centralización
 * del código de acceso a base de datos.
 *
 * <p>El patrón de diseño <i>template</i> es útil cuando varios métodos solo
 * difieren en unas pocas líneas internas, permitiendo extraer el código
 * repetitivo que rodea dicho código.</p>
 *
 * <p>El patrón <b>template</b> permite modificar el comportamiento de un código
 * de plantilla mediante la entrega de callbacks que se ejecutan en puntos
 * concretos de la plantilla.
 *
 * <p>
 * <b>Código Testeable</b><br/>
 * La utilización de patrones de este tipo permite centralizar un código
 * propenso a errores por descuido. Esto permite controlar en un solo
 * punto un código que de otra forma estaría repetido en múltiples clases.</p>
 *
 * <p>
 * En el caso de JDBC esto permite diseñar test unitarios más rigurosos y
 * <b>completamente independientes de la base de datos</b> sin necesidad de
 * controlar en los DAOs y en sus test la gestión de objetos de acceso a la
 * base de datos.</p>
 *
 * <p><b>Nota:</b><br/>
 * Existen librerías que ya proporcionan este tipo de templates para JDBC,
 * como <i>JDBC Spring Templates</i>.</p>
 *
 * @author Pedro Ballesteros <pedro@theprogrammingchronicles.com>
 */
public class JdbcDaoTemplate {

    private DataSource datasource;

    /**
     * Construye un template que ejecuta sentencias usando conexiones
     * del datasource entregado.
     *
     * @param datasource
     */
    public JdbcDaoTemplate(DataSource datasource) {
        this.datasource = datasource;
    }

    /**
     * Ejecuta una query y devuelve la lista de resultados mapeando las filas
     * en el callback del <i>columnMapper</i> entregado.
     *
     * @param sql
     * @param columnMapper El {@link ColumnMapper} encargado de leer una fila
     *                     del resultset y devolver el objeto mapeado.
     * @return Devuelve la lista de objetos T devuelto por la query.
     */
    public <T> List<T> query(String sql, ColumnMapper<T> columnMapper) {
        return query(sql, null, columnMapper);
    }

    /**
     * Ejecuta una query y devuelve la lista de resultados mapeando las filas
     * en el callback del <i>columnMapper</i> entregado.
     *
     * @param sql
     * @param parameterMapper El {@link ParameterMapper} encargado de inicializar
     *        los parametros de la sentencia a ejecutar.
     * @param columnMapper El {@link ColumnMapper} encargado de leer una fila
     *        del resultset y devolver el objeto mapeado.
     * @return
     */
    public <T> List<T> query(String sql, ParameterMapper parameterMapper, final ColumnMapper<T> columnMapper) {
        // El resultado se obtiene creado un PreparedCallback ejecutado por
        // el método execute genérico.
        List<T> result;
        result = (List)execute(sql, false, parameterMapper,
            new PreparedCallback() {
                @Override
                public Object process(PreparedStatement statement) throws SQLException {
                    // Sólo queda ejecutar la sentencia, ya preparada e inicializada.
                    ResultSet rs = statement.executeQuery();
                    try {
                        List<T> result = new ArrayList();
                        while(rs.next()) {
                            // Inserta en la lista el objeto creado por el callback
                            // de mapeo de columnas.
                            result.add(columnMapper.mapColumns(rs));
                        }
                        return result;
                    } finally {                       
                        rs.close();
                    }
                }
            });
        return result;
    }

    /**
     * Ejecuta una query de actualización que no devuelve ningún resultado.
     *
     * @param sql
     * @param parameterMapper El {@link ParameterMapper} encargado de inicializar
     *        los parametros de la sentencia a ejecutar.
     */
    public void update(String sql, ParameterMapper parameterMapper) {
        update(sql, false, parameterMapper);
    }

    /**
     * Ejecuta una query de inserción de columnas que devuelve una lista
     * con las claves generadas.
     *
     * @param sql
     * @param parameterMapper El {@link ParameterMapper} encargado de inicializar
     *        los parametros de la sentencia a ejecutar.
     * @return Lista de claves numéricas generadas.
     */
    public List<Number> insert(String sql, ParameterMapper parameterMapper) {
        return update(sql, true, parameterMapper);
    }

    /**
     * Ejecuta una query de actualización que puede devolver las claves generadas
     * en función del parámetro <i>returnKey</i>. Si <i>returnKey</i> es false
     * siempre se devuelbe null.
     *
     * @param sql
     * @param returnKey Indica si hay que devolver claves generadas.
     * @param parameterMapper El {@link ParameterMapper} encargado de inicializar
     *        los parametros de la sentencia a ejecutar.
     * @return Si returnKey es false siempre devuelve null.
     */
    public List<Number> update(String sql, final boolean returnKey, ParameterMapper parameterMapper) {
        List<Number> result;
        result = (List) execute(sql, returnKey, parameterMapper,
            new PreparedCallback() {
            @Override
            public Object process(PreparedStatement statement) throws SQLException {
                // Ejecuta la query ya preparada e inicializada.
                statement.execute();

                // Si hay que devolver claves se obtiene el ResultSet de
                // las claves generadas.
                if(returnKey) {
                    ResultSet rsKey = statement.getGeneratedKeys();
                    try {
                        List<Number> keys = new ArrayList();
                        while(rsKey.next()) {
                            keys.add((Number)rsKey.getObject(1));
                        }
                        return keys;
                    } finally {                        
                        rsKey.close();
                    }
                }
                return null;
            }
        });
        return result;
    }

    /**
     * Ejecuta una query generica que devuelve el resultado en función de los
     * callbacks y opciones entregadas.
     *
     * <p>Este template se configura definiendo un {@link PreparedCallback} que
     * recibe un {@link PreparedStatement} con los parametros ya inicialidados.
     * El código del callback puede usar el preparedStatement para ejecutar
     * la query deseada y devolver un resultado completo.</p>
     *
     * @param sql
     * @param returnKey Indica si hay que devolver claves generadas.
     * @param parameterMapper El {@link ParameterMapper} encargado de inicializar
     *        los parametros de la sentencia a ejecutar.
     * @param preparedCallback El {@link PreparedCallback} que recibe la sentencia
     *        preparada para su ejecución y que devuelve el resultado.
     * @return Devuelve el objeto generado por el {@link PreparedCallback} entregado.
     */
    public Object execute(String sql, boolean returnKey, ParameterMapper parameterMapper, PreparedCallback preparedCallback) {
        try {
            Connection conn = datasource.getConnection();
            try {
                // Siempre se usa un PreparedStatement ya que se puede usar
                // con parámetros o sin parametros. Se crea en función de si
                // se utilizará o no para devolver claves generadas.
                PreparedStatement statement;

                if(returnKey) { 
                    statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                } else {
                    statement = conn.prepareStatement(sql);
                }

                try {
                    // Ejecuta el callback de inicialización de parametros.
                    if (parameterMapper != null) {
                        parameterMapper.mapParameters(statement);
                    }
                    // Ejecuta con el callback el procesamiento de la sentencia
                    // que decide el objeto a devolver.
                    return preparedCallback.process(statement);
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

    /**
     * Devuelve el datasource configurado en el template.
     *
     * @return
     */
    public DataSource getDatasource() {
        return datasource;
    }
}
