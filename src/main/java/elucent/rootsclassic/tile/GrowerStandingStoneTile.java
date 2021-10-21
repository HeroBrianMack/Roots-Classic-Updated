package elucent.rootsclassic.tile;

import elucent.rootsclassic.client.particles.MagicAuraParticleData;
import elucent.rootsclassic.item.GrowthPowderItem;
import elucent.rootsclassic.registry.RootsRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GrowerStandingStoneTile extends TEBase implements ITickableTileEntity {

  private static final double PCT_CHANCE_PER_BLOCK = 0.03;
  private static final int TICK_SPEED = 100;
  private static final int MAX_TICKER = 500 * 20;
  private static final int RADIUS = 4;
  int ticker = 0;

  public GrowerStandingStoneTile(TileEntityType<?> tileEntityTypeIn) {
    super(tileEntityTypeIn);
  }

  public GrowerStandingStoneTile() {
    this(RootsRegistry.GROWER_STANDING_STONE_TILE.get());
  }

  @Override
  public void load(BlockState state, CompoundNBT tag) {
    super.load(state, tag);
  }

  @Override
  public CompoundNBT save(CompoundNBT tag) {
    super.save(tag);
    return tag;
  }

  @Override
  public void tick() {
    updateTicker();
    spawnParticles();
    if (ticker % TICK_SPEED == 0) {
      applyGrowthToArea();
    }
  }

  private void updateTicker() {
    ticker++;
    if (ticker >= MAX_TICKER) {
      ticker = 0;
    }
  }

  private void applyGrowthToArea() {
    List<BlockPos> positions = new ArrayList<>();
    for (int xp = -1 * RADIUS; xp <= RADIUS; xp++) {
      for (int zp = -1 * RADIUS; zp <= RADIUS; zp++) {
        positions.add(this.getBlockPos().offset(xp, -1, zp));
        positions.add(this.getBlockPos().offset(xp, -2, zp));
      }
    }
    Collections.shuffle(positions);
    for (BlockPos pos : positions) {
      if (level.random.nextDouble() < PCT_CHANCE_PER_BLOCK) {
        //this percent chance to actually fire
        GrowthPowderItem.applyGrowthHere(level, pos);
      }
    }
  }

  private void spawnParticles() {
    if (ticker % 5 == 0 && level.isClientSide) {
      for (double i = 0; i < 720; i += 45.0) {
        double xShift = 0.5 * Math.sin(Math.PI * (i / 360.0));
        double zShift = 0.5 * Math.cos(Math.PI * (i / 360.0));
        level.addParticle(MagicAuraParticleData.createData(32, 255, 32),
            worldPosition.getX() + 0.5 + xShift, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5 + zShift, 0, 0, 0);
      }
    }
  }
}