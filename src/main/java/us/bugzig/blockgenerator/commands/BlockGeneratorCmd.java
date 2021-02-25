package us.bugzig.blockgenerator.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.bugzig.blockgenerator.BlockGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BlockGeneratorCmd implements CommandExecutor {

    private Connection connection;
    public BlockGeneratorCmd(Connection connection) {

        this.connection = connection;

    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;

        if (args[0].equals("create")) {



        } else if (args[0].equals("add")) {

            String sql = "INSERT INTO blockGenerator(Username, Test) VALUES ('var1', 'var2') ON DUPLICATE KEY UPDATE Test = 'var2';;";
            try {
                sql = sql.replace("var1", player.getDisplayName()).replace("var2", args[1]);
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.executeUpdate();
            } catch (SQLException e) {
                BlockGenerator.pluginInstance().getLogger().info(e.getMessage());
            }
        }

        return true;
    }
}
