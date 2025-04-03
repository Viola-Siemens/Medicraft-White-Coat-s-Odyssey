package com.hexagram2021.medicraft.common.register;

import com.google.common.collect.ImmutableSet;
import com.hexagram2021.medicraft.common.block.entity.AEDBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.hexagram2021.medicraft.Medicraft.MODID;

@SuppressWarnings("DataFlowIssue")
public final class MCBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AEDBlockEntity>> AED = REGISTER.register(
			"automated_external_defibrillator", () -> new BlockEntityType<>(
					AEDBlockEntity::new,
					MCBlocks.COLORED_AEDS.values().stream().map(DeferredHolder::get).collect(ImmutableSet.toImmutableSet()),
					null
			)
	);

	private MCBlockEntities() {
	}

	public static void init(IEventBus bus) {
		REGISTER.register(bus);
	}
}
