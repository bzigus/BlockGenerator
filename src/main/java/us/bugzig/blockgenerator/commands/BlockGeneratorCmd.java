package us.bugzig.blockgenerator.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.bugzig.blockgenerator.BlockGenerator;
import us.bugzig.blockgenerator.util.Language;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BlockGeneratorCmd implements CommandExecutor {

    Language lang = new Language();

    private Connection connection;
    public BlockGeneratorCmd(Connection connection) {

        this.connection = connection;

    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        if (args.length >= 1) {
            if (args[0].equals("get")) {

                String sql = "SELECT * FROM blockGenerator WHERE Username = 'var1'";
                try {
                    sql = sql.replace("var1", player.getDisplayName());
                    PreparedStatement stmt = connection.prepareStatement(sql);

                    ResultSet results = stmt.executeQuery();

                    while (results.next()) {
                        player.sendMessage(results.getString("Test"));
                    }


                } catch (SQLException e) {

                    BlockGenerator.pluginInstance().getLogger().info(e.getMessage());

                }

            } else if (args[0].equals("add")) {

                String sql = "INSERT INTO blockGenerator(Username, Test) VALUES ('var1', 'var2') ON DUPLICATE KEY UPDATE Test = 'var2';;";
                try {
                    sql = sql.replace("var1", player.getDisplayName()).replace("var2", args[1]);
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    BlockGenerator.pluginInstance().getLogger().info(e.getMessage());
                }
            } else {

                player.sendMessage(lang.readFiles("Commands.invalidArgs"));

            }

        } else {

            player.sendMessage(lang.readFiles("Commands.notEnoughArgs"));

        }

        return true;
    }
}
