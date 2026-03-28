package net.chixozhmix.neodnm.spells.nature;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.*;
import net.chixozhmix.neodnm.DnMMod;
import net.chixozhmix.neodnm.Util.SpellUtils;
import net.chixozhmix.neodnm.entity.summoned.SummonedRavenEntity;
import net.chixozhmix.neodnm.registers.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class SummonRavenSpell extends AbstractSpell {

    private static ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "summon_ravens");

    public SummonRavenSpell() {

        this.manaCostPerLevel = 10;
        this.baseSpellPower = 10;
        this.spellPowerPerLevel = 3;
        this.castTime = 30;
        this.baseManaCost = 60;
    }

    private DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.UNCOMMON)
            .setMaxLevel(10)
            .setSchoolResource(SchoolRegistry.NATURE_RESOURCE)
            .setCooldownSeconds(120)
            .build();

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
    }

//    @Override
//    public Optional<SoundEvent> getCastFinishSound() {
//        return Optional.of(SoundsRegistry.SUMMON_RAVEN.get());
//    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.summon_count", new Object[]{this.getSummonCount(spellLevel, caster)}),
                Component.translatable("ui.dnmmod.spell_component", new Object[]{SpellUtils.getComponentName(ModItems.RAVEN_FEATHER.get())})
        );
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return SpellUtils.checkSpellComponent(entity, ModItems.RAVEN_FEATHER.get());
    }

    @Override
    public int getRecastCount(int spellLevel, @Nullable LivingEntity entity) {
        return 2;
    }

    @Override
    public void onRecastFinished(ServerPlayer serverPlayer, RecastInstance recastInstance, RecastResult recastResult, ICastDataSerializable castDataSerializable) {
        if (SummonManager.recastFinishedHelper(serverPlayer, recastInstance, recastResult, castDataSerializable)) {
            super.onRecastFinished(serverPlayer, recastInstance, recastResult, castDataSerializable);
        }
    }

    @Override
    public ICastDataSerializable getEmptyCastData() {
        return new SummonedEntitiesCastData();
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);

        PlayerRecasts recasts = playerMagicData.getPlayerRecasts();
        if (!recasts.hasRecastForSpell(this)) {
            SummonedEntitiesCastData summonedEntitiesCastData = new SummonedEntitiesCastData();
            int summonTime = 12000;
            float radius = 1.5F + 0.185F * (float)spellLevel;
            int count = this.getSummonCount(spellLevel, entity);

            for (int i = 0; i < count; i++) {
                SummonedRavenEntity raven = new SummonedRavenEntity(level, entity, true);
                raven.finalizeSpawn((ServerLevel)level, level.getCurrentDifficultyAt(raven.getOnPos()), MobSpawnType.MOB_SUMMONED, (SpawnGroupData)null);
                float yrot = 6.281F / (float)spellLevel * (float)i + entity.getYRot() * ((float)Math.PI / 180F);
                Vec3 spawn = Utils.moveToRelativeGroundLevel(level, entity.getEyePosition().add(new Vec3((double)(radius * Mth.cos(yrot)), (double)0.0F, (double)(radius * Mth.sin(yrot)))), 10);
                raven.setPos(spawn.x, spawn.y + 3.0f, spawn.z);
                raven.setYRot(entity.getYRot());
                raven.setOldPosAndRot();
                level.addFreshEntity(raven);
                SummonManager.initSummon(entity, raven, summonTime, summonedEntitiesCastData);
            }
            RecastInstance recastInstance = new RecastInstance(this.getSpellId(), spellLevel, getRecastCount(spellLevel, entity), summonTime, castSource, summonedEntitiesCastData);
            recasts.addRecast(recastInstance, playerMagicData);
        }
    }

    public int getSummonCount(int spellLevel, LivingEntity caster) {
        return spellLevel + 2;
    }
}
