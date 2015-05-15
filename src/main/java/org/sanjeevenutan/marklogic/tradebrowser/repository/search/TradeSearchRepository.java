package org.sanjeevenutan.marklogic.tradebrowser.repository.search;

import org.sanjeevenutan.marklogic.tradebrowser.domain.Trade;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Trade entity.
 */
public interface TradeSearchRepository extends
		ElasticsearchRepository<Trade, Long> {
}
