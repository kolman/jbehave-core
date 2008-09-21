package com.lunivore.noughtsandcrosses;

import org.jbehave.scenario.MostUsefulConfiguration;
import org.jbehave.scenario.JUnitScenario;
import org.jbehave.scenario.definition.KeyWords;
import org.jbehave.scenario.parser.PatternScenarioParser;
import org.jbehave.scenario.parser.ClasspathScenarioDefiner;
import org.jbehave.scenario.parser.UnderscoredCamelCaseResolver;

import com.lunivore.noughtsandcrosses.steps.LolCatzSteps;

/**
 * Checks that we can support scenarios written in other languages,
 * eg: lolcatz
 */
public class PlayersCanHazTurns extends JUnitScenario {

    public PlayersCanHazTurns() {
        this(Thread.currentThread().getContextClassLoader());
    }

    public PlayersCanHazTurns(final ClassLoader classLoader) {
        super(new MostUsefulConfiguration() {
            public KeyWords keywords() {
                return new KeyWords("I can haz", "Gief", "Wen", "Den", "And");
            }
            public ClasspathScenarioDefiner forDefiningScenarios() {
                return new ClasspathScenarioDefiner(new UnderscoredCamelCaseResolver(), new PatternScenarioParser(this),
                        classLoader);
            }
        }, new LolCatzSteps());
    }
    
}

