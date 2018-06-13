package elucent.rootsclassic.item;

import elucent.rootsclassic.Roots;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRunedTablet extends Item {

  public ItemRunedTablet() {
    super();

    setCreativeTab(Roots.tab);
  }

  @Override
  public int getItemStackLimit() {
    return 1;
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
    ItemStack stack = player.getHeldItem(hand);
    if (hand == EnumHand.MAIN_HAND) {

      player.openGui(Roots.instance, 1, world, (int) player.posX, (int) player.posY, (int) player.posZ);
    }
    return new ActionResult(EnumActionResult.PASS, stack);
  }

  @SideOnly(Side.CLIENT)
  public void initModel() {
    ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
  }
}