package com.hexagram2021.medicraft;

import com.hexagram2021.medicraft.common.MCContent;
import com.hexagram2021.medicraft.server.MedicraftSavedData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

@Mod(Medicraft.MODID)
public class Medicraft {
	public static final String MODID = "medicraft";
	public static final String VERSION = ModList.get().getModFileById(MODID).versionString();

	public Medicraft(IEventBus modBus, ModContainer modContainer) {
		MCContent.modConstruction(modBus);
		modBus.addListener(this::setup);
		NeoForge.EVENT_BUS.addListener(this::serverStarted);
	}

	private void setup(final FMLCommonSetupEvent event) {
	}

	public void serverStarted(ServerStartedEvent event) {
		ServerLevel world = event.getServer().getLevel(Level.OVERWORLD);
		assert world != null;
		if (!world.isClientSide) {
			MedicraftSavedData worldData = world.getDataStorage().computeIfAbsent(
					new SavedData.Factory<>(MedicraftSavedData::new, MedicraftSavedData::new),
					MedicraftSavedData.SAVED_DATA_NAME
			);
			MedicraftSavedData.setInstance(worldData);
		}
	}
}
