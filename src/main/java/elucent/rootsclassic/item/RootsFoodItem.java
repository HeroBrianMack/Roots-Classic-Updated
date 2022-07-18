package elucent.rootsclassic.item;

import elucent.rootsclassic.registry.RootsRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class RootsFoodItem extends Item {

	private static final int HEAL_LARGE = 5;
	private static final int HEAL_SMALL = 2;

	public RootsFoodItem(Properties properties) {
		super(properties);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level levelAccessor, LivingEntity entityLiving) {
		Item item = stack.getItem();
		super.finishUsingItem(stack, levelAccessor, entityLiving);
		if (item == RootsRegistry.REDCURRANT.get()) {
			entityLiving.heal(HEAL_SMALL);
		}
		if (item == RootsRegistry.ELDERBERRY.get()) {
			entityLiving.removeAllEffects();
		}
		if (item == RootsRegistry.HEALING_POULTICE.get()) {
			entityLiving.heal(HEAL_LARGE);
		}
		return stack;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level levelAccessor, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, levelAccessor, tooltip, flagIn);
		if (stack.is(RootsRegistry.REDCURRANT.get())) {
			tooltip.add(new TranslatableComponent("rootsclassic.healingitem.tooltip").withStyle(ChatFormatting.GRAY));
		}
		if (stack.is(RootsRegistry.ELDERBERRY.get())) {
			tooltip.add(new TranslatableComponent("rootsclassic.clearpotionsitem.tooltip").withStyle(ChatFormatting.GRAY));
		}
		if (stack.is(RootsRegistry.HEALING_POULTICE.get())) {
			tooltip.add(new TranslatableComponent("rootsclassic.healingitem.tooltip").withStyle(ChatFormatting.GRAY));
		}
		if (stack.is(RootsRegistry.NIGHTSHADE.get())) {
			tooltip.add(new TranslatableComponent("rootsclassic.poisonitem.tooltip").withStyle(ChatFormatting.GRAY));
		}
	}
}
