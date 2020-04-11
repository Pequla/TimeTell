package timetell;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import net.kronos.rkon.core.Rcon;
import net.kronos.rkon.core.ex.AuthenticationException;

/**
 *
 * @author Petar Kresoja
 */
public class TimeTell {

    private static String host;
    private static String password;
    private static int port;

    public static void main(String[] args) {
        System.out.println("TimeTell started...");
        initialize();
        try {
            Rcon rcon = new Rcon(host, port, password.getBytes());
            rcon.command("tellraw @a [\"\",{\"text\":\"[TimeTell] \",\"bold\":true,\"color\":\"yellow\"},{\"text\":\"Current time: " + getDate() + "\"}]");
            rcon.disconnect();
        } catch (IOException ex) {
            System.err.println("An error occured while connecting to the server...");
            ex.printStackTrace(System.err);
        } catch (AuthenticationException ex) {
            System.err.println("An error occured while trying to login...");
            ex.printStackTrace(System.err);
        }
    }

    private static void initialize() {
        try {
            File config = new File("config.properties");
            Properties prop = new Properties();
            if (!config.exists()) {
                try (OutputStream output = new FileOutputStream(config)) {
                    prop.setProperty("rcon.host", "localhost");
                    prop.setProperty("rcon.port", "25575");
                    prop.setProperty("rcon.password", "password");
                    prop.store(output, "TimeTell configuration file." + System.lineSeparator() + "Created by: Pequla ( https://pequla.github.io/ )");
                }
            }
            try (InputStream input = new FileInputStream(config)) {
                prop.load(input);
                host = prop.getProperty("rcon.host");
                port = Integer.valueOf(prop.getProperty("rcon.port"));
                password = prop.getProperty("rcon.password");
            }
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
    }

    private static String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
