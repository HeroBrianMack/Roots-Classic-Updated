package elucent.rootsclassic.component.components;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import elucent.rootsclassic.Const;
import elucent.rootsclassic.client.particles.MagicAuraParticleData;
import elucent.rootsclassic.component.ComponentBase;
import elucent.rootsclassic.component.EnumCastType;
import elucent.rootsclassic.util.RootsUtil;

public class ComponentPeony extends ComponentBase {

  List<BlockPos> pos = new CopyOnWriteArrayList<>();

  public ComponentPeony() {
    super(new ResourceLocation(Const.MODID, "peony"), Blocks.PEONY, 24);
  }

  @Override
  public void castingAction(Player player, int count, int potency, int efficiency, int size) {
    Level world = player.level;
    int x = player.blockPosition().getX();
    int y = player.blockPosition().getY();
    int z = player.blockPosition().getZ();
    int range = 5;
    for (int x2 = -range; x2 < range; x2++) {
      for (int z2 = -range; z2 < range; z2++) {
        for (int y2 = -1; y2 < 2; y2++) {
          if (world.getBlockState(new BlockPos(x + x2, y + y2, z + z2)).getBlock() instanceof TallFlowerBlock ||
              world.getBlockState(new BlockPos(x + x2, y + y2, z + z2)).getBlock() instanceof FlowerBlock ||
              world.getBlockState(new BlockPos(x + x2, y + y2, z + z2)).getBlock() instanceof DoublePlantBlock) {
            pos.add(new BlockPos(x + x2, y + y2, z + z2));
          }
        }
      }
    }
    pos.forEach((temp) -> {
      int fX = temp.getX();
      int fY = temp.getY();
      int fZ = temp.getZ();
      double factor = 0.15;
      if (world.random.nextInt(10 * pos.size()) == 0) {
        world.addParticle(MagicAuraParticleData.createData(138, 42, 235),
            fX + RootsUtil.randomDouble(0.0, 0.5), fY + RootsUtil.randomDouble(0.1, 0.5), fZ + RootsUtil.randomDouble(0.0, 0.5),
            (player.getX() - fX) * factor, (player.getY() - fY) * factor, (player.getZ() - fZ) * factor);
        world.addParticle(MagicAuraParticleData.createData(138, 42, 235),
            fX + RootsUtil.randomDouble(0.0, 0.5), fY + RootsUtil.randomDouble(0.1, 0.5), fZ + RootsUtil.randomDouble(0.0, 0.5),
            (player.getX() - fX) * factor, (player.getY() - fY) * factor, (player.getZ() - fZ) * factor);
      }
    });
  }

  @Override
  public void doEffect(Level world, Entity caster, EnumCastType type, double x, double y, double z, double potency, double duration, double size) {
    if (type == EnumCastType.SPELL) {
      ((LivingEntity) caster).heal((float) (pos.size() / 2 + (potency * 2)));
      pos.clear();
    }
  }
}