package com.modding_journeys.idea_logger;

import net.fabricmc.api.ModInitializer;

public class Idea_LoggerFabric implements ModInitializer {

    @Override
    public void onInitialize() {

        Idea_Logger.init();
    }

}
