package elucent.rootsclassic.block.mortar;

import elucent.rootsclassic.recipe.ComponentRecipe;
import elucent.rootsclassic.registry.RootsRecipes;
import elucent.rootsclassic.registry.RootsRegistry;
import elucent.rootsclassic.tile.TEBase;
import elucent.rootsclassic.util.InventoryUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

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

	public MortarTile(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	public MortarTile() {
		this(RootsRegistry.MORTAR_TILE.get());
	}

	@Override
	public void read(BlockState state, CompoundNBT nbt) {
		super.read(state, nbt);
		inventory.deserializeNBT(nbt.getCompound("InventoryHandler"));
	}

	@Override
	public CompoundNBT write(CompoundNBT tag) {
		tag = super.write(tag);

		tag.put("InventoryHandler", inventory.serializeNBT());

		return tag;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		dropAllItems(world, pos);
		this.remove();
	}

	@Override
	public ActionResultType activate(World world, BlockPos pos, BlockState state, PlayerEntity player, Hand hand, ItemStack heldItem, BlockRayTraceResult hit) {
		if(hand == Hand.MAIN_HAND) {
			if (heldItem.isEmpty()) {
				return tryDropSingleItem(world, pos, state);
			} else if (heldItem.getItem() == RootsRegistry.PESTLE.get()) {
				return tryActivateRecipe(player, state);
			} else {
				return tryInsertItem(world, pos, state, heldItem);
			}
		}
		return ActionResultType.PASS;
	}
	private ActionResultType tryInsertItem(World world, BlockPos pos, BlockState state, ItemStack heldItem) {
		if (!heldItem.isEmpty() && !InventoryUtil.isFull(inventory)) {
			ItemStack heldCopy = heldItem.copy();
			heldCopy.setCount(1);

			if (heldItem.getItem() == Items.GLOWSTONE_DUST || heldItem.getItem() == Items.REDSTONE || heldItem.getItem() == Items.GUNPOWDER) {
				int maxCapacity = ComponentRecipe.getModifierCapacity(InventoryUtil.createIInventory(inventory));
				int modifierCount = ComponentRecipe.getModifierCount(InventoryUtil.createIInventory(inventory));
				if (modifierCount < maxCapacity) {
					ItemStack restStack = ItemHandlerHelper.insertItem(inventory, heldCopy, false);
					if(restStack.isEmpty()) {
						heldItem.shrink(1);
						markDirty();
						world.notifyBlockUpdate(getPos(), state, world.getBlockState(pos), 3);
						return ActionResultType.SUCCESS;
					} else {
						return ActionResultType.FAIL;
					}
				}
			} else {
				ItemStack restStack = ItemHandlerHelper.insertItem(inventory, heldCopy, false);
				if(restStack.isEmpty()) {
					heldItem.shrink(1);
					markDirty();
					world.notifyBlockUpdate(getPos(), state, world.getBlockState(pos), 3);
					return ActionResultType.SUCCESS;
				} else {
					return ActionResultType.FAIL;
				}
			}
		}
		return ActionResultType.PASS;
	}

	private ActionResultType tryDropSingleItem(World world, BlockPos pos, BlockState state) {
		if(!InventoryUtil.isEmpty(inventory)) {
			ItemStack lastStack = InventoryUtil.getLastStack(inventory);
			if(!lastStack.isEmpty()) {
				dropItem(lastStack, 0.5F);
				lastStack.shrink(1);
			}
			markDirty();
			world.notifyBlockUpdate(getPos(), state, world.getBlockState(pos), 3);
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

	private ActionResultType tryActivateRecipe(PlayerEntity player, BlockState state) {
		ComponentRecipe recipe = world.getRecipeManager().getRecipe(RootsRecipes.COMPONENT_RECIPE_TYPE, InventoryUtil.createIInventory(inventory), world).orElse(null);
		if (recipe == null) {
			player.sendStatusMessage(new TranslationTextComponent("rootsclassic.mortar.invalid"), true);
			return ActionResultType.PASS;
		} else if (recipe.needsMixin() && ComponentRecipe.getModifierCapacity(InventoryUtil.createIInventory(inventory)) < 0) {
			player.sendStatusMessage(new TranslationTextComponent("rootsclassic.mortar.mixin"), true);
			return ActionResultType.PASS;
		}
		if (!world.isRemote) {
			world.addEntity(new ItemEntity(world, getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5, recipe.getCraftingResult(InventoryUtil.createIInventory(inventory))));
		}
		InventoryUtil.clearInventory(inventory);
		markDirty();
		world.notifyBlockUpdate(getPos(), state, world.getBlockState(pos), 3);
		return ActionResultType.SUCCESS;
	}

	public ItemEntity dropItem(ItemStack stack, float offsetY) {
		ItemStack copyStack = stack.copy();
		if (copyStack.isEmpty()) {
			return null;
		} else if (world.isRemote) {
			return null;
		} else {
			BlockPos pos = getPos();
			ItemEntity itementity = new ItemEntity(this.world, pos.getX(), pos.getY() + (double)offsetY, this.pos.getZ(), copyStack);
			itementity.setDefaultPickupDelay();
			this.world.addEntity(itementity);
			return itementity;
		}
	}

	private void dropAllItems(World world, BlockPos pos) {
		for (int i = 0; i < inventory.getSlots(); i++) {
			ItemStack stack = inventory.getStackInSlot(i);
			dropItem(stack, 0F);
		}
	}

	private void calculateRotations() {
		for(int i = 0; i < inventory.getSlots(); i++) {
			ItemStack stack = inventory.getStackInSlot(i);
			if(!stack.isEmpty()) {

			}
		}
	}

	@Override
	protected void invalidateCaps() {
		super.invalidateCaps();
		inventoryHolder.invalidate();
	}
}
