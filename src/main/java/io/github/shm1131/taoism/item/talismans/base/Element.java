package io.github.shm1131.taoism.item.talismans.base;

public enum Element {
    WOOD,
    FIRE,
    EARTH,
    METAL,
    WATER;

    public Element getGeneratingElement(Element element ) {
        return switch ( element ) {
            case WOOD -> FIRE;
            case FIRE -> EARTH;
            case EARTH -> METAL;
            case METAL -> WATER;
            case WATER -> WOOD;
        };
    }

    public Element getRestrictingElement(Element element ) {
        return switch ( element ) {
            case WOOD -> EARTH;
            case FIRE -> METAL;
            case EARTH -> WATER;
            case METAL -> WOOD;
            case WATER -> FIRE;
        };
    }
}
