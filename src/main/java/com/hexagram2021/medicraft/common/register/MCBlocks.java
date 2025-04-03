package com.hexagram2021.medicraft.common.register;

import com.google.common.collect.ImmutableMap;
import com.hexagram2021.medicraft.common.block.AEDBlock;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Map;

import static com.hexagram2021.medicraft.Medicraft.MODID;

public final class MCBlocks {
	private static final DeferredRegister<Block> REGISTER = DeferredRegister.create(Registries.BLOCK, MODID);

	public static final Map<DyeColor, DeferredHolder<Block, AEDBlock>> COLORED_AEDS = Util.make(() -> {
		ImmutableMap.Builder<DyeColor, DeferredHolder<Block, AEDBlock>> builder = ImmutableMap.builder();
		for (DyeColor dyeColor : DyeColor.values()) {
			builder.put(dyeColor, REGISTER.register(
					dyeColor.getName() + "_automated_external_defibrillator",
					() -> new AEDBlock(BlockBehaviour.Properties.of().mapColor(dyeColor))
			));
		}
		return builder.build();
	});

	private MCBlocks() {
	}

	public static void init(IEventBus bus) {

	}
}
