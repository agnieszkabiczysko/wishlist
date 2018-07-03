package agnieszka.wishlist.wrapper;

import static ru.yandex.qatools.embed.postgresql.distribution.Version.V9_6_6;

import java.io.IOException;
import java.net.ServerSocket;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;

@Service()
public class PostgresWrapper {

	private static final Logger LOGGER = LoggerFactory.getLogger(PostgresWrapper.class);
	
    private static EmbeddedPostgres embeddedPostgres;
    private static String connectionUrl;

    /**
     * Start PostgreSQL running
     * @throws IOException if an error occurs starting PostgreSQL
     */
    @PostConstruct
    public void start() throws IOException {
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
        //ServerSocket s = new ServerSocket(0);
        //return s.getLocalPort();
        try (ServerSocket s = new ServerSocket(0)) {
        	return s.getLocalPort();
        }
    }
}
