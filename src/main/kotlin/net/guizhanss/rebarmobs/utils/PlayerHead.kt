package net.guizhanss.rebarmobs.utils

import net.guizhanss.guizhanlib.kt.minecraft.items.edit
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.net.URI
import java.util.UUID

/**
 * Construct a player head [ItemStack] with the given hash string.
 */
fun createPlayerHead(texture: String): ItemStack {
    val textureUrl = "https://textures.minecraft.net/texture/$texture"
    val uuid = UUID.nameUUIDFromBytes(texture.toByteArray())
    return ItemStack(Material.PLAYER_HEAD).edit {
        meta {
            val profile = Bukkit.createProfile(uuid)
            val profileTextures = profile.textures
            profileTextures.skin = URI(textureUrl).toURL()
            profile.setTextures(profileTextures)
            (this as SkullMeta).playerProfile = profile
        }
    }
}

enum class PlayerHead(val texture: String) {
    // Entity heads from ExtraHeads - https://github.com/Slimefun-Addon-Community/ExtraHeads
    ENTITY_BAT("2796aa6d18edc5b724bd89e983bc3215a41bf775d112635e9b5835d1b8ad20cb"),
    ENTITY_BLAZE("b78ef2e4cf2c41a2d14bfde9caff10219f5b1bf5b35a49eb51c6467882cb5f0"),
    ENTITY_CAVE_SPIDER("41645dfd77d09923107b3496e94eeb5c30329f97efc96ed76e226e98224"),
    ENTITY_CHICKEN("1638469a599ceef7207537603248a9ab11ff591fd378bea4735b346a7fae893"),
    ENTITY_COW("5d6c6eda942f7f5f71c3161c7306f4aed307d82895f9d2b07ab4525718edc5"),
    ENTITY_DOLPHIN("cefe7d803a45aa2af1993df2544a28df849a762663719bfefc58bf389ab7f5"),
    ENTITY_DROWNED("c84df79c49104b198cdad6d99fd0d0bcf1531c92d4ab6269e40b7d3cbbb8e98c"),
    ENTITY_ELDER_GUARDIAN("4adc4a6f53afa116027b51d6f2e433ee7afa5d59b2ffa04780be464fa5d61a"),
    ENTITY_ENDERMAN("7a59bb0a7a32965b3d90d8eafa899d1835f424509eadd4e6b709ada50b9cf"),
    ENTITY_EVOKER("d954135dc82213978db478778ae1213591b93d228d36dd54f1ea1da48e7cba6"),
    ENTITY_GHAST("8b6a72138d69fbbd2fea3fa251cabd87152e4f1c97e5f986bf685571db3cc0"),
    ENTITY_GUARDIAN("932c24524c82ab3b3e57c2052c533f13dd8c0beb8bdd06369bb2554da86c123"),
    ENTITY_HORSE("61902898308730c4747299cb5a5da9c25838b1d059fe46fc36896fee662729"),
    ENTITY_HUSK("d674c63c8db5f4ca628d69a3b1f8a36e29d8fd775e1a6bdb6cabb4be4db121"),
    ENTITY_ILLUSIONER("2f2882dd09723e47c0ab9663eab083d6a5969273706110c82910e61bf8a8f07e"),
    ENTITY_IRON_GOLEM("89091d79ea0f59ef7ef94d7bba6e5f17f2f7d4572c44f90f76c4819a714"),
    ENTITY_LLAMA("2a5f10e6e6232f182fe966f501f1c3799d45ae19031a1e4941b5dee0feff059b"),
    ENTITY_MAGMA_CUBE("38957d5023c937c4c41aa2412d43410bda23cf79a9f6ab36b76fef2d7c429"),
    ENTITY_MOOSHROOM("d0bc61b9757a7b83e03cd2507a2157913c2cf016e7c096a4d6cf1fe1b8db"),
    ENTITY_OCELOT("5657cd5c2989ff97570fec4ddcdc6926a68a3393250c1be1f0b114a1db1"),
    ENTITY_PARROT("a4ba8d66fecb1992e94b8687d6ab4a5320ab7594ac194a2615ed4df818edbc3"),
    ENTITY_PIG("621668ef7cb79dd9c22ce3d1f3f4cb6e2559893b6df4a469514e667c16aa4"),
    ENTITY_POLAR_BEAR("442123ac15effa1ba46462472871b88f1b09c1db467621376e2f71656d3fbc"),
    ENTITY_RABBIT("ff1559194a175935b8b4fea6614bec60bf81cf524af6f564333c555e657bc"),
    ENTITY_SHEEP("f31f9ccc6b3e32ecf13b8a11ac29cd33d18c95fc73db8a66c5d657ccb8be70"),
    ENTITY_SHULKER("b1d3534d21fe8499262de87affbeac4d25ffde35c8bdca069e61e1787ff2f"),
    ENTITY_SLIME("16ad20fc2d579be250d3db659c832da2b478a73a698b7ea10d18c9162e4d9b5"),
    ENTITY_SPIDER("cd541541daaff50896cd258bdbdd4cf80c3ba816735726078bfe393927e57f1"),
    ENTITY_SQUID("01433be242366af126da434b8735df1eb5b3cb2cede39145974e9c483607bac"),
    ENTITY_STRAY("78ddf76e555dd5c4aa8a0a5fc584520cd63d489c253de969f7f22f85a9a2d56"),
    ENTITY_TURTLE("0a4050e7aacc4539202658fdc339dd182d7e322f9fbcc4d5f99b5718a"),
    ENTITY_VEX("c2ec5a516617ff1573cd2f9d5f3969f56d5575c4ff4efefabd2a18dc7ab98cd"),
    ENTITY_VILLAGER("822d8e751c8f2fd4c8942c44bdb2f5ca4d8ae8e575ed3eb34c18a86e93b"),
    ENTITY_VINDICATOR("6deaec344ab095b48cead7527f7dee61b063ff791f76a8fa76642c8676e2173"),
    ENTITY_WITCH("ddedbee42be472e3eb791e7dbdfaf18c8fe593c638ba1396c9ef68f555cbce"),
    ENTITY_WITHER("cdf74e323ed41436965f5c57ddf2815d5332fe999e68fbb9d6cf5c8bd4139f"),
    ENTITY_ZOMBIE_VILLAGER("a6224941314bca2ebbb66b10ffd94680cc98c3435eeb71a228a08fd42c24db"),
    ENTITY_RAVAGER("1cb9f139f9489d86e410a06d8cbc670c8028137508e3e4bef612fe32edd60193"),
    ENTITY_PILLAGER("4aee6bb37cbfc92b0d86db5ada4790c64ff4468d68b84942fde04405e8ef5333"),
    ENTITY_FOX("46cff7a19e683a08e4587ea1457880313d5f341f346ceb5b0551195d810e3"),
    ENTITY_PANDA("7818b681cace1c641919f53edadecb142330d089a826b56219138c33b7a5e0db"),
    ENTITY_WANDERING_TRADER("5f1379a82290d7abe1efaabbc70710ff2ec02dd34ade386bc00c930c461cf932"),
    ENTITY_ZOMBIFIED_PIGLIN("e935842af769380f78e8b8a88d1ea6ca2807c1e5693c2cf797456620833e936f"),
    ENTITY_STRIDER("18a9adf780ec7dd4625c9c0779052e6a15a451866623511e4c82e9655714b3c1"),
    ENTITY_AXOLOTL("5c138f401c67fc2e1e387d9c90a9691772ee486e8ddbf2ed375fc8348746f936"),
    ENTITY_GLOW_SQUID("57327ee11812b764c7ade70b282cce4c58e635b2015244081d1490543da7280e"),
    ENTITY_GOAT("457a0d538fa08a7affe312903468861720f9fa34e86d44b89dcec5639265f03"),

    // https://minecraft-heads.com/custom-heads/animals/61373-allay
    ENTITY_ALLAY("df5de940bfe499c59ee8dac9f9c3919e7535eff3a9acb16f4842bf290f4c679f"),

    // https://minecraft-heads.com/custom-heads/animals/63169-cold-frog
    ENTITY_FROG("45852a95928897746012988fbd5dbaa1b70b7a5fb65157016f4ff3f245374c08"),

    // https://minecraft-heads.com/custom-heads/animals/51348-tadpole
    ENTITY_TADPOLE("987035f5352334c2cba6ac4c65c2b9059739d6d0e839c1dd98d75d2e77957847"),

    // https://minecraft-heads.com/custom-heads/animals/62878-camel
    ENTITY_CAMEL("3642c9f71131b5df4a8c21c8c6f10684f22abafb8cd68a1d55ac4bf263a53a31"),

    // https://minecraft-heads.com/custom-heads/animals/64113-sniffer
    ENTITY_SNIFFER("fe5a8341c478a134302981e6a7758ea4ecfd8d62a0df4067897e75502f9b25de"),

    // https://minecraft-heads.com/custom-heads/head/91910-armadillo
    ENTITY_ARMADILLO("9852b33ba294f560090752d113fe728cbc7dd042029a38d5382d65a2146068b7"),

    // https://minecraft-heads.com/custom-heads/head/87691-bogged
    ENTITY_BOGGED("a3b9003ba2d05562c75119b8a62185c67130e9282f7acbac4bc2824c21eb95d9"),

    // https://minecraft-heads.com/custom-heads/head/69108-breeze
    ENTITY_BREEZE("a275728af7e6a29c88125b675a39d88ae9919bb61fdc200337fed6ab0c49d65c"),

    // https://minecraft-heads.com/custom-heads/head/111644-creaking
    ENTITY_CREAKING("3630e03391db6c0e9c8643a59754c2d19a1f938a787150af3b3d516ba8094cda"),

    // https://minecraft-heads.com/custom-heads/head/117418-happy-ghast
    ENTITY_HAPPY_GHAST("a1a36cb93d01675c4622dd5c8d872110911ec12c372e89afa8ba03862867f6fb"),

    // https://minecraft-heads.com/custom-heads/head/48089-copper-golem
    ENTITY_COPPER_GOLEM("ef4fcdff157a36d32061cb7dd0b69f7f7885fd3ddf99de471b67a84cc8677cb3"),

    // https://minecraft-heads.com/custom-heads/head/123259-nautilus
    ENTITY_NAUTILUS("3bb340dd3302615348de5162fe1670b9c5c9c616cd92d2de9d8398cb33e842ae"),

    // https://minecraft-heads.com/custom-heads/head/123260-zombie-nautilus
    ENTITY_ZOMBIE_NAUTILUS("fd9a933376da44c3391307cb9f4cf03f16f3a54f495fd5a11bad8a373f9d5720"),

    // https://minecraft-heads.com/custom-heads/head/123542-camel-husk
    ENTITY_CAMEL_HUSK("750bfc9b2cc40f4d8d0224ccbabac26b338aa947d99dcde769f859b59b8d0b0e"),

    // https://minecraft-heads.com/custom-heads/head/123518-parched
    ENTITY_PARCHED("24aeceff5f26dd8413c5c03547c234ac03108d187af0b9cd834a8ce12598591c"),

    // https://minecraft-heads.com/custom-heads/head/18427-endermite
    ENTITY_ENDERMITE("5bc7b9d36fb92b6bf292be73d32c6c5b0ecc25b44323a541fae1f1e67e393a3e"),

    // https://minecraft-heads.com/custom-heads/head/34783-hoglin
    ENTITY_HOGLIN("9bb9bc0f01dbd762a08d9e77c08069ed7c95364aa30ca1072208561b730e8d75"),

    // https://minecraft-heads.com/custom-heads/head/17931-phantom
    ENTITY_PHANTOM("746830da5f83a3aaed838a99156ad781a789cfcf13e25beef7f54a86e4fa4"),

    // https://minecraft-heads.com/custom-heads/head/38372-piglin-brute
    ENTITY_PIGLIN_BRUTE("3e300e9027349c4907497438bac29e3a4c87a848c50b34c21242727b57f4e1cf"),

    // https://minecraft-heads.com/custom-heads/head/106998-silverfish
    ENTITY_SILVERFISH("bfe13237e1109cab7264dc53b0aa697cf9a7c62c984a269fb0755a288912bbca"),

    // https://minecraft-heads.com/custom-heads/head/100620-warden
    ENTITY_WARDEN("b84d5a30166509864fb1774c72fd9df338fc5c599c84e6e8fe223ba6b17291cd"),

    // https://minecraft-heads.com/custom-heads/head/35932-zoglin
    ENTITY_ZOGLIN("e67e18602e03035ad68967ce090235d8996663fb9ea47578d3a7ebbc42a5ccf9"),

    // https://minecraft-heads.com/custom-heads/head/128259-sulfur-cube
    ENTITY_SULFUR_CUBE("205ec0241f084e1776a201f6df8c124c8637671fd85e0af5e77090f4e9c38602"),

    // https://minecraft-heads.com/custom-heads/head/128170-bee
    ENTITY_BEE("9c72c132073ec5a058218d6fbfb4a9970652ced9214d53f4f614b3902fd99a7a"),

    // https://minecraft-heads.com/custom-heads/head/103253-orange-tabby-cat
    ENTITY_CAT("22d4a972b4cb28774ee5563d4e7b89e4855a228bebe23346ad1174066f0a07aa"),

    // https://minecraft-heads.com/custom-heads/head/17898-cod-fish
    ENTITY_COD("7892d7dd6aadf35f86da27fb63da4edda211df96d2829f691462a4fb1cab0"),

    // https://minecraft-heads.com/custom-heads/head/125655-donkey
    ENTITY_DONKEY("f57ceea4e2a7bc996f0708f1af949f78dcd9cf3ea6b6f123347ddfc3a2259bb5"),

    // https://minecraft-heads.com/custom-heads/head/25319-mule
    ENTITY_MULE("16272233c1eb1bf47baf5c6cf86f93d15af9708ad2ca18f6c90153499226885"),

    // https://minecraft-heads.com/custom-heads/head/121384-pufferfish
    ENTITY_PUFFERFISH("4e5bbb9f1b5dfe92f455bea48181b6f23f56dd2a7de022c780c55aa51b61638c"),

    // https://minecraft-heads.com/custom-heads/head/17714-salmon
    ENTITY_SALMON("afbdf10317a7a6315c3cbaf7edc590f0d97714bd699c8615a24f72bb8a9bc"),

    // https://minecraft-heads.com/custom-heads/head/105331-skeleton-horse
    ENTITY_SKELETON_HORSE("ac7d8a16d3f0f98b598df93f2c2d34e75171cd52dbf4a1211d7b84c019416a40"),

    // https://minecraft-heads.com/custom-heads/head/109991-snow-golem
    ENTITY_SNOW_GOLEM("e6f20aec528c3968dd8164f9d9336b081b3a2c7ecf189cf73df6f925e5a4ed14"),

    // https://minecraft-heads.com/custom-heads/head/113925-trader-llama-grayed
    ENTITY_TRADER_LLAMA("b1c54365d1ceac0da4a86b09067a17a4fd849eac422c79992642157996430289"),

    // https://minecraft-heads.com/custom-heads/head/107004-tropical-fish-grayed
    ENTITY_TROPICAL_FISH("5ac3bd91eab2d5a3980b75ddb93f77970dba8fbb45a862ae5e77c10cf5e7e5e1"),

    // https://minecraft-heads.com/custom-heads/head/127233-wolf
    ENTITY_WOLF("fd117e055b9d2c298d215802fbeabe5cc9cbdabef0bf73f18c7cc2b4bec8a898"),

    // https://minecraft-heads.com/custom-heads/head/13581-zombie-horse
    ENTITY_ZOMBIE_HORSE("5b54f2bc2b6f59abb8c3f9d6c3a3154e3fefe238f2bad593db2de7fa3a4"),

    ;

    fun createHeadItem() = createPlayerHead(texture)
}
