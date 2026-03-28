package net.chixozhmix.dnmmod.registers;

import io.redspace.ironsspellbooks.api.item.weapons.ExtendedSwordItem;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.compat.Curios;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.item.weapons.StaffItem;
import net.chixozhmix.dnmmod.DnMMod;
import net.chixozhmix.dnmmod.Util.PropertiesHelper;
import net.chixozhmix.dnmmod.items.custom.*;
import net.chixozhmix.dnmmod.items.weapons.WandTier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(DnMMod.MOD_ID);

    private static DeferredItem<Item> registerSword(String name, Tier tier, int damage, float speed, int durability) {
        return ITEMS.register(name, () ->
                new SwordItem(tier,
                        new Item.Properties().durability(durability)
                                .attributes(SwordItem.createAttributes(tier, damage, speed))));
    }
    private static DeferredItem<Item> registerAxe(String name, Tier tier, float damage, float speed, int durability) {
        return ITEMS.register(name, () ->
                new AxeItem(tier,
                        new Item.Properties().durability(durability)
                                .attributes(AxeItem.createAttributes(tier, damage, speed))));
    }

    //Weapons
    public static final DeferredItem<Item> IRON_DAGGER = registerSword("iron_dagger", Tiers.IRON,
            1, -1.4f, 300);
    public static final DeferredItem<Item> IRON_KLEVETS = registerSword("iron_klevets", Tiers.IRON,
            3, -1.6f, 330);
    public static final DeferredItem<Item> IRON_SICKLE = registerSword("iron_sickle", Tiers.IRON,
            2, -1.5f, 320);
    public static final DeferredItem<Item> IRON_MACE = registerSword("iron_mace", Tiers.IRON,
            2, -2.0f, 360);
    public static final DeferredItem<Item> IRON_SCIMITAR = registerSword("iron_scimitar", Tiers.IRON,
            3, -1.8f, 340);
    public static final DeferredItem<Item> IRON_BATTLEAXE = registerAxe("iron_battleaxe", Tiers.IRON,
            3.5F, -2.5f, 260);
    public static final DeferredItem<Item> IRON_SPEAR = registerSword("iron_spear", Tiers.IRON,
            4, -2.4f, 350);
    public static final DeferredItem<Item> IRON_HALBERD = registerSword("iron_halberd", Tiers.IRON,
            4, -2.0f, 310);
    public static final DeferredItem<Item> IRON_GREATAXE = registerAxe("iron_greataxe", Tiers.IRON,
            9, -3.4f, 400);
    public static final DeferredItem<Item> IRON_GREATSWORD = registerSword("iron_greatsword", Tiers.IRON,
            8, -2.8f, 460);
    public static final DeferredItem<Item> IRON_GLAIVE = registerSword("iron_glaive", Tiers.IRON,
            4, -2.4f, 460);
    public static final DeferredItem<Item> IRON_TRIDENT = registerSword("iron_trident", Tiers.IRON,
            4, -2.6f, 430);
    public static final DeferredItem<Item> IRON_KATANA = registerSword("iron_katana", Tiers.IRON,
            4, -2.0f, 330);
    public static final DeferredItem<Item> DIAMOND_DAGGER = registerSword("diamond_dagger", Tiers.DIAMOND,
            1, -1.4f, 1200);
    public static final DeferredItem<Item> DIAMOND_KLEVETS = registerSword("diamond_klevets", Tiers.DIAMOND,
            3, -1.6f, 1130);
    public static final DeferredItem<Item> DIAMOND_SICKLE = registerSword("diamond_sickle", Tiers.DIAMOND,
            2, -1.5f, 1020);
    public static final DeferredItem<Item> DIAMOND_MACE = registerSword("diamond_mace", Tiers.DIAMOND,
            2, -2.0f, 1160);
    public static final DeferredItem<Item> DIAMOND_SCIMITAR = registerSword("diamond_scimitar", Tiers.DIAMOND,
            3, -1.8f, 1040);
    public static final DeferredItem<Item> DIAMOND_BATTLEAXE = registerAxe("diamond_battleaxe", Tiers.DIAMOND,
            3.5F, -2.5f, 660);
    public static final DeferredItem<Item> DIAMOND_SPEAR = registerSword("diamond_spear", Tiers.DIAMOND,
            4, -2.4f, 1250);
    public static final DeferredItem<Item> DIAMOND_HALBERD = registerSword("diamond_halberd", Tiers.DIAMOND,
            4, -2.0f, 1110);
    public static final DeferredItem<Item> DIAMOND_GREATAXE = registerAxe("diamond_greataxe", Tiers.DIAMOND,
            9, -3.4f, 1500);
    public static final DeferredItem<Item> DIAMOND_GREATSWORD = registerSword("diamond_greatsword", Tiers.DIAMOND,
            8, -2.8f, 1560);
    public static final DeferredItem<Item> DIAMOND_GLAIVE = registerSword("diamond_glaive", Tiers.DIAMOND,
            4, -2.4f, 1300);
    public static final DeferredItem<Item> DIAMOND_TRIDENT = registerSword("diamond_trident", Tiers.DIAMOND,
            4, -2.6f, 1230);
    public static final DeferredItem<Item> DIAMOND_KATANA = registerSword("diamond_katana", Tiers.DIAMOND,
            4, -2.0f, 1030);
    public static final DeferredItem<Item> RITUAL_DAGGER = registerSword("ritual_dagger", Tiers.DIAMOND,
            1, -1.4f, 400);
//    public static final Supplier<Item> BLADESINGER_SWORD = ITEMS.register("bladesinger_sword", () ->
//            new BladesingerSword(SpellDataRegistryHolder.of(new SpellDataRegistryHolder[]{
//                    new SpellDataRegistryHolder(RegistrySpells.THICK_OF_FIGHT, 1)})));
    //Staffs and wands
    public static final DeferredItem<Item> WOODEN_WAND = ITEMS.register("wooden_wand",
        () -> new StaffItem(PropertiesHelper.stackItemProperties(1).attributes(ExtendedSwordItem.createAttributes(WandTier.WOODEN_WAND))));
    public static final DeferredItem<Item> DRUID_WAND = ITEMS.register("druid_wand",
            () -> new StaffItem(PropertiesHelper.stackItemProperties(1).attributes(ExtendedSwordItem.createAttributes(WandTier.DRUID_WAND)).rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> CRYOMANCER_WAND = ITEMS.register("cryomancer_wand",
            () -> new StaffItem(PropertiesHelper.stackItemProperties(1).attributes(ExtendedSwordItem.createAttributes(WandTier.CRYOMANCER_WAND)).rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> ELECTROMANCER_WAND = ITEMS.register("electromancer_wand",
            () -> new StaffItem(PropertiesHelper.stackItemProperties(1).attributes(ExtendedSwordItem.createAttributes(WandTier.ELECTROMANCER_WAND)).rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> PYROMANCER_WAND = ITEMS.register("pyromancer_wand",
            () -> new StaffItem(PropertiesHelper.stackItemProperties(1).attributes(ExtendedSwordItem.createAttributes(WandTier.PYROMANCER_WAND)).rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> ENDER_WAND = ITEMS.register("ender_wand",
            () -> new StaffItem(PropertiesHelper.stackItemProperties(1).attributes(ExtendedSwordItem.createAttributes(WandTier.ENDER_WAND)).rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> SACRED_SYMBOL = ITEMS.register("sacred_symbol",
            () -> new StaffItem(PropertiesHelper.stackItemProperties(1).attributes(ExtendedSwordItem.createAttributes(WandTier.SACRED_SYMBOL)).rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> BLOOD_WAND = ITEMS.register("blood_wand",
            () -> new StaffItem(PropertiesHelper.stackItemProperties(1).attributes(ExtendedSwordItem.createAttributes(WandTier.BLOOD_WAND)).rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> EVOKER_WAND = ITEMS.register("evoker_wand",
            () -> new StaffItem(PropertiesHelper.stackItemProperties(1).attributes(ExtendedSwordItem.createAttributes(WandTier.EVOKER_WAND)).rarity(Rarity.UNCOMMON)));

    //Items
    public static final DeferredItem<Item> ECTOPLASM = ITEMS.register("ectoplasm", () ->
            new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> RAVEN_FEATHER = ITEMS.register("raven_feather", () ->
            new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> MIRROR = ITEMS.register("mirror", () ->
            new MirrorItem(new Item.Properties()));
    public static final DeferredItem<Item> DICE20 = ITEMS.register("d20", () ->
            new D20Item(new Item.Properties().rarity(Rarity.RARE)));
    public static final DeferredItem<Item> BLADE_RUNE = ITEMS.register("blade_rune", () ->
            new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> GREEMON_FANG = ITEMS.register("greemon_fang", () ->
            new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> IRON_RING = ITEMS.register("iron_ring", () ->
            new Item(new Item.Properties().stacksTo(64)));
    //Magic Item
    public static final DeferredItem<Item> HAG_EYE = ITEMS.register("hag_eye", () ->
            new HagEye(new Item.Properties().stacksTo(1)
                    .rarity(Rarity.RARE)));
    public static final DeferredItem<Item> WAND_CORE = ITEMS.register("wand_core", () ->
            new WandCore(new Item.Properties().stacksTo(16)
                    .rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> DRUID_WAND_CORE = ITEMS.register("druid_wand_core", () ->
            new WandCore(new Item.Properties().stacksTo(16)
                    .rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> BLOOD_WAND_CORE = ITEMS.register("blood_wand_core", () ->
            new WandCore(new Item.Properties().stacksTo(16)
                    .rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> PYROMANCER_WAND_CORE = ITEMS.register("pyromancer_wand_core", () ->
            new WandCore(new Item.Properties().stacksTo(16)
                    .rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> CRYOMANCER_WAND_CORE = ITEMS.register("cryomancer_wand_core", () ->
            new WandCore(new Item.Properties().stacksTo(16)
                    .rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> ELECTROMANCER_WAND_CORE = ITEMS.register("electromancer_wand_core", () ->
            new WandCore(new Item.Properties().stacksTo(16)
                    .rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> EVOKER_WAND_CORE = ITEMS.register("evoker_wand_core", () ->
            new WandCore(new Item.Properties().stacksTo(16)
                    .rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> ENDER_WAND_CORE = ITEMS.register("ender_wand_core", () ->
            new WandCore(new Item.Properties().stacksTo(16)
                    .rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> FOREST_HEART = ITEMS.register("forest_heart", () ->
            new ForestHeart(new Item.Properties().stacksTo(1)
                    .rarity(Rarity.RARE)));
    public static final DeferredItem<Item> THUNDERSTORM_BOTTLE = ITEMS.register("thunderstorm_bottle", () ->
            new Item(new Item.Properties().stacksTo(16)
                    .rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> BURNT_SUGAR = ITEMS.register("burnt_sugar", () ->
            new Item(new Item.Properties().stacksTo(64)));

    public static final DeferredItem<CurioBaseItem> PROTECTION_RING = ITEMS.register("protection_ring",
            () -> new CurioBaseItem(PropertiesHelper.stackItemProperties(1))
                    .withAttributes(Curios.RING_SLOT,
                            new AttributeContainer(
                                    Attributes.ARMOR,
                                    2.0D,
                                    AttributeModifier.Operation.ADD_VALUE)));
    public static final DeferredItem<Item> MAGICAL_GRIMOIRE = ITEMS.register("magical_grimoire",
            () -> (new SpellBook(8)).withSpellbookAttributes(new AttributeContainer[]{new AttributeContainer(AttributeRegistry.COOLDOWN_REDUCTION, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(AttributeRegistry.MAX_MANA, 50.0F, AttributeModifier.Operation.ADD_VALUE)}));
    //Potions
//    public static final Supplier<Item> PHANTOM_POTION = ITEMS.register("phantom_potion",
//            () -> new SimpleElixir(ItemPropertiesHelper.material(), () ->
//                    new MobEffectInstance(ModEffects.PHANTOM_EFFECT.get(), 900, 0)));

    //Eggs
    public static final DeferredItem<SpawnEggItem> RAVEN_SPAWN_EGG = ITEMS.register("raven_spawn_egg", () ->
            new DeferredSpawnEggItem(ModEntityType.RAVEN, 0x111111, 0x262626, PropertiesHelper.stackItemProperties(64)));
    public static final DeferredItem<SpawnEggItem> UNDEAD_SPIRIT_SPAWN_EGG = ITEMS.register("undead_spirit_spawn_egg", () ->
            new DeferredSpawnEggItem(ModEntityType.UNDEAD_SPIRIT, 0x111111, 0x989898, PropertiesHelper.stackItemProperties(64)));
    public static final DeferredItem<SpawnEggItem> GHOST_SPAWN_EGG = ITEMS.register("ghost_spawn_egg", () ->
            new DeferredSpawnEggItem(ModEntityType.GHOST, 0x585858, 0x787878, PropertiesHelper.stackItemProperties(64)));
    public static final DeferredItem<SpawnEggItem> GOBLIN_SHAMAN_SPAWN_EGG = ITEMS.register("goblin_shaman_spawn_egg", () ->
            new DeferredSpawnEggItem(ModEntityType.GOBLIN_SHAMAN, 0x224f23, 0x2f6e30, PropertiesHelper.stackItemProperties(64)));
    public static final DeferredItem<SpawnEggItem> GOBLIN_WARRIOR_SPAWN_EGG = ITEMS.register("goblin_warrior_spawn_egg", () ->
            new DeferredSpawnEggItem(ModEntityType.GOBLIN_WARRIOR, 0x224f23, 0x4e7a4f, PropertiesHelper.stackItemProperties(64)));
    public static final DeferredItem<SpawnEggItem> GREEMON_SPAWN_EGG = ITEMS.register("greemon_spawn_egg", () ->
            new DeferredSpawnEggItem(ModEntityType.GREEMON, 0x955252, 0x74786c, PropertiesHelper.stackItemProperties(64)));
    public static final DeferredItem<SpawnEggItem> GREEN_HAG_SPAWN_EGG = ITEMS.register("green_hag_spawn_egg", () ->
            new DeferredSpawnEggItem(ModEntityType.GREEN_HAG, 0x1c4316, 0x25631c, PropertiesHelper.stackItemProperties(64)));
    public static final DeferredItem<SpawnEggItem> LESHY_SPAWN_EGG = ITEMS.register("leshy_spawn_egg", () ->
            new DeferredSpawnEggItem(ModEntityType.LESHY, 0x295423, 0x37240d, PropertiesHelper.stackItemProperties(64)));



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
