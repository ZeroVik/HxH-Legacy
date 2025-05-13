package com.example.hxhmod.entity;

import com.example.hxhmod.HxHMod;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Random;

public class ChimeraAntEntity extends Monster {
    // Sync data for ant type
    private static final EntityDataAccessor<Integer> DATA_ANT_TYPE =
            SynchedEntityData.defineId(ChimeraAntEntity.class, EntityDataSerializers.INT);

    // Ant types
    public enum AntType {
        WORKER,
        SOLDIER,
        OFFICER,
        SQUADRON_LEADER
    }

    public ChimeraAntEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 10;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.ARMOR, 2.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.FOLLOW_RANGE, 35.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ANT_TYPE, AntType.WORKER.ordinal());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("AntType", this.getAntType().ordinal());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("AntType")) {
            int typeOrdinal = tag.getInt("AntType");
            if (typeOrdinal >= 0 && typeOrdinal < AntType.values().length) {
                this.setAntType(AntType.values()[typeOrdinal]);
            }
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType spawnType, @Nullable SpawnGroupData groupData,
                                        @Nullable CompoundTag tag) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnType, groupData, tag);

        // Randomly determine ant type with weighted distribution
        Random random = level.getRandom();
        float chance = random.nextFloat();

        if (chance < 0.05F) {
            // 5% chance for Squadron Leader
            setAntType(AntType.SQUADRON_LEADER);

            // Squadron Leaders are much stronger
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(80.0D);
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(12.0D);
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.35D);
            this.getAttribute(Attributes.ARMOR).setBaseValue(6.0D);

            this.setHealth(this.getMaxHealth());
            this.xpReward = 50;
        } else if (chance < 0.2F) {
            // 15% chance for Officer
            setAntType(AntType.OFFICER);

            // Officers are stronger
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(50.0D);
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(8.0D);
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.33D);
            this.getAttribute(Attributes.ARMOR).setBaseValue(4.0D);

            this.setHealth(this.getMaxHealth());
            this.xpReward = 25;
        } else if (chance < 0.6F) {
            // 40% chance for Soldier
            setAntType(AntType.SOLDIER);

            // Soldiers are stronger than workers
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(40.0D);
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(6.0D);

            this.setHealth(this.getMaxHealth());
            this.xpReward = 15;
        } else {
            // 40% chance for Worker
            setAntType(AntType.WORKER);
        }

        return data;
    }

    public AntType getAntType() {
        return AntType.values()[this.entityData.get(DATA_ANT_TYPE)];
    }

    public void setAntType(AntType type) {
        this.entityData.set(DATA_ANT_TYPE, type.ordinal());
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean success = super.doHurtTarget(target);

        if (success && target instanceof LivingEntity) {
            LivingEntity livingTarget = (LivingEntity) target;

            // Different effects based on ant type
            switch (getAntType()) {
                case SQUADRON_LEADER:
                    // Squadron leaders can apply stronger effects
                    livingTarget.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 1));
                    livingTarget.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));
                    break;
                case OFFICER:
                    // Officers can apply weakness
                    livingTarget.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 0));
                    break;
                case SOLDIER:
                    // Soldiers can apply brief slowness
                    livingTarget.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 0));
                    break;
                default:
                    // Workers have no special attack effect
                    break;
            }
        }

        return success;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SPIDER_AMBIENT;  // Placeholder until custom sounds are added
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.SPIDER_HURT;  // Placeholder until custom sounds are added
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SPIDER_DEATH;  // Placeholder until custom sounds are added
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockState) {
        this.playSound(SoundEvents.SPIDER_STEP, 0.15F, 1.0F);  // Placeholder until custom sounds are added
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effectInstance) {
        // Squadron leaders are immune to poison and weakness
        if (getAntType() == AntType.SQUADRON_LEADER) {
            if (effectInstance.getEffect() == MobEffects.POISON ||
                    effectInstance.getEffect() == MobEffects.WEAKNESS) {
                return false;
            }
        }

        return super.canBeAffected(effectInstance);
    }
}