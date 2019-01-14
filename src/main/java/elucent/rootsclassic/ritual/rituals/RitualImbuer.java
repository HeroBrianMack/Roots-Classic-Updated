package elucent.rootsclassic.ritual.rituals;

import java.util.List;
import elucent.rootsclassic.RegistryManager;
import elucent.rootsclassic.item.ItemCrystalStaff;
import elucent.rootsclassic.ritual.RitualBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualImbuer extends RitualBase {

  public RitualImbuer() {
    super("imbuer", 1, 255, 255, 255);
  }

  @Override
  public void doEffect(World world, BlockPos pos, List<ItemStack> inventory, List<ItemStack> incenses) {
    ItemStack toSpawn = new ItemStack(RegistryManager.crystalStaff, 1);
    ItemCrystalStaff.createData(toSpawn);
    for (int i = 0; i < incenses.size() && i < 4; i++) {
      if (incenses.get(i) != null) {
        if (incenses.get(i).getItem() == RegistryManager.dustPetal && incenses.get(i).hasTagCompound()) {
          NBTTagCompound tag = incenses.get(i).getTagCompound();
          ItemCrystalStaff.addEffect(toSpawn, i + 1, tag.getString("effect"), tag.getInteger("potency"), tag.getInteger("efficiency"), tag.getInteger("size"));
        }
      }
    }
    if (!world.isRemote) {
      EntityItem item = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, toSpawn);
      item.forceSpawn = true;
      world.spawnEntity(item);
    }
    inventory.clear();
    world.getTileEntity(pos).markDirty();
  }
}
