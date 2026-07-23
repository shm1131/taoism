package io.github.shm1131.taoism.item;

import io.github.shm1131.taoism.TaoismMain;
import io.github.shm1131.taoism.item.herbs.base.Flavor;
import io.github.shm1131.taoism.item.herbs.base.HerbProperties;
import io.github.shm1131.taoism.item.herbs.base.Nature;
import io.github.shm1131.taoism.item.herbs.herbs.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemRegister {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, TaoismMain.MODID);

    private static Item.Properties herbProps(String name) {
        return new Item.Properties()
                .setId(ResourceKey.create(
                        Registries.ITEM,
                        Identifier.fromNamespaceAndPath(TaoismMain.MODID, name)
                ));
    }

    // ==================== 补益类 ====================

    /** 人参：甘微温，大补元气，复脉固脱。药性峻猛，无毒 */
    public static final DeferredHolder<Item, RenShen> REN_SHEN = ITEMS.register(
            "ren_shen",
            () -> new RenShen(
                    herbProps("ren_shen"),
                    new HerbProperties(Flavor.SWEET, Nature.WARM, 0.0f, 1.0f)
            )
    );

    /** 灵芝：甘平，补气安神，止咳平喘。药力雄厚但性平，无毒 */
    public static final DeferredHolder<Item, LingZhi> LING_ZHI = ITEMS.register(
            "ling_zhi",
            () -> new LingZhi(
                    herbProps("ling_zhi"),
                    new HerbProperties(Flavor.SWEET, Nature.NEUTRAL, 0.0f, 0.8f)
            )
    );

    /** 黄精：甘平，补气养阴，健脾润肺。药食同源，性平力缓 */
    public static final DeferredHolder<Item, HuangJing> HUANG_JING = ITEMS.register(
            "huang_jing",
            () -> new HuangJing(
                    herbProps("huang_jing"),
                    new HerbProperties(Flavor.SWEET, Nature.NEUTRAL, 0.0f, 0.5f)
            )
    );

    /** 地黄：甘寒，清热凉血，养阴生津。鲜品性寒力中，无毒 */
    public static final DeferredHolder<Item, DiHuang> DI_HUANG = ITEMS.register(
            "di_huang",
            () -> new DiHuang(
                    herbProps("di_huang"),
                    new HerbProperties(Flavor.SWEET, Nature.COLD, 0.0f, 0.6f)
            )
    );

    /** 甘草：甘平，补脾益气，调和诸药。国老之药，性最平和 */
    public static final DeferredHolder<Item, GanCao> GAN_CAO = ITEMS.register(
            "gan_cao",
            () -> new GanCao(
                    herbProps("gan_cao"),
                    new HerbProperties(Flavor.SWEET, Nature.NEUTRAL, 0.0f, 0.4f)
            )
    );

    /** 大枣：甘温，补中益气，养血安神。药食同源，力缓 */
    public static final DeferredHolder<Item, DaZao> DA_ZAO = ITEMS.register(
            "da_zao.json",
            () -> new DaZao(
                    herbProps("da_zao.json"),
                    new HerbProperties(Flavor.SWEET, Nature.WARM, 0.0f, 0.3f)
            )
    );

    /** 枸杞子：甘平，滋补肝肾，益精明目。药食同源，性平力缓 */
    public static final DeferredHolder<Item, GouQi> GOU_QI = ITEMS.register(
            "gou_qi",
            () -> new GouQi(
                    herbProps("gou_qi"),
                    new HerbProperties(Flavor.SWEET, Nature.NEUTRAL, 0.0f, 0.4f)
            )
    );

    /** 黑芝麻：甘平，补肝肾，益精血，润肠燥。药食同源 */
    public static final DeferredHolder<Item, HeiZhiMa> HEI_ZHI_MA = ITEMS.register(
            "hei_zhi_ma",
            () -> new HeiZhiMa(
                    herbProps("hei_zhi_ma"),
                    new HerbProperties(Flavor.SWEET, Nature.NEUTRAL, 0.0f, 0.3f)
            )
    );

    // ==================== 健脾祛湿类 ====================

    /** 白术：苦甘温，健脾益气，燥湿利水。常规主力，无毒 */
    public static final DeferredHolder<Item, BaiShu> BAI_SHU = ITEMS.register(
            "bai_shu",
            () -> new BaiShu(
                    herbProps("bai_shu"),
                    new HerbProperties(Flavor.SWEET, Nature.WARM, 0.0f, 0.6f)
            )
    );

    /** 苍术：辛苦温，燥湿健脾，祛风散寒。辛燥力强于白术，无毒 */
    public static final DeferredHolder<Item, CangShu> CANG_SHU = ITEMS.register(
            "cang_shu",
            () -> new CangShu(
                    herbProps("cang_shu"),
                    new HerbProperties(Flavor.PUNGENT, Nature.WARM, 0.0f, 0.7f)
            )
    );

    /** 茯苓：甘淡平，利水渗湿，健脾宁心。性平力缓，药食同源 */
    public static final DeferredHolder<Item, FuLing> FU_LING = ITEMS.register(
            "fu_ling",
            () -> new FuLing(
                    herbProps("fu_ling"),
                    new HerbProperties(Flavor.SWEET, Nature.NEUTRAL, 0.0f, 0.4f)
            )
    );

    // ==================== 安神开窍类 ====================

    /** 远志：苦辛温，安神益智，祛痰开窍。对胃黏膜有刺激，微毒 */
    public static final DeferredHolder<Item, YuanZhi> YUAN_ZHI = ITEMS.register(
            "yuan_zhi",
            () -> new YuanZhi(
                    herbProps("yuan_zhi"),
                    new HerbProperties(Flavor.BITTER, Nature.WARM, 0.2f, 0.5f)
            )
    );

    /** 石菖蒲：辛苦温，开窍豁痰，醒神益智。含挥发油，过量致幻，微毒 */
    public static final DeferredHolder<Item, ChangPu> CHANG_PU = ITEMS.register(
            "chang_pu",
            () -> new ChangPu(
                    herbProps("chang_pu"),
                    new HerbProperties(Flavor.PUNGENT, Nature.WARM, 0.2f, 0.5f)
            )
    );

    // ==================== 花叶果实类 ====================

    /** 菊花：辛甘苦微寒，散风清热，平肝明目。药食同源，力缓 */
    public static final DeferredHolder<Item, JuHua> JU_HUA = ITEMS.register(
            "ju_hua",
            () -> new JuHua(
                    herbProps("ju_hua"),
                    new HerbProperties(Flavor.SWEET, Nature.COLD, 0.0f, 0.4f)
            )
    );

    /** 槐实：苦寒，凉血止血，清肝泻火。性寒滑肠，脾胃虚寒慎用，微毒 */
    public static final DeferredHolder<Item, HuaiShi> HUAI_SHI = ITEMS.register(
            "huai_shi",
            () -> new HuaiShi(
                    herbProps("huai_shi"),
                    new HerbProperties(Flavor.BITTER, Nature.COLD, 0.15f, 0.5f)
            )
    );

    // ==================== 松属三味 ====================

    /** 松针：苦温，祛风活血，安神止痛。外用为主，内服微刺激 */
    public static final DeferredHolder<Item, SongZhen> SONG_ZHEN = ITEMS.register(
            "song_zhen",
            () -> new SongZhen(
                    herbProps("song_zhen"),
                    new HerbProperties(Flavor.BITTER, Nature.WARM, 0.1f, 0.3f)
            )
    );

    /** 松脂（松香）：苦甘温，祛风燥湿，生肌止痛。含树脂酸，内服需谨慎 */
    public static final DeferredHolder<Item, SongZhi> SONG_ZHI = ITEMS.register(
            "song_zhi",
            () -> new SongZhi(
                    herbProps("song_zhi"),
                    new HerbProperties(Flavor.BITTER, Nature.WARM, 0.25f, 0.4f)
            )
    );

    /** 松子仁：甘温，润燥滑肠，补虚止咳。药食同源，力缓 */
    public static final DeferredHolder<Item, SongShi> SONG_SHI = ITEMS.register(
            "song_shi",
            () -> new SongShi(
                    herbProps("song_shi"),
                    new HerbProperties(Flavor.SWEET, Nature.WARM, 0.0f, 0.3f)
            )
    );
}
