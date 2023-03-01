package elucent.rootsclassic.compat.jei.category;

import com.mojang.blaze3d.vertex.PoseStack;
import elucent.rootsclassic.Const;
import elucent.rootsclassic.compat.jei.JEIPlugin;
import elucent.rootsclassic.recipe.ComponentRecipe;
import elucent.rootsclassic.registry.RootsRegistry;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class MortarCategory implements IRecipeCategory<ComponentRecipe> {
	private final static ResourceLocation backgroundLocation = new ResourceLocation(Const.MODID, "textures/gui/tabletmortar.png");
	private final IDrawable background;
	private final IDrawable icon;
	private final Component localizedName;

	public MortarCategory(IGuiHelper guiHelper) {
		this.background = guiHelper.drawableBuilder(backgroundLocation, 21, 30, 142, 45).addPadding(0, 0, 0, 0).build();
		this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(RootsRegistry.MORTAR.get()));
		this.localizedName = new TranslatableComponent("rootsclassic.gui.jei.category.mortar");
	}

	@Override
	public RecipeType<ComponentRecipe> getRecipeType() {
		return JEIPlugin.MORTAR_TYPE;
	}

	@SuppressWarnings("removal")
	@Override
	public ResourceLocation getUid() {
		return JEIPlugin.MORTAR;
	}

	@SuppressWarnings("removal")
	@Override
	public Class<? extends ComponentRecipe> getRecipeClass() {
		return ComponentRecipe.class;
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public Component getTitle() {
		return localizedName;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, ComponentRecipe recipe, IFocusGroup focuses) {
		for (int i = 0; i < recipe.getIngredients().size(); i++) {
			Ingredient ingredient = recipe.getIngredients().get(i);
			builder.addSlot(RecipeIngredientRole.INPUT, 3 + (i * 16), 26).addIngredients(ingredient);
		}
		builder.addSlot(RecipeIngredientRole.OUTPUT, 123, 26).addItemStack(recipe.assemble(null));
	}

	@Override
	public void draw(ComponentRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
		IRecipeCategory.super.draw(recipe, recipeSlotsView, stack, mouseX, mouseY);
	}
}
