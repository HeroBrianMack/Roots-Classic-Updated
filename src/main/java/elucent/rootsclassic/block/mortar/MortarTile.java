package elucent.rootsclassic.block.mortar;

import elucent.rootsclassic.recipe.ComponentRecipe;
import elucent.rootsclassic.registry.RootsRecipes;
import elucent.rootsclassic.registry.RootsRegistry;
import elucent.rootsclassic.tile.TEBase;
import elucent.rootsclassic.util.InventoryUtil;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class MortarTile extends TEBase {

  public final ItemStackHandler inventory = new ItemStackHandler(8) {

    @Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
      return 1;
    }

    @Override
    protected void onContentsChanged(int slot) {
      super.onContentsChanged(slot);
      calculateRotations();
    }
  };
  private LazyOptional<IItemHandler> inventoryHolder = LazyOptional.of(() -> inventory);

  public MortarTile(BlockPos pos, BlockState state) {
    super(RootsRegistry.MORTAR_TILE.get(), pos, state);
  }

  @Override
  public void load(CompoundTag nbt) {
    super.load(nbt);
    inventory.deserializeNBT(nbt.getCompound("InventoryHandler"));
  }

  @Override
  public CompoundTag save(CompoundTag tag) {
    tag = super.save(tag);
    tag.put("InventoryHandler", inventory.serializeNBT());
    return tag;
  }

  @Override
  public void breakBlock(Level world, BlockPos pos, BlockState state, Player player) {
    dropAllItems(world, pos);
    this.setRemoved();
  }

  @Override
  public InteractionResult activate(Level world, BlockPos pos, BlockState state, Player player, InteractionHand hand, ItemStack heldItem, BlockHitResult hit) {
    if (hand == InteractionHand.MAIN_HAND) {
      if (heldItem.isEmpty()) {
        return tryDropSingleItem(world, pos, state);
      }
      else if (heldItem.getItem() == RootsRegistry.PESTLE.get()) {
        return tryActivateRecipe(player, state);
      }
      else {
        return tryInsertItem(world, pos, state, heldItem);
      }
    }
    return InteractionResult.PASS;
  }

  private InteractionResult tryInsertItem(Level world, BlockPos pos, BlockState state, ItemStack heldItem) {
    if (!heldItem.isEmpty() && !InventoryUtil.isFull(inventory)) {
      ItemStack heldCopy = heldItem.copy();
      heldCopy.setCount(1);
      if (heldItem.getItem() == Items.GLOWSTONE_DUST || heldItem.getItem() == Items.REDSTONE || heldItem.getItem() == Items.GUNPOWDER) {
        int maxCapacity = ComponentRecipe.getModifierCapacity(InventoryUtil.createIInventory(inventory));
        int modifierCount = ComponentRecipe.getModifierCount(InventoryUtil.createIInventory(inventory));
        if (modifierCount < maxCapacity) {
          ItemStack restStack = ItemHandlerHelper.insertItem(inventory, heldCopy, false);
          if (restStack.isEmpty()) {
            heldItem.shrink(1);
            setChanged();
            world.sendBlockUpdated(getBlockPos(), state, world.getBlockState(pos), 3);
            return InteractionResult.SUCCESS;
          }
          else {
            return InteractionResult.FAIL;
          }
        }
      }
      else {
        ItemStack restStack = ItemHandlerHelper.insertItem(inventory, heldCopy, false);
        if (restStack.isEmpty()) {
          heldItem.shrink(1);
          setChanged();
          world.sendBlockUpdated(getBlockPos(), state, world.getBlockState(pos), 3);
          return InteractionResult.SUCCESS;
        }
        else {
          return InteractionResult.FAIL;
        }
      }
    }
    return InteractionResult.PASS;
  }

  private InteractionResult tryDropSingleItem(Level world, BlockPos pos, BlockState state) {
    if (!InventoryUtil.isEmpty(inventory)) {
      ItemStack lastStack = InventoryUtil.getLastStack(inventory);
      if (!lastStack.isEmpty()) {
        dropItem(lastStack, 0.5F);
        lastStack.shrink(1);
      }
      setChanged();
      world.sendBlockUpdated(getBlockPos(), state, world.getBlockState(pos), 3);
      return InteractionResult.SUCCESS;
    }
    return InteractionResult.PASS;
  }

  private InteractionResult tryActivateRecipe(Player player, BlockState state) {
    ComponentRecipe recipe = level.getRecipeManager().getRecipeFor(RootsRecipes.COMPONENT_RECIPE_TYPE, InventoryUtil.createIInventory(inventory), level).orElse(null);
    if (recipe == null) {
      player.displayClientMessage(new TranslatableComponent("rootsclassic.mortar.invalid"), true);
      return InteractionResult.PASS;
    }
    else if (recipe.needsMixin() && ComponentRecipe.getModifierCapacity(InventoryUtil.createIInventory(inventory)) < 0) {
      player.displayClientMessage(new TranslatableComponent("rootsclassic.mortar.mixin"), true);
      return InteractionResult.PASS;
    }
    if (!level.isClientSide) {
      level.addFreshEntity(new ItemEntity(level, getBlockPos().getX() + 0.5, getBlockPos().getY() + 0.5, getBlockPos().getZ() + 0.5, recipe.assemble(InventoryUtil.createIInventory(inventory))));
    }
    InventoryUtil.clearInventory(inventory);
    setChanged();
    level.sendBlockUpdated(getBlockPos(), state, level.getBlockState(worldPosition), 3);
    return InteractionResult.SUCCESS;
  }

  public ItemEntity dropItem(ItemStack stack, float offsetY) {
    ItemStack copyStack = stack.copy();
    if (copyStack.isEmpty()) {
      return null;
    }
    else if (level.isClientSide) {
      return null;
    }
    else {
      BlockPos pos = getBlockPos();
      ItemEntity itementity = new ItemEntity(this.level, pos.getX(), pos.getY() + (double) offsetY, this.worldPosition.getZ(), copyStack);
      itementity.setDefaultPickUpDelay();
      this.level.addFreshEntity(itementity);
      return itementity;
    }
  }

  private void dropAllItems(Level world, BlockPos pos) {
    for (int i = 0; i < inventory.getSlots(); i++) {
      ItemStack stack = inventory.getStackInSlot(i);
      dropItem(stack, 0F);
    }
  }

  private void calculateRotations() {
    for (int i = 0; i < inventory.getSlots(); i++) {
      ItemStack stack = inventory.getStackInSlot(i);
      if (!stack.isEmpty()) {
      }
    }
  }

  @Override
  public void invalidateCaps() {
    super.invalidateCaps();
    inventoryHolder.invalidate();
  }
}
