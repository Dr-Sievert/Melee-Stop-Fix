package net.sievert.melee_stop_fix;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MeleeStopFix implements ModInitializer {

	public static final String MOD_ID = "melee_stop_fix";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Melee Stop Fix Initialized!");
		MeleeStopFixConfig.load();
	}
}