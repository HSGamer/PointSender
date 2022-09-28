package me.hsgamer.pointsender;

import me.hsgamer.hscore.bukkit.channel.BungeeUtils;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.Collections;

public class SendCommand extends Command {
    private final Permission permission;
    private final PointSender plugin;

    public SendCommand(PointSender plugin) {
        super("pointsender", "Send points to another server", "/pointsender <player> <server> <amount>", Collections.emptyList());
        this.plugin = plugin;
        this.permission = new Permission("pointsender.send", PermissionDefault.OP);
        Bukkit.getPluginManager().addPermission(permission);
        plugin.addDisableFunction(() -> Bukkit.getPluginManager().removePermission(permission));
        setPermission(permission.getName());
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) {
            return false;
        }
        if (args.length != 3) {
            MessageUtils.sendMessage(sender, "&cInvalid arguments");
            return false;
        }

        String playerName = args[0];
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            MessageUtils.sendMessage(sender, "&cPlayer not found or offline");
            return false;
        }

        String server = args[1];

        int amount;
        try {
            amount = Integer.parseInt(args[2]);
            if (amount <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            MessageUtils.sendMessage(sender, "&cInvalid amount");
            return false;
        }

        BungeeUtils.forward(plugin, player, server, "PointReceiver", BungeeUtils.getDataBytes(outputStream -> {
            String serverName = MainConfig.SERVER_NAME.getValue();
            String data = String.join("|", serverName, playerName, Integer.toString(amount));
            outputStream.writeUTF(data);
        }));
        MessageUtils.sendMessage(sender, "&aSent &e" + amount + " &apoints to &e" + playerName + " &aon &e" + server);
        return true;
    }
}
