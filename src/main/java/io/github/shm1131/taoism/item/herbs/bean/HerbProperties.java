package io.github.shm1131.taoism.item.herbs.bean;

public record HerbProperties(
    Flavor flavor,
    Nature nature,
    float toxicity,
    float potency
) {
}
