package com.modding_journeys.examplemod.core.tab;

import com.modding_journeys.examplemod.core.registry.ModRegistryForge;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabsForge {

    public static void register() {}

    public static final RegistryObject<CreativeModeTab> EXAMPLE_MOD_TAB = ModRegistryForge.registerCreativeModeTab("example_mod_tab", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(Items.STICK))
            .title(Component.translatable("itemGroup.exampleMod"))
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .displayItems(((pParameters, pOutput) -> {
                pOutput.accept(Items.STICK);
            })).build());
}
