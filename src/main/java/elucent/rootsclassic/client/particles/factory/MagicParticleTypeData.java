package elucent.rootsclassic.client.particles.factory;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import elucent.rootsclassic.client.particles.ParticleColor;
import elucent.rootsclassic.registry.ParticleRegistry;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;

public class MagicParticleTypeData implements IParticleData {

  private ParticleType<MagicParticleTypeData> type;
  public static final Codec<MagicParticleTypeData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      Codec.FLOAT.fieldOf("r").forGetter(d -> d.color.getRed()),
      Codec.FLOAT.fieldOf("g").forGetter(d -> d.color.getGreen()),
      Codec.FLOAT.fieldOf("b").forGetter(d -> d.color.getBlue()))
      .apply(instance, MagicParticleTypeData::new));
  public ParticleColor color;
  @SuppressWarnings("deprecation")
  static final IParticleData.IDeserializer<MagicParticleTypeData> DESERIALIZER = new IParticleData.IDeserializer<MagicParticleTypeData>() {

    @Override
    public MagicParticleTypeData fromCommand(ParticleType<MagicParticleTypeData> type, StringReader reader) throws CommandSyntaxException {
      reader.expect(' ');
      return new MagicParticleTypeData(type, ParticleColor.deserialize(reader.readString()));
    }

    @Override
    public MagicParticleTypeData fromNetwork(ParticleType<MagicParticleTypeData> type, PacketBuffer buffer) {
      return new MagicParticleTypeData(type, ParticleColor.deserialize(buffer.readUtf()));
    }
  };

  public MagicParticleTypeData(ParticleType<MagicParticleTypeData> particleTypeData, ParticleColor color) {
    this.type = particleTypeData;
    this.color = color;
  }

  public MagicParticleTypeData(float r, float g, float b) {
    this(ParticleRegistry.MAGIC_TYPE.get(), new ParticleColor(r, g, b));
  }

  @Override
  public ParticleType<?> getType() {
    return type;
  }

  @Override
  public void writeToNetwork(PacketBuffer buffer) {}

  @Override
  public String writeToString() {
    return null;
  }
}
