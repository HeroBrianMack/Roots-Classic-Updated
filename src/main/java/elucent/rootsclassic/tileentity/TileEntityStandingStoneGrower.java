package elucent.rootsclassic.tileentity;

import java.util.Random;
import elucent.rootsclassic.Roots;
import net.minecraft.block.BlockCrops;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileEntityStandingStoneGrower extends TEBase implements ITickable {

  private static final int RADIUS = 10;
  int ticker = 0;
  Random random = new Random();

  public TileEntityStandingStoneGrower() {
    super();
  }

  @Override
  public void readFromNBT(NBTTagCompound tag) {
    super.readFromNBT(tag);
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tag) {
    super.writeToNBT(tag);
    return tag;
  }

  @Override
  public void update() {
    ticker++;
    if (ticker % 5 == 0) {
      for (double i = 0; i < 720; i += 45.0) {
        double xShift = 0.5 * Math.sin(Math.PI * (i / 360.0));
        double zShift = 0.5 * Math.cos(Math.PI * (i / 360.0));
        Roots.proxy.spawnParticleMagicAuraFX(this.getWorld(), this.getPos().getX() + 0.5 + xShift, this.getPos().getY() + 0.5, this.getPos().getZ() + 0.5 + zShift, 0, 0, 0, 32, 255, 32);
      }
    }
    if (ticker % 100 == 0) {
      for (int xp = RADIUS; xp <= RADIUS; xp++) {
        for (int zp = RADIUS; zp <= RADIUS; zp++) {
          BlockPos pos = this.getPos().add(xp, -1, zp);
          if (this.getWorld().getBlockState(pos).getBlock() instanceof BlockCrops && random.nextInt(40) == 0) {
            int age = this.getWorld().getBlockState(pos).getBlock().getMetaFromState(this.getWorld().getBlockState(pos));
            if (age < ((BlockCrops) this.getWorld().getBlockState(pos).getBlock()).getMaxAge()) {
              this.getWorld().setBlockState(pos, ((BlockCrops) this.getWorld().getBlockState(pos).getBlock()).withAge(age + 1));
            }
          }
        }
      }
    }
    if (ticker >= 2000) {
      ticker = 0;
    }
  }
}
