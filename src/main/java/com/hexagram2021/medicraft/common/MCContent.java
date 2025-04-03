package com.hexagram2021.medicraft.common;

import com.hexagram2021.medicraft.common.register.MCBlockEntities;
import com.hexagram2021.medicraft.common.register.MCBlocks;
import com.hexagram2021.medicraft.common.register.MCSoundEvents;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import static com.hexagram2021.medicraft.Medicraft.MODID;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD)
public final class MCContent {
	private MCContent() {
	}

	public static void modConstruction(IEventBus modBus) {
		MCBlocks.init(modBus);
		MCBlockEntities.init(modBus);
	}

	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(
				Capabilities.EnergyStorage.BLOCK,
				MCBlockEntities.AED.get(),
				(blockEntity, side) -> blockEntity.energyStorage
		);
	}

	@SubscribeEvent
	public static void registerEntries(RegisterEvent event) {
		event.register(Registries.SOUND_EVENT, helper -> MCSoundEvents.init(helper::register));
	}
}
