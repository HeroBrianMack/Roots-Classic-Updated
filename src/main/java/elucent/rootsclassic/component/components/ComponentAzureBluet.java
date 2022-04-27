package elucent.rootsclassic.component.components;

import elucent.rootsclassic.Const;
import elucent.rootsclassic.component.ComponentBase;
import elucent.rootsclassic.component.EnumCastType;
import elucent.rootsclassic.util.RootsUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ComponentAzureBluet extends ComponentBase {

  public ComponentAzureBluet() {
    super(new ResourceLocation(Const.MODID, "azure_bluet"), Blocks.AZURE_BLUET, 6);
  }

  public void destroyBlockSafe(World world, BlockPos pos, int potency) {
    BlockState state = world.getBlockState(pos);
    if (state.getBlock().getHarvestLevel(world.getBlockState(pos)) <= 2 + potency && state.getDestroySpeed(world, pos) != -1) {
      world.destroyBlock(pos, true);
    }
  }

  @Override
  public void doEffect(World world, Entity caster, EnumCastType type, double x, double y, double z, double potency, double duration, double size) {
    if (type == EnumCastType.SPELL) {
      if (caster instanceof PlayerEntity && !world.isClientSide) {
        BlockPos pos = RootsUtil.getRayTrace(world, (PlayerEntity) caster, 4 + 2 * (int) size);
        destroyBlockSafe(world, pos, (int) potency);
        if (world.random.nextBoolean()) {
          destroyBlockSafe(world, pos.above(), (int) potency);
        }
        if (world.random.nextBoolean()) {
          destroyBlockSafe(world, pos.below(), (int) potency);
        }
        if (world.random.nextBoolean()) {
          destroyBlockSafe(world, pos.east(), (int) potency);
        }
        if (world.random.nextBoolean()) {
          destroyBlockSafe(world, pos.west(), (int) potency);
        }
        if (world.random.nextBoolean()) {
          destroyBlockSafe(world, pos.north(), (int) potency);
        }
        if (world.random.nextBoolean()) {
          destroyBlockSafe(world, pos.south(), (int) potency);
        }
      }
    }
  }
}
