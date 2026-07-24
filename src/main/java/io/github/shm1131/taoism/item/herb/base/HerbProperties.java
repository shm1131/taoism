package io.github.shm1131.taoism.item.herb.base;

public record HerbProperties(
    Flavor flavor,
    Nature nature,
    float toxicity,
    float potency
) {
}
