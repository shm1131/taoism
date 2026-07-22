package io.github.shm1131.taoism.item.talismans.base;


import net.minecraft.world.item.Item;

public abstract class BaseTalismanItem extends Item implements ITalismanBase{
    private final TalismanProperties properties;

    protected BaseTalismanItem(Properties itemProps, TalismanProperties talismanProps) {
        super(itemProps);
        this.properties = talismanProps;
    }

    @Override
    public TalismanProperties getTalismanItemProperties() {
        return properties;
    }
}
