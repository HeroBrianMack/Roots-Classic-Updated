package elucent.rootsclassic.component.components;

import java.util.ArrayList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.Level;
import elucent.rootsclassic.Const;
import elucent.rootsclassic.component.ComponentBase;
import elucent.rootsclassic.component.EnumCastType;
import elucent.rootsclassic.config.RootsConfig;
import elucent.rootsclassic.util.RootsUtil;

public class ComponentRose extends ComponentBase {

  public ComponentRose() {
    super(new ResourceLocation(Const.MODID, "rose_bush"), Blocks.ROSE_BUSH, 14);
  }

  @Override
  public void doEffect(Level world, Entity caster, EnumCastType type, double x, double y, double z, double potency, double duration, double size) {
    if (type == EnumCastType.SPELL) {
      ArrayList<LivingEntity> targets = (ArrayList<LivingEntity>) world.getEntitiesOfClass(LivingEntity.class, new AABB(x - size, y - size, z - size, x + size, y + size, z + size));
      // int damageDealt = 0;
      for (LivingEntity target : targets) {
        if (target.getUUID() != caster.getUUID()) {
          if (target instanceof Player && RootsConfig.COMMON.disablePVP.get()) {}
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
