package net.sievert.melee_stop_fix.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MeleeAttackGoal.class)
public abstract class MeleeAttackGoalMixin {

	@Final
	@Shadow protected PathfinderMob mob;

	@Shadow @Final private double speedModifier;
	@Shadow @Final private boolean followingTargetEvenIfNotSeen;

	@Redirect(
			method = "stop()V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/ai/navigation/PathNavigation;stop()V"
			)
	)
	private void melee_stop_fix$dontStopNavigation(PathNavigation navigation) {
	}

	@Inject(method = "canContinueToUse()Z", at = @At("RETURN"), cancellable = true)
	private void melee_stop_fix$continueEvenIfNavDone(CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue()) return;
		LivingEntity target = this.mob.getTarget();
		if (target == null || !target.isAlive()) return;
		if (target instanceof Player p && (p.isCreative() || p.isSpectator())) return;
		cir.setReturnValue(true);
	}

	@Inject(method = "tick()V", at = @At("HEAD"))
	private void melee_stop_fix$alwaysChaseTarget(CallbackInfo ci) {
		LivingEntity target = this.mob.getTarget();
		if (target == null || !target.isAlive()) return;
		if (!this.followingTargetEvenIfNotSeen && !this.mob.getSensing().hasLineOfSight(target)) return;
		this.mob.getNavigation().moveTo(target, this.speedModifier);
	}
}
