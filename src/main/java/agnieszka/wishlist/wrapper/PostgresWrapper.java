package agnieszka.wishlist.wrapper;

import static java.util.Arrays.asList;
import static ru.yandex.qatools.embed.postgresql.distribution.Version.V9_6_6;

import java.io.IOException;
import java.net.ServerSocket;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;

@Service
public class PostgresWrapper {

	private static final Logger LOGGER = LoggerFactory.getLogger(PostgresWrapper.class);
	
    private static EmbeddedPostgres embeddedPostgres;
    private static String connectionUrl;
    
    @Autowired
    private Environment env;

    /**
     * Start PostgreSQL running
     * @throws IOException if an error occurs starting PostgreSQL
     */
    @PostConstruct
    public void start() throws IOException {
    	if (!isStaging()) {
    		return;
    	}
    	
        if (embeddedPostgres == null) {
            int port = getFreePort();
            LOGGER.info("Initialization database on port {}", port);
            
            embeddedPostgres = new EmbeddedPostgres(V9_6_6);
            connectionUrl = embeddedPostgres.start("localhost", port, "dbName", "userName", "password");
        }
    }

    /**
     * Stop PostgreSQL
     */
    @PreDestroy
    public void stop() {
    	if (!isStaging()) {
    		return;
    	}
    	
        if (embeddedPostgres != null) {
            embeddedPostgres.stop();
            embeddedPostgres = null;
        }
    }

    /**
     * Get the URL to use to connect to the database
     * @return the connection URL
     */
    public String getConnectionUrl() {
        return connectionUrl;
    }

    /**
     * Get a free port to listen on
     * @return the port
     * @throws IOException if an error occurs finding a port
     */
    private static int getFreePort() throws IOException {
        try (ServerSocket s = new ServerSocket(0)) {
        	return s.getLocalPort();
        }
    }
    
    private boolean isStaging() {
    	return asList(env.getActiveProfiles()).contains("staging");
    }
}
