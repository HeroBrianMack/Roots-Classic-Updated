package elucent.rootsclassic.event;

import elucent.rootsclassic.Const;
import elucent.rootsclassic.capability.RootsCapabilityManager;
import elucent.rootsclassic.registry.RootsRegistry;
import elucent.rootsclassic.util.RootsUtil;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ComponentSpellsEvent {

  public static int TICKS_PER_MANA_REGEN = 5;

  @SubscribeEvent
  public void onLivingTick(LivingUpdateEvent event) {
    LivingEntity entity = event.getEntityLiving();
    if (entity instanceof PlayerEntity) {
      // armor regen if full set
      this.wildwoodArmorRegenFullset((PlayerEntity) entity);
      tickManaRegen(entity);
    }
    tickSkipMovementCurse(event, entity);
  }

  private void tickSkipMovementCurse(LivingUpdateEvent event, LivingEntity entity) {
    CompoundNBT persistentData = entity.getPersistentData();
    if (persistentData.contains(Const.NBT_TRACK_TICKS) && persistentData.contains(Const.NBT_SKIP_TICKS)) {
      int skipTicks = persistentData.getInt(Const.NBT_SKIP_TICKS);
      if (skipTicks > 0) {
        persistentData.putInt(Const.NBT_SKIP_TICKS, skipTicks - 1);
        if (skipTicks <= 0) {
          persistentData.remove(Const.NBT_SKIP_TICKS);
          RootsUtil.decrementTickTracking(entity);
        }
        event.setCanceled(true);
      }
    }
  }

  private void tickManaRegen(LivingEntity entity) {
    if (entity.tickCount % TICKS_PER_MANA_REGEN == 0) {
      entity.getCapability(RootsCapabilityManager.MANA_CAPABILITY).ifPresent(cap -> cap.setMana(cap.getMana() + 1.0f));
    }
  }

  private void wildwoodArmorRegenFullset(PlayerEntity entity) {
    ItemStack head = entity.getItemBySlot(EquipmentSlotType.HEAD);
    ItemStack chest = entity.getItemBySlot(EquipmentSlotType.CHEST);
    ItemStack legs = entity.getItemBySlot(EquipmentSlotType.LEGS);
    ItemStack feet = entity.getItemBySlot(EquipmentSlotType.FEET);
    if (head.getItem() == RootsRegistry.WILDWOOD_MASK.get() &&
        chest.getItem() == RootsRegistry.WILDWOOD_PLATE.get() &&
        legs.getItem() == RootsRegistry.WILDWOOD_LEGGINGS.get() &&
        feet.getItem() == RootsRegistry.WILDWOOD_BOOTS.get()) {
      //
      if (entity.level.random.nextDouble() < 0.02 && entity.getHealth() < entity.getMaxHealth()) {
        entity.heal(1);//1 half heart
      }
    }
  }

  @SubscribeEvent
  public void onLivingDrops(LivingDropsEvent event) {
    CompoundNBT persistentData = event.getEntityLiving().getPersistentData();
    if (persistentData.contains(Const.NBT_DONT_DROP)) {
      if (!persistentData.getBoolean(Const.NBT_DONT_DROP)) {
        event.setCanceled(true);
      }
    }
  }

  @SubscribeEvent
  public void onLivingXP(LivingExperienceDropEvent event) {
    CompoundNBT persistentData = event.getEntityLiving().getPersistentData();
    if (persistentData.contains(Const.NBT_DONT_DROP)) {
      if (!persistentData.getBoolean(Const.NBT_DONT_DROP)) {
        event.setCanceled(true);
      }
    }
  }

  @SubscribeEvent
  public void onLivingDamage(LivingHurtEvent event) {
    LivingEntity entityLiving = event.getEntityLiving();
    CompoundNBT persistentData = entityLiving.getPersistentData();
    if (persistentData.contains(Const.NBT_VULN)) {
      event.setAmount((float) (event.getAmount() * (1.0 + persistentData.getDouble(Const.NBT_VULN))));
      persistentData.remove(Const.NBT_VULN);
    }
    if (persistentData.contains(Const.NBT_THORNS) && event.getSource().getEntity() instanceof LivingEntity) {
      ((LivingEntity) event.getSource().getEntity()).hurt(DamageSource.CACTUS, persistentData.getFloat(Const.NBT_THORNS));
      persistentData.remove(Const.NBT_THORNS);
      RootsUtil.decrementTickTracking(entityLiving);
    }
    if (entityLiving instanceof PlayerEntity) {
      PlayerEntity player = (PlayerEntity) entityLiving;
      if (!player.inventory.getSelected().isEmpty() && player.inventory.getSelected().getItem() == RootsRegistry.ENGRAVED_BLADE.get()) {
        ItemStack sword = player.inventory.getSelected();
        if (sword.hasTag() && sword.getTag().contains("shadowstep")) {
          int stepLvl = sword.getTag().getInt("shadowstep");
          double chance = stepLvl * 12.5;
          if (player.getCommandSenderWorld().random.nextInt(100) < chance) {
            event.setCanceled(true);
          }
        }
      }
    }
    if (event.getSource().getEntity() instanceof PlayerEntity) {
      if (!event.getEntity().getCommandSenderWorld().isClientSide) {
        PlayerEntity player = (PlayerEntity) event.getSource().getEntity();
        if (!player.inventory.getSelected().isEmpty() && player.inventory.getSelected().getItem() == RootsRegistry.ENGRAVED_BLADE.get()) {
          ItemStack sword = player.inventory.getSelected();
          if (sword.hasTag()) {
            CompoundNBT tag = sword.getTag();
            if (tag.contains("aquatic")) {
              int aquaLvl = tag.getInt("aquatic");
              float amount = aquaLvl * 0.5f;
              event.getEntity().hurt(DamageSource.DROWN, amount);
            }
            if ((tag.contains("holy")) && entityLiving.getMobType() == CreatureAttribute.UNDEAD) {
              int holyLvl = tag.getInt("holy");
              float amount = holyLvl * 1.5f;
              float currentAmount = event.getAmount();
              event.setAmount(currentAmount + amount);
            }
            if (tag.contains("spikes")) {
              int spikeLvl = tag.getInt("spikes");
              float amount = spikeLvl;
              float currentAmount = event.getAmount();
              event.setAmount(currentAmount + amount);
            }
          }
        }
      }
    }
  }
}