package net.sievert.melee_stop_fix.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.SwellGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Creeper;
import net.sievert.melee_stop_fix.MeleeStopFixConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SwellGoal.class)
public abstract class SwellGoalMixin {

    @Redirect(
            method = "start()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/ai/navigation/PathNavigation;stop()V"
            )
    )
    private void melee_stop_fix$dontStopCreeperNavigation(PathNavigation navigation) {
    }

    @Shadow @Final private Creeper creeper;

    @Inject(method = "tick()V", at = @At("TAIL"))
    private void melee_stop_fix$keepMovingWhileSwell(CallbackInfo ci) {
        if (!MeleeStopFixConfig.INSTANCE.disableCreeperStopDuringIgnition) {
            return;
        }
        LivingEntity target = this.creeper.getTarget();
        if (target == null || !target.isAlive()) return;
        if (this.creeper.getSwellDir() <= 0) return;
        if (!this.creeper.getSensing().hasLineOfSight(target)) return;
        this.creeper.getNavigation().moveTo(
                target,
                MeleeStopFixConfig.INSTANCE.creeperIgnitedChaseSpeed
        );
    }
}
