package io.github.shm1131.taoism.item.herbs.base;

import net.minecraft.world.item.Item;


public abstract class BaseHerbItem extends Item implements IHerbBase {
    private final HerbProperties properties;

    protected BaseHerbItem(Properties itemProps, HerbProperties herbProps) {
        super(itemProps);
        this.properties = herbProps;
    }

    @Override
    public HerbProperties getHerbProperties() {
        return properties;
    }
}