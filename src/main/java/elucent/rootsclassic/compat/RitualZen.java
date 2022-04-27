package elucent.rootsclassic.compat;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import elucent.rootsclassic.ritual.RitualBase;
import elucent.rootsclassic.ritual.RitualBaseRegistry;
import elucent.rootsclassic.ritual.rituals.RitualCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;
import org.openzen.zencode.java.ZenCodeType.Method;
import org.openzen.zencode.java.ZenCodeType.Name;

import java.util.Arrays;

@ZenRegister
@Name("mods.rootsclassic.Ritual")
public class RitualZen {

  public RitualZen() {}

  @Method
  public static void addCraftingRitual(String uniqueName, IItemStack output, int level, double r, double g, double b,
      IItemStack[] incenses, IItemStack[] ingredients) {
    RitualCrafting newCraft = new RitualCrafting(level, r, g, b);
    newCraft.setRegistryName(new ResourceLocation("crafttweaker", uniqueName));
    newCraft.setResult(output.getInternal());
    newCraft.setIncenses(Arrays.asList(convertToStacks(incenses)));
    newCraft.setIngredients(Arrays.asList(convertToStacks(ingredients)));

    ((ForgeRegistry<RitualBase>) RitualBaseRegistry.RITUALS).unfreeze();
    RitualBaseRegistry.RITUALS.register(newCraft);
    ((ForgeRegistry<RitualBase>) RitualBaseRegistry.RITUALS).freeze();
  }

  @Method
  public static void setPrimaryColor(ResourceLocation name, double r, double g, double b) {
    RitualBase found = findRitualByName(name);
    found.setPrimaryColor(r, g, b);
  }

  @Method
  public static void setSecondaryColor(ResourceLocation name, double r, double g, double b) {
    RitualBase found = findRitualByName(name);
    found.setSecondaryColor(r, g, b);
  }

  @Method
  public static void setLevel(ResourceLocation name, int level) {
    RitualBase found = findRitualByName(name);
    found.setLevel(level);
  }

  /**
   * Invalid ritual, names must be one of: staff_crafting, sylvan_hood_crafting, sylvan_chest_crafting, sylvan_legs_crafting, sylvan_boots_crafting, wildwood_head_crafting, wildwood_chest_crafting,
   * wildwood_legs_crafting, wildwood_boots_crafting, accelerator_stone_crafting, standing_stone, entangler_stone_crafting,grower_stone_crafting, healer_stone_crafting, igniter_stone_crafting,
   * repulsor_stone_crafting, vacuum_stone_crafting, runic_focus_crafting, runic_focus_charging, living_pickaxe_crafting, living_axe_crafting, living_sword_crafting, living_hoe_crafting,
   * living_shovel_crafting, cause_rain, banish_rain, mass_breeding, life_drain, imbuer, cow_summoning, pig_summoning, sheep_summoning, chicken_summoning, rabbit_summoning, zombie_summoning,
   * skeleton_summoning, spider_summoning, cavespider_summoning, slime_summoning, creeper_summoning, enderman_summoning, sacrifice,flare, grow, engraved_crafting, time_shift
   *
   * Starting with rootsclassic (Example: "rootsclassic:staff_crafting")
   */
  @Method
  public static void setRitualIngredients(ResourceLocation name, IItemStack[] items) {
    RitualBase found = findRitualByName(name);
    CraftTweakerAPI.logInfo("Changing Ritual ingredients " + name);
    found.setIngredients(Arrays.asList(convertToStacks(items)));
  }

  @Method
  public static void setRitualIncense(ResourceLocation name, IItemStack[] items) {
    RitualBase found = findRitualByName(name);
    CraftTweakerAPI.logInfo("Changing Ritual incense " + name);
    found.setIncenses(Arrays.asList(convertToStacks(items)));
  }

  private static RitualBase findRitualByName(ResourceLocation name) {
    RitualBase found = RitualBaseRegistry.RITUALS.getValue(name);
    if (found == null) {
      StringBuilder names = new StringBuilder();
      for (RitualBase c : RitualBaseRegistry.RITUALS.getValues()) {
        names.append(c.getRegistryName()).append(",");
      }
      CraftTweakerAPI.logInfo(names.toString());
      throw new IllegalArgumentException("Invalid ritual[" + name + "], names must be one of: " + names);
    }
    return found;
  }

  private static ItemStack[] convertToStacks(IItemStack[] stacks) {
    if (stacks == null) {
      return null;
    }
    else {
      ItemStack[] output = new ItemStack[stacks.length];
      for (int i = 0; i < stacks.length; i++) {
        output[i] = stacks[i].getInternal();
      }
      return output;
    }
  }
}
