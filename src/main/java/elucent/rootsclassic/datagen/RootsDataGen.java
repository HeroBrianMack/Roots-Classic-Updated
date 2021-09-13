package elucent.rootsclassic.datagen;

import static elucent.rootsclassic.registry.RootsRegistry.ACCELERATOR_STANDING_STONE;
import static elucent.rootsclassic.registry.RootsRegistry.AESTHETIC_STANDING_STONE;
import static elucent.rootsclassic.registry.RootsRegistry.ALTAR;
import static elucent.rootsclassic.registry.RootsRegistry.ATTUNED_STANDING_STONE;
import static elucent.rootsclassic.registry.RootsRegistry.BARK_KNIFE;
import static elucent.rootsclassic.registry.RootsRegistry.BLACKCURRANT;
import static elucent.rootsclassic.registry.RootsRegistry.BRAZIER;
import static elucent.rootsclassic.registry.RootsRegistry.DRAGONS_EYE;
import static elucent.rootsclassic.registry.RootsRegistry.ELDERBERRY;
import static elucent.rootsclassic.registry.RootsRegistry.ENTANGLER_STANDING_STONE;
import static elucent.rootsclassic.registry.RootsRegistry.FLARE_ORCHID;
import static elucent.rootsclassic.registry.RootsRegistry.FRUIT_SALAD;
import static elucent.rootsclassic.registry.RootsRegistry.GROWER_STANDING_STONE;
import static elucent.rootsclassic.registry.RootsRegistry.GROWTH_POWDER;
import static elucent.rootsclassic.registry.RootsRegistry.HEALER_STANDING_STONE;
import static elucent.rootsclassic.registry.RootsRegistry.HEALING_POULTICE;
import static elucent.rootsclassic.registry.RootsRegistry.IGNITER_STANDING_STONE;
import static elucent.rootsclassic.registry.RootsRegistry.IMBUER;
import static elucent.rootsclassic.registry.RootsRegistry.MIDNIGHT_BLOOM;
import static elucent.rootsclassic.registry.RootsRegistry.MORTAR;
import static elucent.rootsclassic.registry.RootsRegistry.MUNDANE_STANDING_STONE;
import static elucent.rootsclassic.registry.RootsRegistry.MUTATING_POWDER;
import static elucent.rootsclassic.registry.RootsRegistry.OLD_ROOT;
import static elucent.rootsclassic.registry.RootsRegistry.PESTLE;
import static elucent.rootsclassic.registry.RootsRegistry.RADIANT_DAISY;
import static elucent.rootsclassic.registry.RootsRegistry.REDCURRANT;
import static elucent.rootsclassic.registry.RootsRegistry.REPULSOR_STANDING_STONE;
import static elucent.rootsclassic.registry.RootsRegistry.ROOTY_STEW;
import static elucent.rootsclassic.registry.RootsRegistry.RUNIC_TABLET;
import static elucent.rootsclassic.registry.RootsRegistry.VACUUM_STANDING_STONE;
import static elucent.rootsclassic.registry.RootsRegistry.VERDANT_SPRIG;
import static elucent.rootsclassic.registry.RootsRegistry.WHITECURRANT;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.EntityLoot;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import elucent.rootsclassic.Const;
import elucent.rootsclassic.block.AttunedStandingStoneBlock;
import elucent.rootsclassic.lootmodifiers.DropModifier;
import elucent.rootsclassic.lootmodifiers.DropModifier.BlockDropModifier;
import elucent.rootsclassic.registry.RootsEntities;
import elucent.rootsclassic.registry.RootsRegistry;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RootsDataGen {

  @SubscribeEvent
  public static void gatherData(GatherDataEvent event) {
    DataGenerator generator = event.getGenerator();
    //		ExistingFileHelper helper = event.getExistingFileHelper();
    if (event.includeServer()) {
      generator.addProvider(new Loots(generator));
      generator.addProvider(new Recipes(generator));
      generator.addProvider(new GLMProvider(generator));
    }
    if (event.includeClient()) {
      //			generator.addProvider(new Language(generator));
      //			generator.addProvider(new BlockStates(generator, helper));
      //			generator.addProvider(new ItemModels(generator, helper));
    }
  }

  private static class Loots extends LootTableProvider {

    public Loots(DataGenerator gen) {
      super(gen);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
      return ImmutableList.of(Pair.of(RootsBlockLoot::new, LootContextParamSets.BLOCK), Pair.of(RootsEntityLoot::new, LootContextParamSets.ENTITY));
    }

    private class RootsBlockLoot extends BlockLoot {

      @Override
      protected void addTables() {
        this.dropSelf(MORTAR.get());
        this.dropSelf(ALTAR.get());
        this.dropSelf(BRAZIER.get());
        this.dropSelf(IMBUER.get());
        this.dropSelf(MUNDANE_STANDING_STONE.get());
        this.dropSelf(MIDNIGHT_BLOOM.get());
        this.dropSelf(FLARE_ORCHID.get());
        this.dropSelf(RADIANT_DAISY.get());
        this.add(ATTUNED_STANDING_STONE.get(), this::registerStandingStone);
        this.add(VACUUM_STANDING_STONE.get(), this::registerStandingStone);
        this.add(REPULSOR_STANDING_STONE.get(), this::registerStandingStone);
        this.add(ACCELERATOR_STANDING_STONE.get(), this::registerStandingStone);
        this.add(AESTHETIC_STANDING_STONE.get(), this::registerStandingStone);
        this.add(ENTANGLER_STANDING_STONE.get(), this::registerStandingStone);
        this.add(IGNITER_STANDING_STONE.get(), this::registerStandingStone);
        this.add(GROWER_STANDING_STONE.get(), this::registerStandingStone);
        this.add(HEALER_STANDING_STONE.get(), this::registerStandingStone);
      }

      public LootTable.Builder registerStandingStone(Block standingStone) {
        return createSinglePropConditionTable(standingStone, AttunedStandingStoneBlock.HALF, DoubleBlockHalf.LOWER);
      }

      @Override
      protected Iterable<Block> getKnownBlocks() {
        return (Iterable<Block>) RootsRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
      }
    }

    private class RootsEntityLoot extends EntityLoot {

      @Override
      protected void addTables() {
        this.add(RootsEntities.PHANTOM_SKELETON.get(), LootTable.lootTable());
      }

      @Override
      protected boolean isNonLiving(EntityType<?> entityType) {
        return entityType.getCategory() == MobCategory.MISC;
      }

      @Override
      protected Iterable<EntityType<?>> getKnownEntities() {
        Stream<EntityType<?>> entities = RootsEntities.ENTITIES.getEntries().stream().map(RegistryObject::get);
        return (Iterable<EntityType<?>>) entities::iterator;
      }
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, @Nonnull ValidationContext validationtracker) {
      map.forEach((name, table) -> LootTables.validate(validationtracker, name, table));
    }
  }

  private static class GLMProvider extends GlobalLootModifierProvider {

    public GLMProvider(DataGenerator gen) {
      super(gen, Const.MODID);
    }

    @Override
    protected void start() {
      add("rootsclassic_drops", DropModifier.ROOTSCLASSIC_DROPS.get(), new BlockDropModifier(
          new LootItemCondition[] {
              InvertedLootItemCondition.invert(MatchTool.toolMatches(ItemPredicate.Builder.item().of(Tags.Items.SHEARS))).build()
          }));
    }
  }

  private static class Recipes extends RecipeProvider {

    public Recipes(DataGenerator gen) {
      super(gen);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
      ShapedRecipeBuilder.shaped(PESTLE.get()).define('X', Blocks.DIORITE).group("pestle")
          .pattern("X  ").pattern(" XX").pattern(" XX").unlockedBy("has_diorite", has(Blocks.DIORITE)).save(consumer);
      ShapedRecipeBuilder.shaped(PESTLE.get()).define('X', Blocks.DIORITE).group("pestle")
          .pattern("  X").pattern("XX ").pattern("XX ").unlockedBy("has_diorite", has(Blocks.DIORITE))
          .save(consumer, Registry.ITEM.getKey(PESTLE.get()) + "2");
      ShapedRecipeBuilder.shaped(MORTAR.get()).define('X', Tags.Items.STONE)
          .pattern("X X").pattern("X X").pattern(" X ").unlockedBy("has_stone", has(Tags.Items.STONE)).save(consumer);
      ShapedRecipeBuilder.shaped(IMBUER.get()).define('X', Tags.Items.RODS_WOODEN).define('L', ItemTags.LOGS).define('S', Blocks.CHISELED_STONE_BRICKS)
          .pattern("X X").pattern("LSL").unlockedBy("has_chiseled_stone_bricks", has(Blocks.CHISELED_STONE_BRICKS)).save(consumer);
      ShapedRecipeBuilder.shaped(MUNDANE_STANDING_STONE.get()).define('S', Tags.Items.STONE).define('B', Blocks.STONE_BRICKS).define('L', Tags.Items.STORAGE_BLOCKS_LAPIS)
          .pattern("SBS").pattern("BLB").pattern("SBS").unlockedBy("has_lapis_block", has(Tags.Items.STORAGE_BLOCKS_LAPIS)).save(consumer);
      ShapedRecipeBuilder.shaped(ATTUNED_STANDING_STONE.get()).define('S', Tags.Items.STONE).define('N', Tags.Items.INGOTS_NETHER_BRICK).define('D', Tags.Items.GEMS_DIAMOND)
          .pattern("SNS").pattern("NDN").pattern("SNS").unlockedBy("has_diamond", has(Tags.Items.GEMS_DIAMOND)).save(consumer);
      ShapedRecipeBuilder.shaped(BRAZIER.get())
          .define('I', Tags.Items.INGOTS_IRON).define('S', Tags.Items.STRING).define('C', Items.CAULDRON).define('X', Tags.Items.RODS_WOODEN)
          .pattern("ISI").pattern("ICI").pattern("IXI").unlockedBy("has_cauldron", has(Items.CAULDRON)).save(consumer);
      ShapedRecipeBuilder.shaped(ALTAR.get())
          .define('S', Tags.Items.STONE).define('F', Items.POPPY).define('B', VERDANT_SPRIG.get())
          .define('G', Tags.Items.STORAGE_BLOCKS_GOLD).define('C', Blocks.CHISELED_STONE_BRICKS)
          .pattern("BFB").pattern("SGS").pattern(" C ").unlockedBy("has_gold_block", has(Tags.Items.STORAGE_BLOCKS_GOLD)).save(consumer);
      ShapedRecipeBuilder.shaped(BARK_KNIFE.get())
          .define('S', Tags.Items.RODS_WOODEN).define('V', ItemTags.SAPLINGS).define('P', ItemTags.PLANKS)
          .pattern(" VV").pattern("VPV").pattern("SV ").unlockedBy("has_sapling", has(ItemTags.SAPLINGS)).save(consumer);
      ShapedRecipeBuilder.shaped(RUNIC_TABLET.get())
          .define('S', Tags.Items.SEEDS_WHEAT).define('B', Tags.Items.STONE).define('R', OLD_ROOT.get())
          .pattern(" R ").pattern("SBS").pattern(" S ").unlockedBy("has_old_root", has(OLD_ROOT.get())).save(consumer);
      ShapelessRecipeBuilder.shapeless(GROWTH_POWDER.get(), 4)
          .requires(Tags.Items.SEEDS_WHEAT).requires(Items.GRASS).requires(Tags.Items.DUSTS_REDSTONE).requires(PESTLE.get())
          .unlockedBy("has_pestle", has(PESTLE.get())).save(consumer);
      ShapelessRecipeBuilder.shapeless(MUTATING_POWDER.get())
          .requires(GROWTH_POWDER.get()).requires(GROWTH_POWDER.get()).requires(GROWTH_POWDER.get()).requires(GROWTH_POWDER.get())
          .requires(Tags.Items.NETHER_STARS).requires(Tags.Items.CROPS_NETHER_WART).requires(PESTLE.get())
          .unlockedBy("has_pestle", has(PESTLE.get())).save(consumer);
      ShapelessRecipeBuilder.shapeless(ROOTY_STEW.get())
          .requires(Tags.Items.CROPS_WHEAT).requires(Items.BOWL).requires(OLD_ROOT.get())
          .unlockedBy("has_bowl", has(Items.BOWL)).save(consumer);
      ShapelessRecipeBuilder.shapeless(FRUIT_SALAD.get())
          .requires(Items.MELON).requires(Items.MELON).requires(Items.MELON)
          .requires(Items.APPLE).requires(Items.BOWL).requires(ELDERBERRY.get())
          .requires(WHITECURRANT.get()).requires(BLACKCURRANT.get()).requires(REDCURRANT.get())
          .unlockedBy("has_bowl", has(Items.BOWL)).save(consumer);
      ShapelessRecipeBuilder.shapeless(HEALING_POULTICE.get(), 2)
          .requires(REDCURRANT.get()).requires(Items.PAPER).requires(PESTLE.get()).requires(VERDANT_SPRIG.get())
          .unlockedBy("has_pestle", has(PESTLE.get())).save(consumer);
      SimpleCookingRecipeBuilder.smelting(Ingredient.of(DRAGONS_EYE.get()),
          Items.ENDER_PEARL, 1F, 200).unlockedBy("has_dragons_eye", has(DRAGONS_EYE.get()))
          .save(consumer, "rootsclassic:ender_pearl");
    }

    @Override
    protected void saveAdvancement(HashCache cache, JsonObject advancementJson, Path path) {
      // Nope
    }
  }
}
