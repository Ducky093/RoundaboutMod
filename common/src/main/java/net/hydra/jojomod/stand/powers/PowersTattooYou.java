package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.ThrownWaterBottleEntity;
import net.hydra.jojomod.entity.stand.JusticeEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.SurvivorEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.CooldownInstance;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.SplashPotionItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PowersTattooYou extends NewDashPreset {
    public PowersTattooYou(LivingEntity self) {
        super(self);
    }
    @Override
    /**Override to add disable config*/
    public boolean isStandEnabled(){
        return ClientNetworking.getAppropriateConfig().survivorSettings.enableSurvivor;
    }
    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersTattooYou(entity);
    }



    public boolean canSummonStandAsEntity(){
        return false;
    }
    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        // code for advanced icons
         setSkillIcon(context, x, y, 1, StandIcons.BOTTLE, PowerIndex.SKILL_1);

        if (isHoldingSneak())
            setSkillIcon(context, x, y, 2, StandIcons.DESPAWN, PowerIndex.NO_CD);
        else
            setSkillIcon(context, x, y, 2, StandIcons.SPAWN, PowerIndex.SKILL_2);
        setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);

        if (getCreative() || !ClientNetworking.getAppropriateConfig().survivorSettings.canonSurvivorHasNoRageCupid) {
            if (angerSelectionMode())
                setSkillIcon(context, x, y, 4, StandIcons.CUPID_ON, PowerIndex.SKILL_4);
            else
                setSkillIcon(context, x, y, 4, StandIcons.RAGE_SELECTION, PowerIndex.SKILL_4);
        }

        super.renderIcons(context, x, y);
    }


    @Override
    public void onPowerSwitch(){
      
    }

    public StandEntity getStandForHUDIfFake(){
        if (displayStand == null){
            displayStand = null
        }

        return displayStand;
    }

    public Component getPosName(byte posID){
        return Component.empty();
    }
    public List<Byte> getPosList(){
        List<Byte> $$1 = Lists.newArrayList();
        return $$1;
    }

    @Override









    @Override
    public void powerActivate(PowerContext context) {
        /**Making dash usable on both key presses*/
        switch (context)
        {
            case SKILL_1_NORMAL-> {
                summonCloneClient();
            }
            case SKILL_1_CROUCH-> {
              commandCloneClient
            )
            case SKILL_2_NORMAL, SKILL_2_CROUCH -> {
                swapWithCloneClient();
            }
            case SKILL_3_NORMAL, SKILL_3_CROUCH -> {
                dash();
            }
            case SKILL_4_NORMAL, SKILL_4_CROUCH -> {
                callAllClonesBackClient();
            }
        }
    }

    public void switchModeClient(){
        if (getCreative() || !ClientNetworking.getAppropriateConfig().survivorSettings.canonSurvivorHasNoRageCupid) {
            SurvivorTarget = null;
            EntityTargetOne = null;
            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_4, true);
            tryPowerPacket(PowerIndex.POWER_4);
        }
    }

    public void throwBottleClient(){
        if (!this.onCooldown(PowerIndex.SKILL_1)) {
            if (canUseWaterBottleThrow()) {
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1, true);
                tryPowerPacket(PowerIndex.POWER_1);
            }
        }
    }

    public void throwBottleActually(ItemStack stack){

        playSoundIfPossible(self.level(),
                null,
                this.self.getX(),
                this.self.getY(),
                this.self.getZ(),
                SoundEvents.SPLASH_POTION_THROW,
                SoundSource.PLAYERS,
                0.5F,
                0.4F / (this.self.getRandom().nextFloat() * 0.4F + 0.8F)
        );
        ThrownWaterBottleEntity $$4 = new ThrownWaterBottleEntity(this.self.level(), this.self);
        $$4.setItem(stack);
        $$4.shootFromRotation(this.self, this.self.getXRot(), this.self.getYRot(), -0.1F, 1.5F, 0.2F);
        this.self.level().addFreshEntity($$4);
    }

    public boolean throwWaterBottle(){
        int cooldown = 5;
        this.setCooldown(PowerIndex.SKILL_1, cooldown);
        if (!this.self.level().isClientSide() && this.self instanceof Player PL){
            ItemStack stack = this.getSelf().getMainHandItem();
            if ((!stack.isEmpty() && stack.getItem() instanceof PotionItem PI && PotionUtils.getPotion(stack) == Potions.WATER)
            && !(stack.getItem() instanceof SplashPotionItem )) {
                throwBottleActually(stack.copy());
                if (!PL.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                return true;
            }
            ItemStack stack2 = this.getSelf().getOffhandItem();
            if ((!stack2.isEmpty() && stack2.getItem() instanceof PotionItem PI && PotionUtils.getPotion(stack2) == Potions.WATER)
                    && !(stack2.getItem() instanceof SplashPotionItem )) {
                throwBottleActually(stack2.copy());
                if (!PL.getAbilities().instabuild) {
                    stack2.shrink(1);
                }
                return true;
            }
        }
        return true;
    }

    @Override
    public boolean tryTripleIntPower(int move, boolean forced, int chargeTime, int move2, int move3){
        switch (move)
        {
            case PowerIndex.POWER_4_BONUS -> {
                initializeTargets(chargeTime,move2, move3);
            }
        }
        return tryPower(move, forced);
    }

    public void initializeTargets(int x, int y, int z){


        Entity targ = this.self.level().getEntity(x);
        if (targ instanceof SurvivorEntity SE){
            SurvivorTarget = SE;
        }
        EntityTargetOne = this.self.level().getEntity(y);
        EntityTargetTwo = this.self.level().getEntity(z);
    }


    @Override
    public boolean highlightsEntity(Entity ent,Player player){
        if (ent != null) {
            if (angerSelectionMode()) {
                if (
                        (SurvivorTarget != null  && ent.is(SurvivorTarget)) ||
                                (EntityTargetOne != null && ent.is(EntityTargetOne))
                ) {
                    return true;
                }

                Entity highlights = getHighlighter();
                if (highlights != null && highlights.is(ent)){
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public int highlightsEntityColor(Entity ent, Player player){
        if (
                (SurvivorTarget != null && ent != null && ent.is(SurvivorTarget)) ||
                        (EntityTargetOne != null && ent != null && ent.is(EntityTargetOne))
        ){
            return 4971295;
        }
        return 11283968;
    }

    @Override
    public boolean returnFakeStandForHud(){
        return true;
    }
    public SurvivorEntity SurvivorTarget = null;
    public Entity EntityTargetOne = null;
    public Entity EntityTargetTwo = null;
    public boolean selectTarget(){
        setRageCupidCooldown();
        unloadTargets();
        SurvivorEntity surv = SurvivorTarget;
        if (surv != null && EntityTargetOne instanceof LivingEntity LE && EntityTargetTwo instanceof LivingEntity LE2){
            surv.matchEntities(LE,LE2);
        }
        return true;
    }

    public boolean canUseStillStandingRecharge(byte bt){
        if (bt == PowerIndex.SKILL_2)
            return false;
        return super.canUseStillStandingRecharge(bt);
    }

  

    public int lastPlacementTime = -1;

 
  
    @Override
    public int getDisplayPowerInventoryScale(){
        return 60;
    }
    @Override
    public int getDisplayPowerInventoryYOffset(){
        return 7;
    }
 

    @Override
    public boolean isSecondaryStand(){
        return false;
    }
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }


 
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        if (slot == 1 && !canUseWaterBottleThrow()){
            return true;
        }

        return super.isAttackIneptVisually(activeP,slot);
    }

    public static final byte
            PLACE = 71,
            RETRACT = 72,
            SHOCK = 73;
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypass) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 80, 0, "ability.roundabout.throw_bottle",
                "instruction.roundabout.press_skill", StandIcons.BOTTLE, 1, level, bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 99, 0, "ability.roundabout.summon_survivor",
                "instruction.roundabout.press_skill", StandIcons.SPAWN,2,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 118, 0, "ability.roundabout.desummon_survivor",
                "instruction.roundabout.press_skill_crouch", StandIcons.DESPAWN,2,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 80, 0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypass));
        if (getCreative() || !ClientNetworking.getAppropriateConfig().survivorSettings.canonSurvivorHasNoRageCupid) {
            $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 99, 0, "ability.roundabout.target_zap",
                    "instruction.roundabout.press_skill", StandIcons.RAGE_SELECTION, 4, level, bypass));
        }
        return $$1;
    }

    public boolean isWip(){
        return true;
    }
    public Component ifWipListDevStatus(){
        return Component.translatable(  "roundabout.dev_status.active").withStyle(ChatFormatting.AQUA);
    }
    public Component ifWipListDev(){
        return Component.literal(  "A Duck").withStyle(ChatFormatting.GOLD);
    }
}


    boolean holdAttack = false;
    public void buttonInputAttack(boolean keyIsDown, Options options) {
        if (keyIsDown) {
            if (!holdAttack) {
                holdAttack = true;
                if (angerSelectionMode()) {
                    selectTargetClient();
                }
            }
        } else if (holdAttack){
            holdAttack = false;
        }
    }
}
