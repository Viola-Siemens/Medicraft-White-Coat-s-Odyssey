package com.hexagram2021.medicraft;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(Medicraft.MODID)
public class Medicraft {
	public static final String MODID = "medicraft";
	public static final String VERSION = ModList.get().getModFileById(MODID).versionString();

	public Medicraft(IEventBus modBus, ModContainer modContainer) {
		modBus.addListener(this::setup);
	}

	private void setup(final FMLCommonSetupEvent event) {
	}
}
