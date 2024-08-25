package com.modding_journeys.idea_logger.core.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class IdeaCommand {

    private static final String IDEA_FILE = "ideas.txt";

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> ideaCommand = Commands.literal("idea")
                .then(Commands.argument("ideaText", StringArgumentType.greedyString())
                        .executes(context -> {
                            String idea = StringArgumentType.getString(context, "ideaText");
                            addIdeaToFile(idea);
                            context.getSource().sendSuccess(() -> Component.literal("Idea logged: " + idea), true);
                            return 1;
                        }));

        LiteralArgumentBuilder<CommandSourceStack> ideasCommand = Commands.literal("ideas")
                .executes(context -> {
                    List<String> ideas = getIdeasFromFile();
                    if (ideas.isEmpty()) {
                        context.getSource().sendSuccess(() -> Component.literal("No ideas logged yet."), false);
                    } else {
                        ideas.forEach(idea -> context.getSource().sendSuccess(() -> Component.literal(idea), false));
                    }
                    return 1;
                });

        dispatcher.register(ideaCommand);
        dispatcher.register(ideasCommand);
    }

    private static void addIdeaToFile(String idea) {
        try (FileWriter writer = new FileWriter(IDEA_FILE, true)) {
            writer.write(idea + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> getIdeasFromFile() {
        Path path = Paths.get(IDEA_FILE);
        List<String> ideas = new ArrayList<>();
        if (Files.exists(path)) {
            try (BufferedReader reader = Files.newBufferedReader(path)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    ideas.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ideas;
    }
}
