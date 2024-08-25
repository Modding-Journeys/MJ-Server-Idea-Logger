package com.modding_journeys.idea_logger.core.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class IdeaCommand {

    private static final Path IDEA_FILE = Paths.get(FMLPaths.GAMEDIR.get().toString(), "ideas.txt");

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        // Command to add an idea
        LiteralArgumentBuilder<CommandSourceStack> ideaCommand = Commands.literal("idea")
                .then(Commands.argument("ideaText", StringArgumentType.greedyString())
                        .executes(context -> {
                            String idea = StringArgumentType.getString(context, "ideaText");
                            addIdeaToFile(idea);
                            context.getSource().sendSuccess(() -> Component.literal("Idea logged: " + idea), true);
                            return 1;
                        }));

        // Command to list all ideas
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

        // Command to remove a specific idea
        LiteralArgumentBuilder<CommandSourceStack> removeIdeaCommand = Commands.literal("removeidea")
                .then(Commands.argument("ideaText", StringArgumentType.greedyString())
                        .executes(context -> {
                            String idea = StringArgumentType.getString(context, "ideaText");
                            boolean success = removeIdeaFromFile(idea);
                            if (success) {
                                context.getSource().sendSuccess(() -> Component.literal("Idea removed: " + idea), true);
                            } else {
                                context.getSource().sendSuccess(() -> Component.literal("Idea not found: " + idea), false);
                            }
                            return 1;
                        }));

        // Register commands with public visibility (no OP required)
        dispatcher.register(ideaCommand);
        dispatcher.register(ideasCommand);
        dispatcher.register(removeIdeaCommand);
    }

    private static void addIdeaToFile(String idea) {
        try (FileWriter writer = new FileWriter(IDEA_FILE.toFile(), true)) {
            writer.write(idea + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> getIdeasFromFile() {
        List<String> ideas = new ArrayList<>();
        if (Files.exists(IDEA_FILE)) {
            try (BufferedReader reader = new BufferedReader(new FileReader(IDEA_FILE.toFile()))) {
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

    private static boolean removeIdeaFromFile(String idea) {
        List<String> ideas = getIdeasFromFile();
        if (ideas.contains(idea)) {
            ideas.remove(idea);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(IDEA_FILE.toFile()))) {
                for (String remainingIdea : ideas) {
                    writer.write(remainingIdea);
                    writer.newLine();
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
