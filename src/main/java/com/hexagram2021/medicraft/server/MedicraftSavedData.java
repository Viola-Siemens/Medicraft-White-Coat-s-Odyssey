package com.hexagram2021.medicraft.server;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.saveddata.SavedData;

import javax.annotation.Nullable;
import java.util.Objects;

public class MedicraftSavedData extends SavedData {
	@Nullable
	private static MedicraftSavedData INSTANCE;

	public static final String SAVED_DATA_NAME = "Medicraft-SavedData";
	private static final String TAG_ELECTROCARDIOGRAM_ID_COUNT = "idcounts";

	private int electrocardiogramIdCount = 0;

	public MedicraftSavedData() {
	}

	public MedicraftSavedData(CompoundTag nbt, HolderLookup.Provider registries) {
		if(nbt.contains(TAG_ELECTROCARDIOGRAM_ID_COUNT, Tag.TAG_INT)) {
			this.electrocardiogramIdCount = nbt.getInt(TAG_ELECTROCARDIOGRAM_ID_COUNT);
		}
	}

	public int getNextFreeElectrocardiogramId() {
		int ret = this.electrocardiogramIdCount;
		this.electrocardiogramIdCount += 1;
		this.setDirty();
		return ret;
	}

	public static int getNextFreeElectrocardiogramIdOrThrow() {
		return Objects.requireNonNull(INSTANCE).getNextFreeElectrocardiogramId();
	}

	@Override
	public CompoundTag save(CompoundTag nbt, HolderLookup.Provider registries) {
		nbt.putInt(TAG_ELECTROCARDIOGRAM_ID_COUNT, this.electrocardiogramIdCount);
		return nbt;
	}

	public static void setInstance(MedicraftSavedData in) {
		INSTANCE = in;
	}
}
