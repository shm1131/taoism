package io.github.shm1131.taoism.init;

import io.github.shm1131.taoism.TaoismMain;
import io.github.shm1131.taoism.item.herb.base.Flavor;
import io.github.shm1131.taoism.item.herb.base.HerbProperties;
import io.github.shm1131.taoism.item.herb.base.Nature;
import io.github.shm1131.taoism.item.herb.herbs.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class HerbItemRegister {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, TaoismMain.MODID);

    private static Item.Properties herbProps(String name) {
        return new Item.Properties()
                .setId(ResourceKey.create(
                        Registries.ITEM,
                        Identifier.fromNamespaceAndPath(TaoismMain.MODID, name)
                ));
    }

    public static final DeferredHolder<Item, RenShen> REN_SHEN = ITEMS.register(
            "ren_shen",
            () -> new RenShen(
                    herbProps("ren_shen"),
                    new HerbProperties(Flavor.SWEET, Nature.WARM, 0.0f, 1.0f)
            )
    );


    public static final DeferredHolder<Item, LingZhi> LING_ZHI = ITEMS.register(
            "ling_zhi",
            () -> new LingZhi(
                    herbProps("ling_zhi"),
                    new HerbProperties(Flavor.SWEET, Nature.NEUTRAL, 0.0f, 0.8f)
            )
    );


    public static final DeferredHolder<Item, HuangJing> HUANG_JING = ITEMS.register(
            "huang_jing",
            () -> new HuangJing(
                    herbProps("huang_jing"),
                    new HerbProperties(Flavor.SWEET, Nature.NEUTRAL, 0.0f, 0.5f)
            )
    );


    public static final DeferredHolder<Item, DiHuang> DI_HUANG = ITEMS.register(
            "di_huang",
            () -> new DiHuang(
                    herbProps("di_huang"),
                    new HerbProperties(Flavor.SWEET, Nature.COLD, 0.0f, 0.6f)
            )
    );


    public static final DeferredHolder<Item, GanCao> GAN_CAO = ITEMS.register(
            "gan_cao",
            () -> new GanCao(
                    herbProps("gan_cao"),
                    new HerbProperties(Flavor.SWEET, Nature.NEUTRAL, 0.0f, 0.4f)
            )
    );


    public static final DeferredHolder<Item, DaZao> DA_ZAO = ITEMS.register(
            "da_zao",
            () -> new DaZao(
                    herbProps("da_zao"),
                    new HerbProperties(Flavor.SWEET, Nature.WARM, 0.0f, 0.3f)
            )
    );


    public static final DeferredHolder<Item, GouQi> GOU_QI = ITEMS.register(
            "gou_qi",
            () -> new GouQi(
                    herbProps("gou_qi"),
                    new HerbProperties(Flavor.SWEET, Nature.NEUTRAL, 0.0f, 0.4f)
            )
    );


    public static final DeferredHolder<Item, HeiZhiMa> HEI_ZHI_MA = ITEMS.register(
            "hei_zhi_ma",
            () -> new HeiZhiMa(
                    herbProps("hei_zhi_ma"),
                    new HerbProperties(Flavor.SWEET, Nature.NEUTRAL, 0.0f, 0.3f)
            )
    );

    public static final DeferredHolder<Item, BaiShu> BAI_SHU = ITEMS.register(
            "bai_shu",
            () -> new BaiShu(
                    herbProps("bai_shu"),
                    new HerbProperties(Flavor.SWEET, Nature.WARM, 0.0f, 0.6f)
            )
    );


    public static final DeferredHolder<Item, CangShu> CANG_SHU = ITEMS.register(
            "cang_shu",
            () -> new CangShu(
                    herbProps("cang_shu"),
                    new HerbProperties(Flavor.SWEET, Nature.WARM, 0.0f, 0.7f)
            )
    );


    public static final DeferredHolder<Item, FuLing> FU_LING = ITEMS.register(
            "fu_ling",
            () -> new FuLing(
                    herbProps("fu_ling"),
                    new HerbProperties(Flavor.SWEET, Nature.NEUTRAL, 0.0f, 0.4f)
            )
    );

    public static final DeferredHolder<Item, YuanZhi> YUAN_ZHI = ITEMS.register(
            "yuan_zhi",
            () -> new YuanZhi(
                    herbProps("yuan_zhi"),
                    new HerbProperties(Flavor.BITTER, Nature.WARM, 0.2f, 0.5f)
            )
    );

    public static final DeferredHolder<Item, ChangPu> CHANG_PU = ITEMS.register(
            "chang_pu",
            () -> new ChangPu(
                    herbProps("chang_pu"),
                    new HerbProperties(Flavor.SWEET, Nature.WARM, 0.2f, 0.5f)
            )
    );

    public static final DeferredHolder<Item, JuHua> JU_HUA = ITEMS.register(
            "ju_hua",
            () -> new JuHua(
                    herbProps("ju_hua"),
                    new HerbProperties(Flavor.SWEET, Nature.COLD, 0.0f, 0.4f)
            )
    );

    public static final DeferredHolder<Item, HuaiShi> HUAI_SHI = ITEMS.register(
            "huai_shi",
            () -> new HuaiShi(
                    herbProps("huai_shi"),
                    new HerbProperties(Flavor.BITTER, Nature.COLD, 0.15f, 0.5f)
            )
    );

    public static final DeferredHolder<Item, SongZhen> SONG_ZHEN = ITEMS.register(
            "song_zhen",
            () -> new SongZhen(
                    herbProps("song_zhen"),
                    new HerbProperties(Flavor.BITTER, Nature.WARM, 0.1f, 0.3f)
            )
    );

    public static final DeferredHolder<Item, SongZhi> SONG_ZHI = ITEMS.register(
            "song_zhi",
            () -> new SongZhi(
                    herbProps("song_zhi"),
                    new HerbProperties(Flavor.BITTER, Nature.WARM, 0.25f, 0.4f)
            )
    );

    public static final DeferredHolder<Item, SongShi> SONG_SHI = ITEMS.register(
            "song_shi",
            () -> new SongShi(
                    herbProps("song_shi"),
                    new HerbProperties(Flavor.SWEET, Nature.WARM, 0.0f, 0.3f)
            )
    );



    //____________________________________________________________________________________________

    public static final DeferredHolder<Item, ShuiYin> SHUI_YIN = ITEMS.register(
        "shui_yin",
        () -> new ShuiYin(
            herbProps("shui_yin"),
            new HerbProperties(Flavor.SPICY, Nature.COLD, 0.95f, 0.80f)
        )
    );

    public static final DeferredHolder<Item, CiShi> CI_SHI = ITEMS.register(
        "ci_shi",
        () -> new CiShi(
            herbProps("ci_shi"),
            new HerbProperties(Flavor.SALTY, Nature.WARM, 0.10f, 0.60f)
        )
    );

    public static final DeferredHolder<Item, XiongHuang> XIONG_HUANG = ITEMS.register(
        "xiong_huang",
        () -> new XiongHuang(
            herbProps("xiong_huang"),
            new HerbProperties(Flavor.BITTER, Nature.WARM, 0.85f, 0.75f)
        )
    );

    public static final DeferredHolder<Item, CiHuang> CI_HUANG = ITEMS.register(
        "ci_huang",
        () -> new CiHuang(
            herbProps("ci_huang"),
            new HerbProperties(Flavor.SPICY, Nature.NEUTRAL, 0.80f, 0.70f)
        )
    );

    public static final DeferredHolder<Item, YunMu> YUN_MU_FEN = ITEMS.register(
        "yun_mu_fen",
        () -> new YunMu(
            herbProps("yun_mu_fen"),
            new HerbProperties(Flavor.SWEET, Nature.NEUTRAL, 0.05f, 0.40f)
        )
    );

    public static final DeferredHolder<Item, JinFen> JIN_FEN = ITEMS.register(
        "jin_fen",
        () -> new JinFen(
            herbProps("jin_fen"),
            new HerbProperties(Flavor.SPICY, Nature.NEUTRAL, 0.00f, 0.95f)
        )
    );

    public static final DeferredHolder<Item, YinFen> YIN_FEN = ITEMS.register(
        "yin_fen",
        () -> new YinFen(
            herbProps("yin_fen"),
            new HerbProperties(Flavor.SWEET, Nature.COLD, 0.05f, 0.50f)
        )
    );

    public static final DeferredHolder<Item, QianFen> QIAN_FEN = ITEMS.register(
        "qian_fen",
        () -> new QianFen(
            herbProps("qian_fen"),
            new HerbProperties(Flavor.SWEET, Nature.COLD, 0.75f, 0.30f)
        )
    );

    public static final DeferredHolder<Item, LiuHuang> LIU_HUANG = ITEMS.register(
        "liu_huang",
        () -> new LiuHuang(
            herbProps("liu_huang"),
            new HerbProperties(Flavor.SPICY, Nature.WARM, 0.60f, 0.85f)
        )
    );

    public static final DeferredHolder<Item, XiaoShi> XIAO_SHI = ITEMS.register(
        "xiao_shi",
        () -> new XiaoShi(
            herbProps("xiao_shi"),
            new HerbProperties(Flavor.BITTER, Nature.COLD, 0.40f, 0.65f)
        )
    );

    public static final DeferredHolder<Item,CinnabarDust> ZHU_SHA_FEN = ITEMS.register(
        "zhu_sha_fen",
        () -> new CinnabarDust(
            herbProps("zhu_sha_fen"),
            new HerbProperties(Flavor.SWEET, Nature.NEUTRAL, 0.70f, 0.90f)
        )
    );
}
