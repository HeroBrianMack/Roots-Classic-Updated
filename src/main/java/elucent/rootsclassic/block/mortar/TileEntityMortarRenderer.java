package elucent.rootsclassic.block.mortar;

import java.util.Random;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

public class TileEntityMortarRenderer extends TileEntitySpecialRenderer<TileEntityMortar> {

  @Override
  public void render(TileEntityMortar tem, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
    for (ItemStack stack : tem.inventory) {
      GL11.glPushMatrix();
      EntityItem item = new EntityItem(Minecraft.getMinecraft().world, x, y, z, stack);
      item.hoverStart = 0;
      Random random = this.getWorld().rand;
      random.setSeed(item.getItem().hashCode());
      GL11.glTranslated(x, y, z);
      GL11.glTranslated(0.475 + random.nextFloat() / 20.0, 0.05 + random.nextFloat() / 20.0, 0.475 + random.nextFloat() / 20.0);
      GL11.glScaled(0.65, 0.65, 0.65);
      GL11.glRotated(random.nextInt(360), 0, 1, 0);
      Minecraft.getMinecraft().getRenderManager().doRenderEntity(item, 0, 0, 0, 0, 0, true);
      GL11.glPopMatrix();
    }
  }
}
