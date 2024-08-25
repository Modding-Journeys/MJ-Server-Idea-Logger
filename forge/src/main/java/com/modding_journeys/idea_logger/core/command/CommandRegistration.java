package com.modding_journeys.idea_logger.core.command;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CommandRegistration {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        IdeaCommand.register(event.getDispatcher());
    }
}
