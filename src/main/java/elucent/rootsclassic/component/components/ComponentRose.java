package elucent.rootsclassic.component.components;

import java.util.ArrayList;
import elucent.rootsclassic.Util;
import elucent.rootsclassic.component.ComponentBase;
import elucent.rootsclassic.component.EnumCastType;
import elucent.rootsclassic.config.ConfigManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class ComponentRose extends ComponentBase {

  public ComponentRose() {
    super("rosebush", Blocks.DOUBLE_PLANT, 4, 14);
  }

  @Override
  public void doEffect(World world, Entity caster, EnumCastType type, double x, double y, double z, double potency, double duration, double size) {
    if (type == EnumCastType.SPELL) {
      ArrayList<EntityLivingBase> targets = (ArrayList<EntityLivingBase>) world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(x - size, y - size, z - size, x + size, y + size, z + size));
      int damageDealt = 0;
      for (int i = 0; i < targets.size(); i++) {
        if (targets.get(i).getUniqueID() != caster.getUniqueID()) {
          if (targets.get(i) instanceof EntityPlayer && ConfigManager.disablePVP) {}
          else {
            //            if (caster instanceof EntityPlayer) {
            //              if (!((EntityPlayer) caster).hasAchievement(RegistryManager.achieveSpellRose)) {
            //                PlayerManager.addAchievement(((EntityPlayer) caster), RegistryManager.achieveSpellRose);
            //              }
            //            }
            targets.get(i).attackEntityFrom(DamageSource.CACTUS, (int) (9 + 2 * potency));
            Util.addTickTracking(targets.get(i));
            targets.get(i).getEntityData().setFloat("RMOD_thornsDamage", 2.0f + (float) potency);
            targets.get(i).attackEntityAsMob(caster);
            targets.get(i).setLastAttackedEntity(caster);
            targets.get(i).setRevengeTarget((EntityLivingBase) caster);
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
