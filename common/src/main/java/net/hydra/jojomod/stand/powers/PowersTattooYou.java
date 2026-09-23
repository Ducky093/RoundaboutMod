package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.ThrownWaterBottleEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
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
import net.hydra.jojomod.entity.visages.CloneEntity;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.entity.EquipmentSlot;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.visages.CloneEntity;
import net.hydra.jojomod.entity.TattooYouCloneEntity;



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


                setSkillIcon(context, x, y, 4, StandIcons.CUPID_ON, PowerIndex.SKILL_4);
                setSkillIcon(context, x, y, 4, StandIcons.RAGE_SELECTION, PowerIndex.SKILL_4);
        super.renderIcons(context, x, y);
    }



    public StandEntity getStandForHUDIfFake(){
        if (displayStand == null){
            displayStand = null;
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
    public void powerActivate(PowerContext context) {
        /**Making dash usable on both key presses*/
        switch (context)
        {
            case SKILL_1_NORMAL-> {
                summonCloneClient();
            }
            case SKILL_1_CROUCH-> {
              desummonCloneClient();
            }
            case SKILL_2_NORMAL, SKILL_2_CROUCH -> {
                swapWithCloneClient();
            }
            case SKILL_3_NORMAL, SKILL_3_CROUCH -> {
                dash();
            }
            case SKILL_4_NORMAL, SKILL_4_CROUCH -> {
                commandCloneClient();
            }
        }
    }

public void summonCloneClient() {
            ServerLevel level;
            Player original;
            Vec3 spawnPos;
     {
        Entity copyEntity = ModEntities.TATTOO_YOU_CLONE.create(this.getSelf().level());

        if (!(copyEntity instanceof TattooYouCloneEntity copy)) {
            return false;
        }


        // Name
        if (original.hasCustomName()) {
            copy.setCustomName(original.getCustomName());
            copy.setCustomNameVisible(original.isCustomNameVisible());
        }


        if (original.getUUID().equals(self.getUUID())){
            copy.safeCopy = true;
        }
        copy.setPlayer(original);
        ((IMob)copy).roundabout$setFate(((IPlayerEntity)original).roundabout$getFate());
        copy.setItemSlot(EquipmentSlot.HEAD, original.getItemBySlot(EquipmentSlot.HEAD).copy());
        copy.setItemSlot(EquipmentSlot.CHEST, original.getItemBySlot(EquipmentSlot.CHEST).copy());
        copy.setItemSlot(EquipmentSlot.LEGS, original.getItemBySlot(EquipmentSlot.LEGS).copy());
        copy.setItemSlot(EquipmentSlot.FEET, original.getItemBySlot(EquipmentSlot.FEET).copy());
        copy.setItemSlot(EquipmentSlot.MAINHAND, original.getMainHandItem().copy());
        copy.setItemSlot(EquipmentSlot.OFFHAND, original.getOffhandItem().copy());
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            copy.setDropChance(slot, 0.0F);
            }    
        }
}
    public void commandCloneClient() {
                ClientUtil.setTattooYouTacticsScreen();
            return;
     }
    public void swapWithCloneClient() {
        
    }
    public void desummonCloneClient() {
        )
  
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



    boolean holdAttack = false;
    public void buttonInputAttack(boolean keyIsDown, Options options) {
        if (keyIsDown) {
            if (!holdAttack) {
                holdAttack = true;
            }
        } else if (holdAttack){
            holdAttack = false;
        }
    }
}

