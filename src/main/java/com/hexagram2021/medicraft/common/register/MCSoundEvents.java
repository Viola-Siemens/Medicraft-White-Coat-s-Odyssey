package com.hexagram2021.medicraft.common.register;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static com.hexagram2021.medicraft.Medicraft.MODID;

public final class MCSoundEvents {
	private static final Map<ResourceLocation, SoundEvent> registeredEvents = new HashMap<>();

	public static final class AED {
		public static final SoundEvent SINGLE_BLEEP = registerSound("block.medicraft.aed.single_bleep");
		public static final SoundEvent LONG_BEEP = registerSound("block.medicraft.aed.long_beep");
		public static final SoundEvent SHOCK_ADVISED = registerSound("block.medicraft.aed.shock_advised");
		public static final SoundEvent NO_SHOCK_ADVISED = registerSound("block.medicraft.aed.no_shock_advised");
		public static final SoundEvent DONT_TOUCH_PATIENT = registerSound("block.medicraft.aed.dont_touch_patient");
		public static final SoundEvent CHARGE_AND_SHOCK = registerSound("block.medicraft.aed.charge_and_shock");
		public static final SoundEvent PERFORM_CPR = registerSound("block.medicraft.aed.perform_cpr");
		public static final SoundEvent PUSH_HARDER = registerSound("block.medicraft.aed.push_harder");
		public static final SoundEvent GOOD_COMPRESSIONS = registerSound("block.medicraft.aed.good_compressions");

		private static void init() {
		}
	}

	private static SoundEvent registerSound(String name) {
		ResourceLocation location = ResourceLocation.fromNamespaceAndPath(MODID, name);
		SoundEvent event = SoundEvent.createVariableRangeEvent(location);
		registeredEvents.put(location, event);
		return event;
	}

	public static void init(BiConsumer<ResourceLocation, SoundEvent> consumer) {
		AED.init();
		registeredEvents.forEach(consumer);
	}
}
