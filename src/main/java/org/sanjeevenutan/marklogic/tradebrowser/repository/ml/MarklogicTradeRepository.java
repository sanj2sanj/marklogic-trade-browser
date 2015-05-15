package org.sanjeevenutan.marklogic.tradebrowser.repository.ml;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Inject;

import org.assertj.core.util.Lists;
import org.sanjeevenutan.marklogic.tradebrowser.domain.Trade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.marklogic.client.pojo.PojoPage;
import com.marklogic.client.pojo.PojoRepository;
import com.marklogic.client.query.QueryManager;
import com.marklogic.client.query.StringQueryDefinition;

@Repository
public class MarklogicTradeRepository{

	@Inject
	PojoRepository<Trade, Long> tradeRepo;
	
	@Inject
	QueryManager qm;

	static AtomicLong idGen = new AtomicLong();

	public void save(Trade trade) {
		trade.setId(idGen.getAndIncrement());
		tradeRepo.write(trade);
	}

	public Page<Trade> findAll(Pageable generatePageRequest) {
		return new PageAdaptor(tradeRepo.readAll(generatePageRequest
				.getOffset()));
	}

	public void delete(Long id) {
		tradeRepo.delete(id);
	}

	public Trade findOne(Long id) {
		return tradeRepo.readAll(id).next();
	}

	public List<Trade> search(String queryString) {			
		StringQueryDefinition query = qm.newStringDefinition().withCriteria(queryString);
		PojoPage<Trade> matches = tradeRepo.search(query, 1);		
		return Lists.newArrayList(matches.iterator());
	}

}
