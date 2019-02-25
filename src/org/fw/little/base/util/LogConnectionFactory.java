package org.fw.little.base.util;

import java.sql.Connection;
import java.util.Map;

import javax.sql.DataSource;

import com.app.conf.info.Conf;
import com.mchange.v2.c3p0.ComboPooledDataSource;




public class LogConnectionFactory {
	// 单例模式保证多线程安全
    private static interface Singleton {
        final LogConnectionFactory INSTANCE = new LogConnectionFactory();
    }

    private DataSource dataSource = null;

    private LogConnectionFactory() {
    	Map<String, String> dbMap = Conf.getDbs().get(0);

        try {
        	ComboPooledDataSource source = new ComboPooledDataSource();
        	source.setDriverClass(dbMap.get("className"));
        	source.setJdbcUrl(dbMap.get("jdbcUrl"));
        	source.setUser(dbMap.get("userName"));
        	source.setPassword(dbMap.get("password"));
        	source.setMaxPoolSize(15);
        	source.setMinPoolSize(10);
        	source.setMaxIdleTime(9);
        	source.setIdleConnectionTestPeriod(60);
            this.dataSource = source;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getDatabaseConnection() throws Exception {
        return Singleton.INSTANCE.dataSource.getConnection();
    }
}
