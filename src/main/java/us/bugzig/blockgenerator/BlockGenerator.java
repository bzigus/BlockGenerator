package us.bugzig.blockgenerator;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import us.bugzig.blockgenerator.commands.testcmd;
import us.bugzig.blockgenerator.util.Language;

import java.io.*;
import java.util.Set;

public class BlockGenerator extends JavaPlugin {

    private static BlockGenerator pluginInstance;

    public static Plugin pluginInstance() {
        return pluginInstance;
    }

    @Override
    public void onEnable() {

        BlockGenerator.pluginInstance = this;

        //Commands
        this.getCommand("testcmd").setExecutor(new testcmd());

        //Setup config
        FileConfiguration config = this.getConfig();
        this.saveDefaultConfig();

        config.options().copyDefaults(true);
        saveConfig();


        //Set up language file
        Language lang = new Language();
        updateConfigs();
        lang.saveDefault();


    }

    @Override
    public void onDisable() {



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

}
