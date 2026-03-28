package net.chixozhmix.dnmmod.spells.blood;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.capabilities.magic.*;
import net.chixozhmix.dnmmod.DnMMod;
import net.chixozhmix.dnmmod.Util.SpellUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class SummonUndeadSpiritSpell extends AbstractSpell {

    private static ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DnMMod.MOD_ID, "summon_undead_spirit");

    public SummonUndeadSpiritSpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 10;
        this.spellPowerPerLevel = 3;
        this.castTime = 30;
        this.baseManaCost = 60;
    }

    private DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.RARE)
            .setMaxLevel(3)
            .setSchoolResource(SchoolRegistry.BLOOD_RESOURCE)
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
//        return Optional.of(SoundsRegistry.SUMMON_UNDEAD_SPIRIT.get());
//    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.summon_count", new Object[]{this.getSummonCount(spellLevel, caster)}),
                Component.translatable("ui.dnmmod.spell_component", new Object[]{SpellUtils.getComponentName(Items.SKELETON_SKULL)}));
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
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return SpellUtils.checkSpellComponent(entity, Items.SKELETON_SKULL);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);

//        PlayerRecasts recasts = playerMagicData.getPlayerRecasts();
//        if (!recasts.hasRecastForSpell(this)) {
//            SummonedEntitiesCastData summonedEntitiesCastData = new SummonedEntitiesCastData();
//            int summonTime = 12000;
//            float radius = 1.5F + 0.185F * (float)spellLevel;
//            int count = this.getSummonCount(spellLevel, entity);
//
//            for (int i = 0; i < count; ++i) {
//                SummonedUndeadSpirit undead = new SummonedUndeadSpirit(level, entity, true);
//                undead.finalizeSpawn((ServerLevel)level, level.getCurrentDifficultyAt(undead.getOnPos()), MobSpawnType.MOB_SUMMONED, (SpawnGroupData)null, (CompoundTag)null);
//                float yrot = 6.281F / (float)spellLevel * (float)i + entity.getYRot() * ((float)Math.PI / 180F);
//                Vec3 spawn = Utils.moveToRelativeGroundLevel(level, entity.getEyePosition().add(new Vec3((double)(radius * Mth.cos(yrot)), (double)0.0F, (double)(radius * Mth.sin(yrot)))), 10);
//                undead.setPos(spawn.x, spawn.y, spawn.z);
//                undead.setYRot(entity.getYRot());
//                undead.setOldPosAndRot();
//                level.addFreshEntity(undead);
//                SummonManager.initSummon(entity, undead, summonTime, summonedEntitiesCastData);
//            }
//
//            RecastInstance recastInstance = new RecastInstance(this.getSpellId(), spellLevel, getRecastCount(spellLevel, entity), summonTime, castSource, summonedEntitiesCastData);
//            recasts.addRecast(recastInstance, playerMagicData);
//        }
    }

    public int getSummonCount(int spellLevel, LivingEntity caster) {
        return spellLevel + 2;
    }
}
