package com.celeste;

import me.saiintbrisson.bungee.command.BungeeFrame;
import me.saiintbrisson.minecraft.command.message.MessageHolder;
import me.saiintbrisson.minecraft.command.message.MessageType;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public abstract class BungeePlugin extends Plugin {

    /**
     * Starts the BukkitFrame and MessageHolder
     * With the default messages
     *
     * @param objects Command classes
     */
    public void startCommandManager(final String language, final Object... objects) {
        final BungeeFrame frame = new BungeeFrame(this);
        final MessageHolder messageHolder = frame.getMessageHolder();

        switch (language) {
            case "br": {
                messageHolder.setMessage(MessageType.ERROR, "§cUm erro ocorreu.");
                messageHolder.setMessage(MessageType.INCORRECT_TARGET, "§cApenas jogadores podem executar esse comando.");
                messageHolder.setMessage(MessageType.INCORRECT_USAGE, "§cUso incorreto! Utilize: /{usage}");
                messageHolder.setMessage(MessageType.NO_PERMISSION, "§cVocê não tem permissão para executar esse comando.");
            }
            case "eua": {
                messageHolder.setMessage(MessageType.ERROR, "§cA error occurred.");
                messageHolder.setMessage(MessageType.INCORRECT_TARGET, "§cOnly players can execute this command..");
                messageHolder.setMessage(MessageType.INCORRECT_USAGE, "§cWrong use! The correct is: /{usage}");
                messageHolder.setMessage(MessageType.NO_PERMISSION, "§cYou don't have the permission to use this command!");
            }
        }

        frame.registerCommands(objects);

    }

    public Configuration createConfiguration(final String name) {
        if (!getDataFolder().exists()) getDataFolder().mkdir();

        final File file = new File(getDataFolder(), name);

        try (InputStream inputStream = getResourceAsStream(name)) {
            if (!file.exists()) {
                Files.copy(inputStream, file.toPath());
                return ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            }

            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    /**
     * Registers all listeners used as parameters
     *
     * @param listeners Listener classes
     */
    public void registerListeners(Listener... listeners) {
        final PluginManager manager = getProxy().getPluginManager();

        for (Listener listener : listeners) {
            manager.registerListener(this, listener);
        }
    }

}
