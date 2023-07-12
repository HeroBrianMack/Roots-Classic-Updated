package elucent.rootsclassic.component.components;

import elucent.rootsclassic.Const;
import elucent.rootsclassic.component.ComponentBase;
import elucent.rootsclassic.component.EnumCastType;
import elucent.rootsclassic.config.RootsConfig;
import elucent.rootsclassic.util.RootsUtil;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.ArrayList;

public class ComponentRose extends ComponentBase {

  public ComponentRose() {
    super(new ResourceLocation(Const.MODID, "rose_bush"), Blocks.ROSE_BUSH, 14);
  }

  @Override
  public void doEffect(World world, Entity caster, EnumCastType type, double x, double y, double z, double potency, double duration, double size) {
    if (type == EnumCastType.SPELL) {
      ArrayList<LivingEntity> targets = (ArrayList<LivingEntity>) world.getEntitiesOfClass(LivingEntity.class, new AxisAlignedBB(x - size, y - size, z - size, x + size, y + size, z + size));
      // int damageDealt = 0;
      for (LivingEntity target : targets) {
        if (target.getUUID() != caster.getUUID()) {
          if (target instanceof PlayerEntity && RootsConfig.COMMON.disablePVP.get()) {}
          else {
            //            if (caster instanceof EntityPlayer) {
            //              if (!((EntityPlayer) caster).hasAchievement(RegistryManager.achieveSpellRose)) {
            //                PlayerManager.addAchievement(((EntityPlayer) caster), RegistryManager.achieveSpellRose);
            //              }
            //            }
            target.hurt(DamageSource.CACTUS, (int) (9 + 2 * potency));
            RootsUtil.addTickTracking(target);
            target.getPersistentData().putFloat("RMOD_thornsDamage", 2.0f + (float) potency);
            target.doHurtTarget(caster);
            target.setLastHurtMob(caster);
            target.setLastHurtByMob((LivingEntity) caster);
          }
        }
      }
      //      if (damageDealt > 80) {
      //        if (caster instanceof EntityPlayer) {
      //          if (!((EntityPlayer) caster).hasAchievement(RegistryManager.achieveLotsDamage)) {
      //            PlayerManager.addAchievement(((EntityPlayer) caster), RegistryManager.achieveLotsDamage);
      //          }
      //        }
      //      }
    }
  }
}
