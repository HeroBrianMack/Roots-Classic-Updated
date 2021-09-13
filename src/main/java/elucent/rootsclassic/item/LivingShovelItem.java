package elucent.rootsclassic.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.Level;
import elucent.rootsclassic.util.RootsUtil;

public class LivingShovelItem extends ShovelItem {

  public LivingShovelItem(Tier tier, float attackDamageIn, float attackSpeedIn, Item.Properties builder) {
    super(tier, attackDamageIn, attackSpeedIn, builder);
  }

  @Override
  public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
    RootsUtil.randomlyRepair(world.random, stack);
  }
}
