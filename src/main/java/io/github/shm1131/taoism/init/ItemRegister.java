package io.github.shm1131.taoism.init;

import io.github.shm1131.taoism.TaoismMain;
import io.github.shm1131.taoism.item.IconItem;
import io.github.shm1131.taoism.item.bar.ZhuSha;
import io.github.shm1131.taoism.item.herb.base.BaseHerbItem;
import io.github.shm1131.taoism.item.herb.base.Flavor;
import io.github.shm1131.taoism.item.herb.base.HerbProperties;
import io.github.shm1131.taoism.item.herb.base.Nature;
import io.github.shm1131.taoism.item.herb.pill.PillItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemRegister {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, TaoismMain.MODID);


    private static Item.Properties props(String name) {
        return new Item.Properties()
            .setId(ResourceKey.create(
                Registries.ITEM,
                Identifier.fromNamespaceAndPath(TaoismMain.MODID, name)
            ));
    }


    /** 草药定义记录 */
    private record HerbDef(String name, Flavor flavor, Nature nature, float toxicity, float efficacy) {}

    private static final List<HerbDef> HERB_DEFS = List.of(
        // 基础草药
        new HerbDef("ren_shen",   Flavor.SWEET,  Nature.WARM,   0.0f,  1.0f),
        new HerbDef("ling_zhi",   Flavor.SWEET,  Nature.NEUTRAL,0.0f,  0.8f),
        new HerbDef("huang_jing", Flavor.SWEET,  Nature.NEUTRAL,0.0f,  0.5f),
        new HerbDef("di_huang",   Flavor.SWEET,  Nature.COLD,   0.0f,  0.6f),
        new HerbDef("gan_cao",    Flavor.SWEET,  Nature.NEUTRAL,0.0f,  0.4f),
        new HerbDef("da_zao",     Flavor.SWEET,  Nature.WARM,   0.0f,  0.3f),
        new HerbDef("gou_qi",     Flavor.SWEET,  Nature.NEUTRAL,0.0f,  0.4f),
        new HerbDef("hei_zhi_ma", Flavor.SWEET,  Nature.NEUTRAL,0.0f,  0.3f),
        new HerbDef("bai_shu",    Flavor.SWEET,  Nature.WARM,   0.0f,  0.6f),
        new HerbDef("cang_shu",   Flavor.SWEET,  Nature.WARM,   0.0f,  0.7f),
        new HerbDef("fu_ling",    Flavor.SWEET,  Nature.NEUTRAL,0.0f,  0.4f),
        new HerbDef("yuan_zhi",   Flavor.BITTER, Nature.WARM,   0.2f,  0.5f),
        new HerbDef("chang_pu",   Flavor.SWEET,  Nature.WARM,   0.2f,  0.5f),
        new HerbDef("ju_hua",     Flavor.SWEET,  Nature.COLD,   0.0f,  0.4f),
        new HerbDef("huai_shi",   Flavor.BITTER, Nature.COLD,   0.15f, 0.5f),
        new HerbDef("song_zhen",  Flavor.BITTER, Nature.WARM,   0.1f,  0.3f),
        new HerbDef("song_zhi",   Flavor.BITTER, Nature.WARM,   0.25f, 0.4f),
        new HerbDef("song_shi",   Flavor.SWEET,  Nature.WARM,   0.0f,  0.3f),
        // 矿物/特殊草药
        new HerbDef("shui_yin",   Flavor.SPICY,  Nature.COLD,   0.95f, 0.80f),
        new HerbDef("ci_shi",     Flavor.SALTY,  Nature.WARM,   0.10f, 0.60f),
        new HerbDef("xiong_huang",Flavor.BITTER, Nature.WARM,   0.85f, 0.75f),
        new HerbDef("ci_huang",   Flavor.SPICY,  Nature.NEUTRAL,0.80f, 0.70f),
        new HerbDef("yun_mu_fen", Flavor.SWEET,  Nature.NEUTRAL,0.05f, 0.40f),
        new HerbDef("jin_fen",    Flavor.SPICY,  Nature.NEUTRAL,0.00f, 0.95f),
        new HerbDef("yin_fen",    Flavor.SWEET,  Nature.COLD,   0.05f, 0.50f),
        new HerbDef("qian_fen",   Flavor.SWEET,  Nature.COLD,   0.75f, 0.30f),
        new HerbDef("liu_huang",  Flavor.SPICY,  Nature.WARM,   0.60f, 0.85f),
        new HerbDef("xiao_shi",   Flavor.BITTER, Nature.COLD,   0.40f, 0.65f),
        new HerbDef("zhu_sha_fen",Flavor.SWEET,  Nature.NEUTRAL,0.70f, 0.90f)
    );

    private static final List<String> SIMPLE_ITEM_NAMES = List.of(
        "yellow_paper",
        "yun_mu",
        "cu_yin",
        "yin_ding",
        "qian_pian"
    );

    /** 所有批量注册的平凡物品（草药 + 简单物品），key 为注册名 */
    public static final Map<String, DeferredHolder<Item, ? extends Item>> SIMPLE_ITEMS;

    static {
        Map<String, DeferredHolder<Item, ? extends Item>> map = new HashMap<>();

        // 注册草药
        for (HerbDef def : HERB_DEFS) {
            map.put(def.name(), ITEMS.register(def.name(),
                () -> new BaseHerbItem(
                    props(def.name()),
                    new HerbProperties(def.flavor(), def.nature(), def.toxicity(), def.efficacy())
                )
            ));
        }

        // 注册简单物品
        for (String name : SIMPLE_ITEM_NAMES) {
            map.put(name, ITEMS.register(name,
                () -> new Item(props(name))
            ));
        }

        SIMPLE_ITEMS = Collections.unmodifiableMap(map);
    }


    public static final DeferredHolder<Item, PillItem> PILL = ITEMS.register("pill",
        () -> new PillItem(props("pill"))
    );

    public static final DeferredHolder<Item, IconItem> ICON_ITEM = ITEMS.register("icon_item",
        () -> new IconItem(props("icon_item"))
    );

    public static final DeferredHolder<Item, ZhuSha> ZHU_SHA = ITEMS.register("zhu_sha",
        () -> new ZhuSha(props("zhu_sha"))
    );
}
