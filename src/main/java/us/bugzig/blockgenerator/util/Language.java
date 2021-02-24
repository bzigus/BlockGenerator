package us.bugzig.blockgenerator.util;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import us.bugzig.blockgenerator.BlockGenerator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Language {
    private Map<String, String> langMap = new HashMap<String, String>();
    Plugin plugin = BlockGenerator.pluginInstance();

    File directory = new File(plugin.getDataFolder().getPath());
    File f = new File(directory + File.separator +"lang.yml");

    String[] langOptions;

    public void saveDefault(){

        if (!f.exists()) {
            FileConfiguration langFile = YamlConfiguration.loadConfiguration(f);
            plugin.saveResource("lang.yml", false);

        }

    }

    public String readFiles(String name) {

        FileConfiguration langFile = YamlConfiguration.loadConfiguration(f);

        String lang = langFile.getString(name);
        lang = lang.replaceAll("<prefix>", langFile.getString("prefix"));

        lang = ChatColor.translateAlternateColorCodes('&', lang);


        return lang;
    }

    public void save() {

        FileConfiguration langFile = YamlConfiguration.loadConfiguration(f);
        try {
            langFile.save(f);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Couldn't save file");
        }

    }

}
