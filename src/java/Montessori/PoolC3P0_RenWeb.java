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
import com.mchange.v2.c3p0.*;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;

public class PoolC3P0_RenWeb {
    // Notara que el pool es un miembro *estatico* esto es para evitar duplicidad

    private static PoolC3P0_RenWeb datasource;
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
            configDBCP(ds_ah,"jdbc:sqlserver://ah-zaf.odbc.renweb.com\\ah_zaf:1433;databaseName=ah_zaf",
                    "AH_ZAF_CUST",
                    "BravoJuggle+396",
                    "com.microsoft.sqlserver.jdbc.SQLServerDriver");
     */
    private PoolC3P0_RenWeb() throws IOException, SQLException, PropertyVetoException {
        // Configuramos la conexion a base de datos
        // Creamos la fuente de datos
        cpds = new ComboPooledDataSource();
        // Que driver de base de datos usaremos
        // cpds.setDriverClass("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        // La url de la base de datos a la que nos conectaremos
        //cpds.setJdbcUrl("jdbc:sqlserver://ah-zaf.odbc.renweb.com\\\\ah_zaf:1433;databaseName=ah_zaf");
        // Usuario de esa base de datos
        // cpds.setUser("AH_ZAF_CUST");
        // Contraseña de la base de datos
        // cpds.setPassword("BravoJuggle+396");

        // Que driver de base de datos usaremos
        cpds.setDriverClass(BambooConfig.driverClassName_renweb);
        // La url de la base de datos a la que nos conectaremos
        cpds.setJdbcUrl(BambooConfig.url_renweb);
        // Usuario de esa base de datos
        cpds.setUser(BambooConfig.user_renweb);
        // Contraseña de la base de datos
        cpds.setPassword(BambooConfig.pass_renweb);

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
    public static PoolC3P0_RenWeb getInstance() throws IOException, SQLException, PropertyVetoException {

        if (datasource == null) {
            datasource = new PoolC3P0_RenWeb();
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
