package elucent.rootsclassic.entity;

import elucent.rootsclassic.client.particles.MagicAuraParticleData;
import elucent.rootsclassic.registry.RootsEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.Random;

public class EntityAccelerator extends Entity {
  private Entity entity;
  private final Random random = new Random();
  private int lifetime = 0;
  private int potency = 1;

  public EntityAccelerator(EntityType<? extends EntityAccelerator> type, World worldIn) {
    super(type, worldIn);
  }

  public EntityAccelerator(World world, Entity entity, int potency, int size) {
    this(RootsEntities.ENTITY_ACCELERATOR.get(), world);
    this.entity = entity;
    this.potency = potency + 2;
    this.lifetime = 200 + 200 * size;
    this.setPosRaw(entity.getX(), entity.getY(), entity.getZ());
  }

  @Override
  public void tick() {
    super.tick();
    if (entity == null) {
      this.level.broadcastEntityEvent(this, (byte) 3);
      this.remove();
    }
    else {
      this.setPosRaw((entity.getBoundingBox().maxX + entity.getBoundingBox().minX) / 2.0 - 0.5,
              (entity.getBoundingBox().maxY + entity.getBoundingBox().minY) / 2.0 - 0.5,
              (entity.getBoundingBox().maxZ + entity.getBoundingBox().minZ) / 2.0 - 0.5);
      for (int i = 0; i < potency; i++) {
        entity.tick();
        entity.baseTick();
      }
    }
    if(level.isClientSide) {
      for (int i = 0; i < 2; i++) {
        int side = random.nextInt(6);
        if (side == 0) {
          level.addParticle(MagicAuraParticleData.createData(255, 255, 255),
                  getX(), getY() + random.nextDouble(), getZ() + random.nextDouble(), 0, 0, 0);
        }
        if (side == 1) {
          level.addParticle(MagicAuraParticleData.createData(255, 255, 255),
                  getX() + 1.0, getY() + random.nextDouble(), getZ() + random.nextDouble(), 0, 0, 0);
        }
        if (side == 2) {
          level.addParticle(MagicAuraParticleData.createData(255, 255, 255),
                  getX() + random.nextDouble(), getY(), getZ() + random.nextDouble(), 0, 0, 0);
        }
        if (side == 3) {
          level.addParticle(MagicAuraParticleData.createData(255, 255, 255),
                  getX() + random.nextDouble(), getY() + 1.0, getZ() + random.nextDouble(), 0, 0, 0);
        }
        if (side == 4) {
          level.addParticle(MagicAuraParticleData.createData(255, 255, 255),
                  getX() + random.nextDouble(), getY() + random.nextDouble(), getZ(), 0, 0, 0);
        }
        if (side == 5) {
          level.addParticle(MagicAuraParticleData.createData(255, 255, 255),
                  getX() + random.nextDouble(), getY() + random.nextDouble(), getZ() + 1.0, 0, 0, 0);
        }
      }
    }
    lifetime--;
    if (lifetime <= 0) {
      this.level.broadcastEntityEvent(this, (byte) 3);
      this.remove();
    }
  }

  @Override
  protected void defineSynchedData() {}

  @Override
  protected void readAdditionalSaveData(CompoundNBT compound) {
    this.lifetime = compound.getInt("lifetime");
    this.potency = compound.getInt("potency");
  }

  @Override
  protected void addAdditionalSaveData(CompoundNBT compound) {
    compound.putInt("lifetime", lifetime);
    compound.putInt("potency", potency);
  }

  @Override
  public IPacket<?> getAddEntityPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }
}
