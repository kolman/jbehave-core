package org.jbehave.examples.trader;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.jbehave.Ensure.ensureThat;

import org.jbehave.examples.trader.converters.TraderConverter;
import org.jbehave.examples.trader.model.Stock;
import org.jbehave.examples.trader.model.Trader;
import org.jbehave.examples.trader.persistence.TraderPersister;
import org.jbehave.scenario.annotations.Alias;
import org.jbehave.scenario.annotations.Aliases;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Named;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.parser.PrefixCapturingPatternBuilder;
import org.jbehave.scenario.steps.ParameterConverters;
import org.jbehave.scenario.steps.SilentStepMonitor;
import org.jbehave.scenario.steps.StepMonitor;
import org.jbehave.scenario.steps.Steps;
import org.jbehave.scenario.steps.StepsConfiguration;

public class TraderSteps extends Steps {

	private static final StepsConfiguration configuration = new StepsConfiguration();
    private Stock stock;
    private Trader trader;

    public TraderSteps(ClassLoader classLoader) {
        super(configuration);
        StepMonitor monitor = new SilentStepMonitor();
		configuration.useParameterConverters(new ParameterConverters(
        		monitor, new TraderConverter(mockTradePersister())));
        configuration.usePatternBuilder(new PrefixCapturingPatternBuilder("%"));
        configuration.useMonitor(monitor);
    }

    private TraderPersister mockTradePersister() {
        return new TraderPersister(new Trader("Mauro", asList(new Stock("STK1", 10.d))));
    }

    @Given("a trader of name %trader")
    public void aTrader(Trader trader) {
        this.trader = trader;
    }

    @Given("a stock of <symbol> and a <threshold>")
    public void aStock(@Named("symbol") String symbol, @Named("threshold") double threshold) {
        stock = new Stock(symbol, threshold);
    }

    @When("the stock is traded with <price>")
    public void theStockIsBoughtAt(@Named("price") double price) {
        stock.tradeAt(price);
    }

    @Then("the trader is alerted with <status>")
    public void theAlertIs(@Named("status") String status) {
        ensureThat(stock.getStatus().name(), equalTo(status));
    }

    @Given("a stock of symbol %symbol and a threshold of %threshold")
    public void aStockWithNamedParameters(@Named("symbol") String symbol, @Named("threshold") double threshold) {
        stock = new Stock(symbol, threshold);
    }

    @When("the stock is traded at price %price")
    @Aliases(values={"the stock is sold at price %price"})
    public void theStockIsTradedAt(@Named("price") double price) {
        stock.tradeAt(price);
    }

    @Then("the alert status should be %status")
    @Alias("the alert status is %status")
    public void theAlertStatusShouldBe(@Named("status") String status) {
        ensureThat(stock.getStatus().name(), equalTo(status));
    }

    @When("the trader sells all stocks")
    public void theTraderSellsAllStocks() {
        trader.sellAllStocks();
    }

    @Then ("the trader is left with no stocks")
    public void theTraderIsLeftWithNoStocks() {
        ensureThat(trader.getStocks().size(), equalTo(0));
    }

}