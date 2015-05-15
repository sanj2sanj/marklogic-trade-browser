package org.sanjeevenutan.marklogic.tradebrowser;

import java.math.BigDecimal;
import java.util.Random;

import javax.inject.Inject;

import org.joda.time.LocalDate;
import org.sanjeevenutan.marklogic.tradebrowser.domain.Trade;
import org.sanjeevenutan.marklogic.tradebrowser.domain.TradeBuilder;
import org.sanjeevenutan.marklogic.tradebrowser.repository.ml.MarklogicTradeRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup implements
		ApplicationListener<ContextRefreshedEvent> {

	@Inject
	private MarklogicTradeRepository tradeRepository;

	static String[] cpys = { "CSI", "GSI", "BARCAP" };

	static String[] currency = { "GBP", "USD", "EUR" };

	static String[] stockNames = ("AAL.L\n" + "ABF.L\n" + "ADM.L\n" + "AGK.L\n"
			+ "AMEC.L\n" + "ANTO.L\n" + "ARM.L\n" + "AU.L\n" + "AV.L\n"
			+ "AZN.L\n" + "BA.L\n" + "BARC.L\n" + "BATS.L\n" + "BG.L\n"
			+ "BLND.L\n" + "BLT.L\n" + "BP.L\n" + "BRBY.L\n" + "BSY.L\n"
			+ "BT-A.L\n" + "CCL.L\n" + "CNA.L\n" + "CNE.L\n" + "CPG.L\n"
			+ "CPI.L\n" + "CSCG.L\n" + "DGE.L\n" + "EMG.L\n" + "ENRC.L\n"
			+ "ESSR.L\n" + "EXPN.L\n" + "FRES.L\n" + "GFS.L\n" + "GKN.L")
			.split("\n");

	@Override
	public void onApplicationEvent(final ContextRefreshedEvent event) {

		Random random = new Random();
		for (int i = 0; i < 2000; i++) {
			String stock = stockNames[random
					.nextInt((stockNames.length - 1 - 0) + 1)];
			String ccy = currency[random.nextInt((currency.length - 1 - 0) + 1)];
			String cpy = cpys[random.nextInt((cpys.length - 1 - 0) + 1)];

			int offset = random.nextInt((30 - 1) + 1);

			Trade t = TradeBuilder.trade().withId(Long.valueOf(i))
					.withSettlementDate(LocalDate.now().plusDays(offset))
					.withStock(stock)
					.withTradeDate(LocalDate.now().plusDays(offset))
					.withAmount(BigDecimal.valueOf(random.nextInt()))
					.withCurrency(ccy).withCounterparty(cpy).build();
			tradeRepository.save(t);
		}

		return;
	}

}