/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Montessori;

/**
 *
 * @author David
 */
import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;

public class PoolC3P0_Local {
    // Notara que el pool es un miembro *estatico* esto es para evitar duplicidad

    private static PoolC3P0_Local datasource;
    // Esta es la fuente de datos que conecta con la base de datos
    private final ComboPooledDataSource cpds;

    /**
     * Crea el constructor del pool, notara que este constructor es privado esto
     * con el fin de que podamos controlar cuando se crea el pool
     *
     * @throws IOException
     * @throws SQLException
     * @throws PropertyVetoException
     */
    /*
             configDBCP(ds_eduweb,"jdbc:postgresql://192.168.1.3:5432/Lessons",
                     "eduweb","Madrid2016",
                    "org.postgresql.Driver");
     */
    private PoolC3P0_Local() throws IOException, SQLException, PropertyVetoException {
        // Configuramos la conexion a base de datos
        // Creamos la fuente de datos
        cpds = new ComboPooledDataSource();
        // Que driver de base de datos usaremos
        //cpds.setDriverClass("org.postgresql.Driver");
        // La url de la base de datos a la que nos conectaremos
        //cpds.setJdbcUrl("jdbc:postgresql://192.168.1.9:5432/Lessons");
        // Usuario de esa base de datos
        //cpds.setUser("eduweb");
        // Contraseña de la base de datos
       //cpds.setPassword("Madrid2016");

        cpds.setDriverClass(BambooConfig.driverClassName_bamboo);
           // La url de la base de datos a la que nos conectaremos
        cpds.setJdbcUrl(BambooConfig.url_bamboo);
           // Usuario de esa base de datos
        cpds.setUser(BambooConfig.user_bamboo);
           // Contraseña de la base de datos
        cpds.setPassword(BambooConfig.pass_bamboo);

        // Configuramos el pool
        // Numero de conexiones con las que iniciara el pool
        cpds.setInitialPoolSize(25);
        // Minimo de conexiones que tendra el pool
        cpds.setMinPoolSize(25);
        // Numero de conexiones a crear cada incremento
        cpds.setAcquireIncrement(1);
        // Maximo numero de conexiones
        cpds.setMaxPoolSize(100);
        // Maximo de consultas
        cpds.setMaxStatements(900);
        // Maximo numero de reintentos para conectar a base de datos
        // cpds.setAcquireRetryAttempts(2);
        // Que se genere una excepcion si no se puede conectar
        cpds.setBreakAfterAcquireFailure(true);
        
        cpds.setIdleConnectionTestPeriod(300);
        cpds.setMaxIdleTimeExcessConnections(240);
    }

    /**
     * Nos regresa la instancia actual del pool, en caso que no halla una
     * instancia crea una nueva y la regresa
     *
     * @return
     * @throws IOException
     * @throws SQLException
     * @throws PropertyVetoException
     */
    public static PoolC3P0_Local getInstance() throws IOException, SQLException, PropertyVetoException {

        if (datasource == null) {
            datasource = new PoolC3P0_Local();
            return datasource;
        } else {
            return datasource;
        }
    }

    /**
     * Este metodo nos regresa una conexion a base de datos, esta la podemos
     * usar como una conexion usual
     *
     * @return Conexion a base de datos
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        return this.cpds.getConnection();
    }

}
