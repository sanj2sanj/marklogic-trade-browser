package org.sanjeevenutan.marklogic.tradebrowser.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.sanjeevenutan.marklogic.tradebrowser.Application;
import org.sanjeevenutan.marklogic.tradebrowser.domain.Trade;
import org.sanjeevenutan.marklogic.tradebrowser.repository.TradeRepository;
import org.sanjeevenutan.marklogic.tradebrowser.repository.search.TradeSearchRepository;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test class for the TradeResource REST controller.
 *
 * @see TradeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TradeResourceTest {

	private static final String DEFAULT_STOCK = "SAMPLE_TEXT";
	private static final String UPDATED_STOCK = "UPDATED_TEXT";

	private static final LocalDate DEFAULT_TRADE_DATE = new LocalDate(0L);
	private static final LocalDate UPDATED_TRADE_DATE = new LocalDate();

	private static final LocalDate DEFAULT_SETTLEMENT_DATE = new LocalDate(0L);
	private static final LocalDate UPDATED_SETTLEMENT_DATE = new LocalDate();

	private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(0);
	private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(1);
	private static final String DEFAULT_CURRENCY = "SAMPLE_TEXT";
	private static final String UPDATED_CURRENCY = "UPDATED_TEXT";
	private static final String DEFAULT_COUNTERPARTY = "SAMPLE_TEXT";
	private static final String UPDATED_COUNTERPARTY = "UPDATED_TEXT";

	@Inject
	private TradeRepository tradeRepository;

	@Inject
	private TradeSearchRepository tradeSearchRepository;

	private MockMvc restTradeMockMvc;

	private Trade trade;

	@PostConstruct
	public void setup() {
		MockitoAnnotations.initMocks(this);
		TradeResource tradeResource = new TradeResource();
		ReflectionTestUtils.setField(tradeResource, "tradeRepository",
				tradeRepository);
		ReflectionTestUtils.setField(tradeResource, "tradeSearchRepository",
				tradeSearchRepository);
		this.restTradeMockMvc = MockMvcBuilders.standaloneSetup(tradeResource)
				.build();
	}

	@Before
	public void initTest() {
		trade = new Trade();
		trade.setStock(DEFAULT_STOCK);
		trade.setTradeDate(DEFAULT_TRADE_DATE);
		trade.setSettlementDate(DEFAULT_SETTLEMENT_DATE);
		trade.setAmount(DEFAULT_AMOUNT);
		trade.setCurrency(DEFAULT_CURRENCY);
		trade.setCounterparty(DEFAULT_COUNTERPARTY);
	}

	@Test
	@Transactional
	public void createTrade() throws Exception {
		int databaseSizeBeforeCreate = tradeRepository.findAll().size();

		// Create the Trade
		restTradeMockMvc.perform(
				post("/api/trades").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(trade)))
				.andExpect(status().isCreated());

		// Validate the Trade in the database
		List<Trade> trades = tradeRepository.findAll();
		assertThat(trades).hasSize(databaseSizeBeforeCreate + 1);
		Trade testTrade = trades.get(trades.size() - 1);
		assertThat(testTrade.getStock()).isEqualTo(DEFAULT_STOCK);
		assertThat(testTrade.getTradeDate()).isEqualTo(DEFAULT_TRADE_DATE);
		assertThat(testTrade.getSettlementDate()).isEqualTo(
				DEFAULT_SETTLEMENT_DATE);
		assertThat(testTrade.getAmount()).isEqualTo(DEFAULT_AMOUNT);
		assertThat(testTrade.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
		assertThat(testTrade.getCounterparty()).isEqualTo(DEFAULT_COUNTERPARTY);
	}

	@Test
	@Transactional
	public void checkStockIsRequired() throws Exception {
		// Validate the database is empty
		assertThat(tradeRepository.findAll()).hasSize(0);
		// set the field null
		trade.setStock(null);

		// Create the Trade, which fails.
		restTradeMockMvc.perform(
				post("/api/trades").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(trade)))
				.andExpect(status().isBadRequest());

		// Validate the database is still empty
		List<Trade> trades = tradeRepository.findAll();
		assertThat(trades).hasSize(0);
	}

	@Test
	@Transactional
	public void checkTradeDateIsRequired() throws Exception {
		// Validate the database is empty
		assertThat(tradeRepository.findAll()).hasSize(0);
		// set the field null
		trade.setTradeDate(null);

		// Create the Trade, which fails.
		restTradeMockMvc.perform(
				post("/api/trades").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(trade)))
				.andExpect(status().isBadRequest());

		// Validate the database is still empty
		List<Trade> trades = tradeRepository.findAll();
		assertThat(trades).hasSize(0);
	}

	@Test
	@Transactional
	public void checkSettlementDateIsRequired() throws Exception {
		// Validate the database is empty
		assertThat(tradeRepository.findAll()).hasSize(0);
		// set the field null
		trade.setSettlementDate(null);

		// Create the Trade, which fails.
		restTradeMockMvc.perform(
				post("/api/trades").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(trade)))
				.andExpect(status().isBadRequest());

		// Validate the database is still empty
		List<Trade> trades = tradeRepository.findAll();
		assertThat(trades).hasSize(0);
	}

	@Test
	@Transactional
	public void checkAmountIsRequired() throws Exception {
		// Validate the database is empty
		assertThat(tradeRepository.findAll()).hasSize(0);
		// set the field null
		trade.setAmount(null);

		// Create the Trade, which fails.
		restTradeMockMvc.perform(
				post("/api/trades").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(trade)))
				.andExpect(status().isBadRequest());

		// Validate the database is still empty
		List<Trade> trades = tradeRepository.findAll();
		assertThat(trades).hasSize(0);
	}

	@Test
	@Transactional
	public void checkCurrencyIsRequired() throws Exception {
		// Validate the database is empty
		assertThat(tradeRepository.findAll()).hasSize(0);
		// set the field null
		trade.setCurrency(null);

		// Create the Trade, which fails.
		restTradeMockMvc.perform(
				post("/api/trades").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(trade)))
				.andExpect(status().isBadRequest());

		// Validate the database is still empty
		List<Trade> trades = tradeRepository.findAll();
		assertThat(trades).hasSize(0);
	}

	@Test
	@Transactional
	public void checkCounterpartyIsRequired() throws Exception {
		// Validate the database is empty
		assertThat(tradeRepository.findAll()).hasSize(0);
		// set the field null
		trade.setCounterparty(null);

		// Create the Trade, which fails.
		restTradeMockMvc.perform(
				post("/api/trades").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(trade)))
				.andExpect(status().isBadRequest());

		// Validate the database is still empty
		List<Trade> trades = tradeRepository.findAll();
		assertThat(trades).hasSize(0);
	}

	@Test
	@Transactional
	public void getAllTrades() throws Exception {
		// Initialize the database
		tradeRepository.saveAndFlush(trade);

		// Get all the trades
		restTradeMockMvc
				.perform(get("/api/trades"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(
						jsonPath("$.[*].id").value(
								hasItem(trade.getId().intValue())))
				.andExpect(
						jsonPath("$.[*].stock").value(
								hasItem(DEFAULT_STOCK.toString())))
				.andExpect(
						jsonPath("$.[*].tradeDate").value(
								hasItem(DEFAULT_TRADE_DATE.toString())))
				.andExpect(
						jsonPath("$.[*].settlementDate").value(
								hasItem(DEFAULT_SETTLEMENT_DATE.toString())))
				.andExpect(
						jsonPath("$.[*].amount").value(
								hasItem(DEFAULT_AMOUNT.intValue())))
				.andExpect(
						jsonPath("$.[*].currency").value(
								hasItem(DEFAULT_CURRENCY.toString())))
				.andExpect(
						jsonPath("$.[*].counterparty").value(
								hasItem(DEFAULT_COUNTERPARTY.toString())));
	}

	@Test
	@Transactional
	public void getTrade() throws Exception {
		// Initialize the database
		tradeRepository.saveAndFlush(trade);

		// Get the trade
		restTradeMockMvc
				.perform(get("/api/trades/{id}", trade.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(trade.getId().intValue()))
				.andExpect(jsonPath("$.stock").value(DEFAULT_STOCK.toString()))
				.andExpect(
						jsonPath("$.tradeDate").value(
								DEFAULT_TRADE_DATE.toString()))
				.andExpect(
						jsonPath("$.settlementDate").value(
								DEFAULT_SETTLEMENT_DATE.toString()))
				.andExpect(
						jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
				.andExpect(
						jsonPath("$.currency").value(
								DEFAULT_CURRENCY.toString()))
				.andExpect(
						jsonPath("$.counterparty").value(
								DEFAULT_COUNTERPARTY.toString()));
	}

	@Test
	@Transactional
	public void getNonExistingTrade() throws Exception {
		// Get the trade
		restTradeMockMvc.perform(get("/api/trades/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateTrade() throws Exception {
		// Initialize the database
		tradeRepository.saveAndFlush(trade);

		int databaseSizeBeforeUpdate = tradeRepository.findAll().size();

		// Update the trade
		trade.setStock(UPDATED_STOCK);
		trade.setTradeDate(UPDATED_TRADE_DATE);
		trade.setSettlementDate(UPDATED_SETTLEMENT_DATE);
		trade.setAmount(UPDATED_AMOUNT);
		trade.setCurrency(UPDATED_CURRENCY);
		trade.setCounterparty(UPDATED_COUNTERPARTY);
		restTradeMockMvc.perform(
				put("/api/trades").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(trade)))
				.andExpect(status().isOk());

		// Validate the Trade in the database
		List<Trade> trades = tradeRepository.findAll();
		assertThat(trades).hasSize(databaseSizeBeforeUpdate);
		Trade testTrade = trades.get(trades.size() - 1);
		assertThat(testTrade.getStock()).isEqualTo(UPDATED_STOCK);
		assertThat(testTrade.getTradeDate()).isEqualTo(UPDATED_TRADE_DATE);
		assertThat(testTrade.getSettlementDate()).isEqualTo(
				UPDATED_SETTLEMENT_DATE);
		assertThat(testTrade.getAmount()).isEqualTo(UPDATED_AMOUNT);
		assertThat(testTrade.getCurrency()).isEqualTo(UPDATED_CURRENCY);
		assertThat(testTrade.getCounterparty()).isEqualTo(UPDATED_COUNTERPARTY);
	}

	@Test
	@Transactional
	public void deleteTrade() throws Exception {
		// Initialize the database
		tradeRepository.saveAndFlush(trade);

		int databaseSizeBeforeDelete = tradeRepository.findAll().size();

		// Get the trade
		restTradeMockMvc.perform(
				delete("/api/trades/{id}", trade.getId()).accept(
						TestUtil.APPLICATION_JSON_UTF8)).andExpect(
				status().isOk());

		// Validate the database is empty
		List<Trade> trades = tradeRepository.findAll();
		assertThat(trades).hasSize(databaseSizeBeforeDelete - 1);
	}
}
