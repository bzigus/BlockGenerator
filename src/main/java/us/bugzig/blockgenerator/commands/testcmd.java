package us.bugzig.blockgenerator.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.bugzig.blockgenerator.util.Language;

public class testcmd implements CommandExecutor {
    private Language lang = new Language();
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;

        player.sendMessage(lang.readFiles("Commands.also"));

        return true;
    }
}
