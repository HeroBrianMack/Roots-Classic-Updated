package elucent.rootsclassic.component.components;

import java.util.ArrayList;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import elucent.rootsclassic.Const;
import elucent.rootsclassic.component.ComponentBase;
import elucent.rootsclassic.component.EnumCastType;
import elucent.rootsclassic.util.RootsUtil;

public class ComponentBlueOrchid extends ComponentBase {

  public ComponentBlueOrchid() {
    super(new ResourceLocation(Const.MODID, "blue_orchid"), Blocks.BLUE_ORCHID, 14);
  }

  @Override
  public void doEffect(Level world, Entity caster, EnumCastType type, double x, double y, double z, double potency, double duration, double size) {
    if (type == EnumCastType.SPELL) {
      if (caster instanceof Player && !world.isClientSide) {
        BlockPos pos = RootsUtil.getRayTrace(world, (Player) caster, 4 + 2 * (int) size);
        BlockState state = world.getBlockState(pos);
        Block block = world.getBlockState(pos).getBlock();
        if (block == Blocks.STONE || block == Blocks.DIRT || block == Blocks.GRASS || block == Blocks.SAND || block == Blocks.GRAVEL) {
          if (block == Blocks.GRASS) {
            state = Blocks.DIRT.defaultBlockState();
            world.setBlockAndUpdate(pos, state);
          }
          world.setBlockAndUpdate(pos.above(), state);
          ArrayList<LivingEntity> targets = (ArrayList<LivingEntity>) world.getEntitiesOfClass(LivingEntity.class, new AABB(pos.getX() - size, pos.getY() - size, pos.getZ() - size, pos.getX() + size, pos.getY() + size, pos.getZ() + size));
          for (LivingEntity target : targets) {
            if (target.getUUID() != caster.getUUID()) {
              target.push(0, 3, 0);
              Vec3 motion = target.getDeltaMovement();
              target.setDeltaMovement(motion.x, 0.65 + world.random.nextDouble() + 0.25 * potency, motion.z);
              if (target instanceof Player) {
                ((Player) target).hurtMarked = true;
              }
            }
          }
          if (world.random.nextInt(3) == 0) {
            world.setBlockAndUpdate(pos.above().west().north(), state);
          }
          if (world.random.nextInt(3) == 0) {
            world.setBlockAndUpdate(pos.above().east().south(), state);
          }
          if (world.random.nextInt(3) == 0) {
            world.setBlockAndUpdate(pos.above().north().east(), state);
          }
          if (world.random.nextInt(3) == 0) {
            world.setBlockAndUpdate(pos.above().south().west(), state);
          }
          if (world.random.nextInt(1) == 0) {
            world.setBlockAndUpdate(pos.above().west(), state);
          }
          if (world.random.nextInt(1) == 0) {
            world.setBlockAndUpdate(pos.above().east(), state);
          }
          if (world.random.nextInt(1) == 0) {
            world.setBlockAndUpdate(pos.above().north(), state);
          }
          if (world.random.nextInt(1) == 0) {
            world.setBlockAndUpdate(pos.above().south(), state);
          }
          world.setBlockAndUpdate(pos.above().above(), state);
          if (world.random.nextInt(3) == 0) {
            world.setBlockAndUpdate(pos.above().above().west(), state);
          }
          if (world.random.nextInt(3) == 0) {
            world.setBlockAndUpdate(pos.above().above().east(), state);
          }
          if (world.random.nextInt(3) == 0) {
            world.setBlockAndUpdate(pos.above().above().north(), state);
          }
          if (world.random.nextInt(3) == 0) {
            world.setBlockAndUpdate(pos.above().above().south(), state);
          }
          world.setBlockAndUpdate(pos.above().above().above(), state);
        }
      }
    }
  }
}
