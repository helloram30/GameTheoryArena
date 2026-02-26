package com.gametheoryarena.arena.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import org.springframework.stereotype.Component;

@Component
public class StrategyRegistry {
    private final Map<String, Function<Random, Strategy>> factories;

    public StrategyRegistry() {
        Map<String, Function<Random, Strategy>> map = new HashMap<>();
        map.put("AlwaysCooperate", random -> new AlwaysCooperate());
        map.put("AlwaysDefect", random -> new AlwaysDefect());
        map.put("Random", RandomStrategy::new);
        map.put("TitForTat", random -> new TitForTat());
        map.put("GrimTrigger", random -> new GrimTrigger());
        map.put("WinStayLoseShift", random -> new WinStayLoseShift());
        map.put("TitForTwoTats", random -> new TitForTwoTats());
        map.put("GenerousTitForTat", random -> new GenerousTitForTat(random, 0.1));
        map.put("Prober", random -> new Prober());
        map.put("SuspiciousTitForTat", random -> new SuspiciousTitForTat());
        map.put("ContriteTitForTat", random -> new ContriteTitForTat());
        map.put("AdaptiveThreshold", random -> new AdaptiveThreshold());
        map.put("RegretMinimizer", RegretMinimizer::new);
        factories = Collections.unmodifiableMap(map);
    }

    public List<String> availableStrategies() {
        List<String> names = new ArrayList<>(factories.keySet());
        Collections.sort(names);
        return names;
    }

    public Strategy create(String name, Random random) {
        Function<Random, Strategy> factory = factories.get(name);
        if (factory == null) {
            throw new IllegalArgumentException("Unknown strategy: " + name);
        }
        return factory.apply(random);
    }

    public boolean hasStrategy(String name) {
        return factories.containsKey(name);
    }
}
