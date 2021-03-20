package com.celeste;

import com.celeste.annotation.Command;
import lombok.SneakyThrows;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import me.saiintbrisson.minecraft.command.message.MessageHolder;
import me.saiintbrisson.minecraft.command.message.MessageType;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.celeste.menu.MenuListener;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import java.util.Arrays;

public abstract class ServerPlugin extends JavaPlugin {

    /**
     * Registers all listeners used as parameters
     *
     * @param listeners Listener classes
     */
    public void registerListeners(@NotNull Listener... listeners) {
        final PluginManager manager = getServer().getPluginManager();

        Arrays.stream(listeners).forEach(listener -> manager.registerEvents(listener, this));

        /*
        Registers the listener for the MenuAPI
         */
        manager.registerEvents(new MenuListener(), this);
    }

    /**
     * Starts the BukkitFrame and MessageHolder
     * With the default messages
     */
    public void startCommandManager(@NotNull final String language) {
        final BukkitFrame frame = new BukkitFrame(this);
        final MessageHolder messageHolder = frame.getMessageHolder();

        switch(language) {
            case "br": {
                messageHolder.setMessage(MessageType.ERROR, "§cUm erro ocorreu");
                messageHolder.setMessage(MessageType.INCORRECT_TARGET, "§cApenas jogadores podem executar esse comando.");
                messageHolder.setMessage(MessageType.INCORRECT_USAGE, "§cErro! Utilize: /{usage}");
                messageHolder.setMessage(MessageType.NO_PERMISSION, "§cVocê não tem permissão para isso.");
            }
            case "eua": {
                messageHolder.setMessage(MessageType.ERROR, "§cA error occurred.");
                messageHolder.setMessage(MessageType.INCORRECT_TARGET, "§cOnly players can execute this command..");
                messageHolder.setMessage(MessageType.INCORRECT_USAGE, "§cWrong use! The correct is: /{usage}");
                messageHolder.setMessage(MessageType.NO_PERMISSION, "§cYou don't have enough permissions.");
            }
        }

        final Reflections reflections = new Reflections("com.celeste");
        for (Class<?> clazz : reflections.getTypesAnnotatedWith(Command.class)) {
            frame.registerCommands(clazz);
        }
    }

}
