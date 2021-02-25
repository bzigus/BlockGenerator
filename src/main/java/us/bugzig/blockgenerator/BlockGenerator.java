package us.bugzig.blockgenerator;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import us.bugzig.blockgenerator.commands.BlockGeneratorCmd;
import us.bugzig.blockgenerator.util.Language;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

public class BlockGenerator extends JavaPlugin {

    private static BlockGenerator pluginInstance;

    public static Plugin pluginInstance() {
        return pluginInstance;
    }

    static Connection connection;

    @Override
    public void onEnable() {

        BlockGenerator.pluginInstance = this;

        connection = setupMySQL();

        setupDB();
        //Commands
        this.getCommand("blockgenerator").setExecutor(new BlockGeneratorCmd(connection));



        //Setup config
        this.saveDefaultConfig();

        //Set up language file
        Language lang = new Language();
        updateConfigs();
        lang.saveDefault();


    }

    @Override
    public void onDisable() {

        try { //using a try catch to catch connection errors (like wrong sql password...)
            if (connection!=null && !connection.isClosed()){ //checking if connection isn't null to
                //avoid receiving a nullpointer
                connection.close(); //closing the connection field variable.
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    private void updateConfigs() {

        File f = new File(pluginInstance().getDataFolder() + File.separator +"lang.yml");
        FileConfiguration langFile = YamlConfiguration.loadConfiguration(f);
        InputStream inputStream = getClass().getResourceAsStream("/lang.yml");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        FileConfiguration yaml = YamlConfiguration.loadConfiguration(reader);


        updateKeys(yaml, langFile);

        try {
            langFile.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void updateKeys(FileConfiguration jarYaml, FileConfiguration currentYaml) {

        ConfigurationSection currentConfigurationSection = currentYaml.getConfigurationSection(""),
                latestConfigurationSection = jarYaml.getConfigurationSection("");
        if (currentConfigurationSection != null && latestConfigurationSection != null) {

            Set<String> newKeys = latestConfigurationSection.getKeys(true),
                    currentKeys = currentConfigurationSection.getKeys(true);
            for (String updatedKey : newKeys) {
                if (!currentKeys.contains(updatedKey)) {
                    currentYaml.set(updatedKey, jarYaml.get(updatedKey));

                }

            }

            for (String currentKey : currentKeys) {

                if (!newKeys.contains(currentKey)) {
                    currentYaml.set(currentKey, null);
                }

            }

        }

    }

    private Connection setupMySQL () {

        final String username = getConfig().getString("MySQL.username"); //Enter in your db username
        final String password = getConfig().getString("MySQL.password"); //Enter your password for the db
        final String url = "jdbc:mysql://"+ getConfig().getString("MySQL.host") +":" + getConfig().get("MySQL.port") + "/" + getConfig().getString("MySQL.database"); //Enter URL w/db name

        try { //We use a try catch to avoid errors, hopefully we don't get any.
            Class.forName("com.mysql.jdbc.Driver"); //this accesses Driver in jdbc.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            getLogger().info(e.getMessage());
            return null;
        }

        try { //Another try catch to get any SQL errors (for example connections errors)
            connection = DriverManager.getConnection(url,username,password);
            //with the method getConnection() from DriverManager, we're trying to set
            //the connection's url, username, password to the variables we made earlier and
            //trying to get a connection at the same time. JDBC allows us to do this.
        } catch (SQLException e) { //catching errors)
            getLogger().info(e.getMessage()); //prints out SQLException errors to the console (if any)
        }

        return connection;

    }

    private void setupDB() {

        String sql = "CREATE TABLE IF NOT EXISTS blockGenerator(Username varchar(20) UNIQUE, Test varchar(64));";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.executeUpdate();
        } catch (SQLException e) {
            getLogger().info(e.getMessage());
        }

    }

}
