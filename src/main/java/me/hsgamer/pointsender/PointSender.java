package me.hsgamer.pointsender;

import me.hsgamer.hscore.bukkit.baseplugin.BasePlugin;
import me.hsgamer.hscore.bukkit.channel.BungeeUtils;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;

public final class PointSender extends BasePlugin {
    private final MainConfig mainConfig = new MainConfig(this);

    @Override
    public void load() {
        mainConfig.setup();
        MessageUtils.setPrefix("&8[&6PointSender&8] &r");
    }

    @Override
    public void enable() {
        BungeeUtils.register(this);
        registerCommand(new SendCommand(this));
    }

    @Override
    public void disable() {
        BungeeUtils.unregister(this);
    }
}
