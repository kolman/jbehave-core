package org.jbehave.examples.trader.guice;

import static java.util.Arrays.asList;
import static org.jbehave.core.io.CodeLocations.codeLocationFromClass;
import static org.jbehave.core.reporters.StoryReporterBuilder.Format.CONSOLE;
import static org.jbehave.core.reporters.StoryReporterBuilder.Format.HTML;
import static org.jbehave.core.reporters.StoryReporterBuilder.Format.TXT;
import static org.jbehave.core.reporters.StoryReporterBuilder.Format.XML;

import java.text.SimpleDateFormat;

import org.jbehave.core.annotations.Configure;
import org.jbehave.core.annotations.UsingEmbedder;
import org.jbehave.core.annotations.guice.UsingGuice;
import org.jbehave.core.configuration.AnnotationBuilder;
import org.jbehave.core.configuration.guice.GuiceAnnotationBuilder;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.io.StoryLoader;
import org.jbehave.core.parsers.RegexPrefixCapturingPatternParser;
import org.jbehave.core.parsers.StepPatternParser;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.ParameterConverters.DateConverter;
import org.jbehave.core.steps.ParameterConverters.ParameterConverter;
import org.jbehave.examples.trader.BeforeAfterSteps;
import org.jbehave.examples.trader.TraderSteps;
import org.jbehave.examples.trader.guice.AnnotatedEmbedderUsingGuice.ConfigurationModule;
import org.jbehave.examples.trader.guice.AnnotatedEmbedderUsingGuice.StepsModule;
import org.jbehave.examples.trader.service.TradingService;
import org.jbehave.examples.trader.stories.AndStep.AndSteps;
import org.jbehave.examples.trader.stories.ClaimsWithNullCalendar.CalendarSteps;
import org.jbehave.examples.trader.stories.FailureFollowedByGivenStories.SandpitSteps;
import org.jbehave.examples.trader.stories.PriorityMatching.PriorityMatchingSteps;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * Run stories via Embedder using JBehave's annotated configuration using Guice
 * injection
 */
//@RunWith(GuiceJUnit4ClassRunner.class)
@Configure()
@UsingGuice(modules = { ConfigurationModule.class, StepsModule.class })
@UsingEmbedder(ignoreFailureInStories = true, ignoreFailureInView = true)
public class AnnotatedEmbedderUsingGuice {

//    @Inject
//    Embedder embedder;

    @Test
    public void run() {
        AnnotationBuilder builder = new GuiceAnnotationBuilder(this.getClass());
        Embedder embedder =  builder.buildEmbedder();
        embedder.embedderControls().doIgnoreFailureInStories(true).doIgnoreFailureInView(true);
        embedder.runStoriesAsPaths(new StoryFinder().findPaths(codeLocationFromClass(this.getClass()).getFile(),
                asList("**/stories/*.story"), asList("")));
    }

    // Guice modules
    public static class ConfigurationModule extends AbstractModule {

        @Override
        protected void configure() {
            bind(StepPatternParser.class).toInstance(new RegexPrefixCapturingPatternParser("%"));
            bind(StoryLoader.class).toInstance(new LoadFromClasspath(this.getClass().getClassLoader()));
            bind(ParameterConverter.class).toInstance(new DateConverter(new SimpleDateFormat("yyyy-MM-dd")));
            bind(StoryReporterBuilder.class).toInstance(new StoryReporterBuilder()
                .withDefaultFormats().withFormats(CONSOLE, HTML, TXT, XML)
                .withCodeLocation(CodeLocations.codeLocationFromClass(this.getClass()))
                .withFailureTrace(true)
            );
        }

    }

    public static class StepsModule extends AbstractModule {

        @Override
        protected void configure() {
            bind(TradingService.class).in(Scopes.SINGLETON);
            bind(TraderSteps.class).in(Scopes.SINGLETON);          
            bind(BeforeAfterSteps.class).in(Scopes.SINGLETON);
            bind(AndSteps.class).in(Scopes.SINGLETON);
            bind(CalendarSteps.class).in(Scopes.SINGLETON);
            bind(PriorityMatchingSteps.class).in(Scopes.SINGLETON);
            bind(SandpitSteps.class).in(Scopes.SINGLETON);
        }

    }


}