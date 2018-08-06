package elucent.rootsclassic.component.components;

import java.util.ArrayList;
import java.util.Random;
import elucent.rootsclassic.component.ComponentBase;
import elucent.rootsclassic.component.EnumCastType;
import elucent.rootsclassic.entity.skeleton.EntityFrozenKnight;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ComponentRedTulip extends ComponentBase {

  Random random = new Random();

  public ComponentRedTulip() {
    super("redtulip", Blocks.RED_FLOWER, 6);
  }

  @Override
  public void doEffect(World world, Entity caster, EnumCastType type, double x, double y, double z, double potency, double duration, double size) {
    if (type == EnumCastType.SPELL) {
      ArrayList<EntityLivingBase> targets = (ArrayList<EntityLivingBase>) world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(x - size * 2.4, y - size * 2.4, z - size * 2.4, x + size * 2.4, y + size * 2.4, z + size * 2.4));
      if (targets.size() > 0 && !world.isRemote) {
        EntityFrozenKnight skeleton = new EntityFrozenKnight(world);
        skeleton.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(x, y, z)), null);
        //        skeleton.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
        skeleton.setDropItemsWhenDead(false);
        //        skeleton.getEntityData().setBoolean("RMOD_dropItems", false);
        //        skeleton.getEntityData().setString("RMOD_dontTarget", caster.getUniqueID().toString());
        skeleton.setPosition(x, y + 2.0, z);
        //        skeleton.setAttackTarget(targets.get(random.nextInt(targets.size())));
        // if (skeleton.getAttackTarget().getUniqueID() != caster.getUniqueID()) {
        world.spawnEntity(skeleton);
        // }
      }
    }
  }
}
