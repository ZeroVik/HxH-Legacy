package com.example.hxhmod.particle;

import com.example.hxhmod.HxHMod;
import com.example.hxhmod.capabilities.PlayerNen;
import com.example.hxhmod.nen.NenType;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Custom particle for Nen aura visualization
 */
public class NenAuraParticle extends TextureSheetParticle {
    private final double xStart;
    private final double yStart;
    private final double zStart;
    private final NenType nenType;

    protected NenAuraParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, NenType nenType) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);

        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
        this.xStart = x;
        this.yStart = y;
        this.zStart = z;
        this.nenType = nenType;

        this.lifetime = 20 + random.nextInt(20); // 1-2 seconds

        // Set particle color based on Nen type
        if (nenType != null) {
            switch (nenType) {
                case ENHANCER:
                    setColor(0.9F, 0.1F, 0.1F); // Red
                    break;
                case TRANSMUTER:
                    setColor(0.9F, 0.9F, 0.1F); // Yellow
                    break;
                case CONJURER:
                    setColor(0.1F, 0.5F, 0.9F); // Blue
                    break;
                case EMITTER:
                    setColor(0.9F, 0.5F, 0.1F); // Orange
                    break;
                case MANIPULATOR:
                    setColor(0.1F, 0.8F, 0.1F); // Green
                    break;
                case SPECIALIST:
                    setColor(0.8F, 0.1F, 0.8F); // Purple
                    break;
                default:
                    setColor(1.0F, 1.0F, 1.0F); // White (default)
            }
        } else {
            setColor(0.8F, 0.8F, 0.8F); // Light gray for undefined
        }

        // Adjust alpha for varying transparency
        this.alpha = 0.7F;

        // Set particle scale
        this.quadSize = 0.1F + random.nextFloat() * 0.1F;

        this.hasPhysics = false; // Ignore blocks, flow with the player
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }

        // Fade out near the end of lifetime
        if (this.age > this.lifetime * 0.7F) {
            this.alpha = (this.lifetime - this.age) / (float) (this.lifetime * 0.3F);
        }

        // Move the particle
        this.move(this.xd, this.yd, this.zd);

        // Slow down movement over time
        this.xd *= 0.95;
        this.yd *= 0.95;
        this.zd *= 0.95;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    /**
     * Factory for creating NenAura particles
     */
    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            NenAuraParticle particle = new NenAuraParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, null);
            particle.pickSprite(this.sprites);
            return particle;
        }
    }

    /**
     * Utility class for spawning Nen-related particles
     */
    public static class NenParticleHandler {
        private static final Random random = new Random();

        /**
         * Spawn Nen aura particles around a player
         */
        public static void spawnNenAuraParticles(Player player, PlayerNen.NenState nenState) {
            if (player.level.isClientSide) {
                // Get player's Nen capability
                player.getCapability(PlayerNen.CAPABILITY).ifPresent(playerNen -> {
                    // Only spawn particles if Nen is awakened
                    if (!playerNen.isNenAwakened()) return;

                    NenType nenType = playerNen.getNenType();

                    // Different patterns based on Nen state
                    int particleCount;
                    float spreadFactor;
                    float speedFactor;

                    switch (nenState) {
                        case TEN:
                            particleCount = 5;
                            spreadFactor = 0.5F;
                            speedFactor = 0.02F;
                            break;
                        case REN:
                            particleCount = 15;
                            spreadFactor = 0.8F;
                            speedFactor = 0.05F;
                            break;
                        case ZETSU:
                            return; // No particles in Zetsu
                        case HATSU:
                            particleCount = 10;
                            spreadFactor = 0.7F;
                            speedFactor = 0.03F;
                            break;
                        default:
                            particleCount = 2;
                            spreadFactor = 0.3F;
                            speedFactor = 0.01F;
                    }

                    // Spawn particles around the player
                    for (int i = 0; i < particleCount; i++) {
                        double offsetX = (random.nextDouble() - 0.5) * spreadFactor;
                        double offsetY = random.nextDouble() * player.getBbHeight();
                        double offsetZ = (random.nextDouble() - 0.5) * spreadFactor;

                        double speedX = (random.nextDouble() - 0.5) * speedFactor;
                        double speedY = random.nextDouble() * speedFactor;
                        double speedZ = (random.nextDouble() - 0.5) * speedFactor;

                        // This would need to be replaced with actual particle spawning code
                        // player.level.addParticle(particleType,
                        //     player.getX() + offsetX,
                        //     player.getY() + offsetY,
                        //     player.getZ() + offsetZ,
                        //     speedX, speedY, speedZ);
                    }
                });
            }
        }

        /**
         * Spawn particles for the water divination effect
         */
        public static void spawnWaterDivinationParticles(ClientLevel level, double x, double y, double z, NenType type) {
            if (level.isClientSide) {
                int particleCount = 50;

                for (int i = 0; i < particleCount; i++) {
                    double offsetX = (random.nextDouble() - 0.5) * 0.5;
                    double offsetY = (random.nextDouble() - 0.5) * 0.5;
                    double offsetZ = (random.nextDouble() - 0.5) * 0.5;

                    double speedX = (random.nextDouble() - 0.5) * 0.05;
                    double speedY = random.nextDouble() * 0.1;
                    double speedZ = (random.nextDouble() - 0.5) * 0.05;

                    // Custom particle effect based on Nen type
                    // This would need to be replaced with actual particle spawning code
                    // level.addParticle(particleType,
                    //     x + offsetX,
                    //     y + offsetY,
                    //     z + offsetZ,
                    //     speedX, speedY, speedZ);
                }
            }
        }

        /**
         * Spawn particles for Jajanken Rock ability
         */
        public static void spawnJajankenRockParticles(Player player) {
            if (player.level.isClientSide) {
                // Concentrated particles around the fist
                int particleCount = 30;

                for (int i = 0; i < particleCount; i++) {
                    double offsetX = (random.nextDouble() - 0.5) * 0.3;
                    double offsetY = (random.nextDouble() - 0.5) * 0.3;
                    double offsetZ = (random.nextDouble() - 0.5) * 0.3;

                    // Calculate position in front of player (for the punching hand)
                    double handX = player.getX() + player.getLookAngle().x * 0.7 + offsetX;
                    double handY = player.getY() + 1.2 + offsetY;
                    double handZ = player.getZ() + player.getLookAngle().z * 0.7 + offsetZ;

                    double speedX = (random.nextDouble() - 0.5) * 0.1;
                    double speedY = (random.nextDouble() - 0.5) * 0.1;
                    double speedZ = (random.nextDouble() - 0.5) * 0.1;

                    // This would need to be replaced with actual particle spawning code
                    // player.level.addParticle(particleType,
                    //     handX, handY, handZ,
                    //     speedX, speedY, speedZ);
                }
            }
        }
    }
}