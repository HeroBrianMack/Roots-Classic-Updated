package elucent.rootsclassic.block;

import java.util.List;
import elucent.rootsclassic.Roots;
import elucent.rootsclassic.tileentity.TEBase;
import elucent.rootsclassic.tileentity.TileEntityStandingStoneEntangler;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStandingStoneEntangler extends TEBlockBase implements ITileEntityProvider {

  private static final AxisAlignedBB AXIS_ALIGNED_BB_ONE = new AxisAlignedBB(0.25, 0, 0.25, 0.75, 0.75, 0.75);
  private static final AxisAlignedBB AXIS_ALIGNED_BB_ZERO = new AxisAlignedBB(0.25, 0, 0.25, 0.75, 1.0, 0.75);
  public static final PropertyEnum<BlockstateIsTop> topState = PropertyEnum.create("topq", BlockstateIsTop.class);

  @Override
  public BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { topState });
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    BlockstateIsTop TYPE = state.getValue(topState);
    return TYPE.getID();
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty(topState, BlockstateIsTop.fromMeta(meta));
  }

  @Override
  public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
    if (this.getMetaFromState(state) == 0) {
      world.setBlockState(pos.up(), this.getDefaultState().withProperty(topState, BlockstateIsTop.enumTop));
    }
    else {
      world.setBlockState(pos.down(), this.getStateFromMeta(0));
    }
  }

  @Override
  public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    return null;
  }

  @Override
  public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
    if (!world.isRemote && !player.capabilities.isCreativeMode) {
      world.spawnEntity(new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(this, 1, 0)));
    }
    if (this.getMetaFromState(state) == 0) {
      world.setBlockToAir(pos.up());
    }
    else {
      world.setBlockToAir(pos.down());
    }
    if (world.getTileEntity(pos) instanceof TEBase) {
      ((TEBase) world.getTileEntity(pos)).breakBlock(world, pos, state, player);
    }
  }

  public BlockStandingStoneEntangler() {
    super(Material.ROCK);
    setUnlocalizedName("standingStoneEntangler");
    setCreativeTab(Roots.tab);
    setHardness(1.0f);
  }

  @Override
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }

  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
    if (this.getMetaFromState(state) == 0) {
      return AXIS_ALIGNED_BB_ZERO;
    }
    else {
      return AXIS_ALIGNED_BB_ONE;
    }
  }

  @Override
  public boolean isFullCube(IBlockState state) {
    return false;
  }

  @Override
  public boolean canPlaceBlockAt(World world, BlockPos pos) {
    if (world.getBlockState(pos.up()) == Blocks.AIR.getDefaultState()) {
      return true;
    }
    return false;
  }

  @Override
  public TileEntity createNewTileEntity(World worldIn, int meta) {
    if (meta == 1) {
      return new TileEntityStandingStoneEntangler();
    }
    return null;
  }
}
