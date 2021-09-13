package elucent.rootsclassic.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.gui.screens.Screen;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.chat.TextComponent;
import elucent.rootsclassic.Const;
import elucent.rootsclassic.client.ClientInfo;
import elucent.rootsclassic.config.RootsConfig;
import elucent.rootsclassic.research.ResearchBase;
import elucent.rootsclassic.research.ResearchGroup;
import elucent.rootsclassic.research.ResearchManager;
import elucent.rootsclassic.util.RootsUtil;

public class TabletScreen extends Screen {

  private int currentGroup = 0;
  private final Player player;

  public TabletScreen(Player player) {
    super(TextComponent.EMPTY);
    this.player = player;
    if (player.getItemInHand(InteractionHand.MAIN_HAND).hasTag()) {
      currentGroup = player.getItemInHand(InteractionHand.MAIN_HAND).getTag().getInt("currentGroup");
    }
  }

  public static void openScreen(Player player) {
    Minecraft.getInstance().setScreen(new TabletScreen(player));
  }

  @Override
  public boolean isPauseScreen() {
    return false;
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    float basePosX = (width / 2.0f) - 108;
    ResearchGroup group = null;
    ResearchBase base = null;
    for (int i = 0; i < ResearchManager.globalResearches.get(currentGroup).researches.size(); i++) {
      minecraft.getTextureManager().bind(Const.tabletGui);
      float yShift = (float) Math.floor(i / 6);
      float xShift = i % 6;
      if (mouseX >= basePosX + 32 * xShift && mouseX < basePosX + 32 * xShift + 24 && mouseY >= 32 + 40 * yShift && mouseY < 32 + 40 * yShift + 24) {
        group = ResearchManager.globalResearches.get(currentGroup);
        base = ResearchManager.globalResearches.get(currentGroup).researches.get(i);
      }
    }
    if (group != null && base != null) {
      CompoundTag persistentData = player.getPersistentData();
      persistentData.putString("RMOD_researchGroup", group.getName());
      persistentData.putString("RMOD_researchBase", base.getName());
      ResearchBase tempResearch = ResearchManager.getResearch(persistentData.getString("RMOD_researchGroup"), persistentData.getString("RMOD_researchBase"));
      ResearchGroup tempGroup = ResearchManager.getResearchGroup(persistentData.getString("RMOD_researchGroup"));
      persistentData.remove("RMOD_researchGroup");
      persistentData.remove("RMOD_researchBase");
      if (tempResearch != null && tempGroup != null) {
        minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        this.minecraft.setScreen(new TabletPageScreen(tempGroup, tempResearch, player));
      }
    }
    if (mouseX >= 32 && mouseX < 64 && mouseY >= height - 48 && mouseY < height - 32) {
      currentGroup--;
      if (currentGroup < 0) {
        minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        currentGroup = ResearchManager.globalResearches.size() - 1;
      }
    }
    if (mouseX >= width - 64 && mouseX < width - 32 && mouseY >= height - 48 && mouseY < height - 32) {
      currentGroup++;
      if (currentGroup == ResearchManager.globalResearches.size()) {
        minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        currentGroup = 0;
      }
    }
    ItemStack heldStack = player.getItemInHand(InteractionHand.MAIN_HAND);
    if (currentGroup != 0) {
      CompoundTag tag = heldStack.getTag() != null ? heldStack.getTag() : new CompoundTag();
      tag.putInt("currentGroup", currentGroup);
      heldStack.setTag(tag);
    }
    else {
      if (heldStack.hasTag()) {
        heldStack.getTag().remove("currentGroup");
      }
    }
    return super.mouseClicked(mouseX, mouseY, button);
  }

  public void drawQuad(BufferBuilder vertexbuffer, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, int minU, int minV, int maxU, int maxV) {
    float f = 0.00390625F;
    float f1 = 0.00390625F;
    vertexbuffer.vertex(x4 + 0.0F, y4 + 0.0F, this.itemRenderer.blitOffset).uv((minU + 0) * f, (minV + maxV) * f1).endVertex();
    vertexbuffer.vertex(x3 + 0.0F, y3 + 0.0F, this.itemRenderer.blitOffset).uv((minU + maxU) * f, (minV + maxV) * f1).endVertex();
    vertexbuffer.vertex(x2 + 0.0F, y2 + 0.0F, this.itemRenderer.blitOffset).uv((minU + maxU) * f, (minV + 0) * f1).endVertex();
    vertexbuffer.vertex(x1 + 0.0F, y1 + 0.0F, this.itemRenderer.blitOffset).uv((minU + 0) * f, (minV + 0) * f1).endVertex();
  }

  @Override
  public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(matrixStack);
    this.player.releaseUsingItem();
    matrixStack.pushPose();
    minecraft.getTextureManager().bind(Const.tabletGui);
    float unit = width / 32.0f;
    if (RootsConfig.CLIENT.showTabletWave.get()) {
      RenderSystem.enableBlend();
      Tesselator tessellator = Tesselator.getInstance();
      BufferBuilder bufferBuilder = tessellator.getBuilder();
      bufferBuilder.begin(7, DefaultVertexFormat.POSITION_TEX);
      for (float i = 0; i < width; i += unit) {
        float height1 = 12.0f * ((float) Math.cos(((ClientInfo.ticksInGame / 36.0) + (i / (width / 4.0))) * Math.PI) + 1.0f);
        float height2 = 12.0f * ((float) Math.cos(((ClientInfo.ticksInGame / 36.0) + ((i + unit) / (width / 4.0))) * Math.PI) + 1.0f);
        this.drawQuad(bufferBuilder, i, height - (24.0f + height1), i + unit, height - (24.0f + height2), i + unit, height, i, height, 16, 96, 16, 64);
      }
      tessellator.end();
      RenderSystem.disableBlend();
    }
    int basePosX = (int) ((width / 2.0f) - 108);
    String researchName = "rootsclassic.research." + ResearchManager.globalResearches.get(currentGroup).getName();
    for (int i = 0; i < ResearchManager.globalResearches.get(currentGroup).researches.size(); i++) {
      minecraft.getTextureManager().bind(Const.tabletGui);
      int yShift = (int) (float) Math.floor(i / 6);
      int xShift = (int) (i % 6);
      this.blit(matrixStack, basePosX + 32 * xShift, 32 + 40 * yShift, 16, 0, 24, 24);
      if (ResearchManager.globalResearches.get(currentGroup).researches.get(i).getIcon() != null) {
        this.itemRenderer.renderGuiItem(ResearchManager.globalResearches.get(currentGroup).researches.get(i).getIcon(), (int) (basePosX + xShift * 32 + 4), (int) (32 + 40 * yShift + 4));
      }
      if (mouseX >= basePosX + 32 * xShift && mouseX < basePosX + 32 * xShift + 24 && mouseY >= 32 + 40 * yShift && mouseY < 32 + 40 * yShift + 24) {
        String name = I18n.get(researchName + "." + ResearchManager.globalResearches.get(currentGroup).researches.get(i).getName());
        this.font.drawShadow(matrixStack, name, basePosX + 32 * xShift + 12 - (font.width(name) / 2.0f), 32 + 40 * yShift + 25, RootsUtil.intColor(255, 255, 255));
      }
    }
    String formattedName = I18n.get(researchName);
    this.font.drawShadow(matrixStack, formattedName, width / 2.0f - (font.width(formattedName) / 2.0f), height - 16.0f, RootsUtil.intColor(255, 255, 255));
    minecraft.getTextureManager().bind(Const.tabletGui);
    if (mouseX >= 32 && mouseX < 64 && mouseY >= height - 48 && mouseY < height - 32) {
      this.blit(matrixStack, 32, height - 48, 32, 80, 32, 16);
    }
    else {
      this.blit(matrixStack, 32, height - 48, 32, 64, 32, 16);
    }
    if (mouseX >= width - 64 && mouseX < width - 32 && mouseY >= height - 48 && mouseY < height - 32) {
      this.blit(matrixStack, width - 64, height - 48, 0, 80, 32, 16);
    }
    else {
      this.blit(matrixStack, width - 64, height - 48, 0, 64, 32, 16);
    }
    matrixStack.popPose();
  }
}
