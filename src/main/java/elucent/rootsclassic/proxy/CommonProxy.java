package elucent.rootsclassic.proxy;

import elucent.rootsclassic.RegistryManager;
import elucent.rootsclassic.Roots;
import elucent.rootsclassic.Util;
import elucent.rootsclassic.capability.RootsCapabilityManager;
import elucent.rootsclassic.gui.GuiHandler;
import elucent.rootsclassic.mutation.MutagenManager;
import elucent.rootsclassic.research.ResearchManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {

  public void preInit(FMLPreInitializationEvent event) {
    RegistryManager.init();//items/blocks
    RootsCapabilityManager.preInit();
  }

  public void init(FMLInitializationEvent event) {
    RegistryManager.registerEntities();
    MutagenManager.init();
  }

  public void postInit(FMLPostInitializationEvent event) {
    Util.initBerries();
    NetworkRegistry.INSTANCE.registerGuiHandler(Roots.instance, new GuiHandler());
    ResearchManager.init();
  }

  public void spawnParticleMagicFX(World world, double x, double y, double z, double vx, double vy, double vz, double r, double g, double b) {
    //
  }

  public void spawnParticleMagicLineFX(World world, double x, double y, double z, double vx, double vy, double vz, double r, double g, double b) {
    //
  }

  public void spawnParticleMagicAltarLineFX(World world, double x, double y, double z, double vx, double vy, double vz, double r, double g, double b) {
    //
  }

  public void spawnParticleMagicAltarFX(World world, double x, double y, double z, double vx, double vy, double vz, double r, double g, double b) {
    //
  }

  public void spawnParticleMagicAuraFX(World world, double x, double y, double z, double vx, double vy, double vz, double r, double g, double b) {
    //
  }
}
