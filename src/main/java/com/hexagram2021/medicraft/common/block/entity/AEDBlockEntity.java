package com.hexagram2021.medicraft.common.block.entity;

import com.hexagram2021.medicraft.common.register.MCBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.EnergyStorage;

public class AEDBlockEntity extends BlockEntity {
	public final EnergyStorage energyStorage = new EnergyStorage(10000);
	private State state = State.NORMAL;

	public AEDBlockEntity(BlockPos pos, BlockState blockState) {
		this(MCBlockEntities.AED.get(), pos, blockState);
	}
	public AEDBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
	}

	public static void clientTick(Level level, BlockPos pos, BlockState state, AEDBlockEntity blockEntity) {

	}
	public static void serverTick(Level level, BlockPos pos, BlockState state, AEDBlockEntity blockEntity) {

	}

	public int getAnalogOutputSignal() {
		return this.state.signal();
	}

	public enum State {
		NORMAL(1),		// Sinus, maybe even ST-T abnormalities, arrhythmia etc.
		ABNORMAL(3),		// Nonsinus but not serious, eg. A-Fib, SV-tach.
		URGENT(5),		// Really serious, eg. V-Fib, V-tach
		ARRESTED(7);		// RIP.

		private final int signal;

		State(int signal) {
			this.signal = signal;
		}

		public int signal() {
			return this.signal;
		}
	}
}
