// CHECKSTYLE:OFF
/**
 * Source code generated by Fluent Builders Generator
 * Do not modify this file
 * See generator home page at: http://code.google.com/p/fluent-builders-generator-eclipse-plugin/
 */

package org.sanjeevenutan.marklogic.tradebrowser.domain;

import java.math.BigDecimal;

import org.joda.time.LocalDate;

public class TradeBuilder extends TradeBuilderBase<TradeBuilder> {
	public static TradeBuilder trade() {
		return new TradeBuilder();
	}

	public TradeBuilder() {
		super(new Trade());
	}

	public Trade build() {
		return getInstance();
	}
}

class TradeBuilderBase<GeneratorT extends TradeBuilderBase<GeneratorT>> {
	private Trade instance;

	protected TradeBuilderBase(Trade aInstance) {
		instance = aInstance;
	}

	protected Trade getInstance() {
		return instance;
	}

	@SuppressWarnings("unchecked")
	public GeneratorT withId(Long aValue) {
		instance.setId(aValue);

		return (GeneratorT) this;
	}

	@SuppressWarnings("unchecked")
	public GeneratorT withStock(String aValue) {
		instance.setStock(aValue);

		return (GeneratorT) this;
	}

	@SuppressWarnings("unchecked")
	public GeneratorT withTradeDate(LocalDate aValue) {
		instance.setTradeDate(aValue);

		return (GeneratorT) this;
	}

	@SuppressWarnings("unchecked")
	public GeneratorT withSettlementDate(LocalDate aValue) {
		instance.setSettlementDate(aValue);

		return (GeneratorT) this;
	}

	@SuppressWarnings("unchecked")
	public GeneratorT withAmount(BigDecimal aValue) {
		instance.setAmount(aValue);

		return (GeneratorT) this;
	}

	@SuppressWarnings("unchecked")
	public GeneratorT withCurrency(String aValue) {
		instance.setCurrency(aValue);

		return (GeneratorT) this;
	}

	@SuppressWarnings("unchecked")
	public GeneratorT withCounterparty(String aValue) {
		instance.setCounterparty(aValue);

		return (GeneratorT) this;
	}
}
