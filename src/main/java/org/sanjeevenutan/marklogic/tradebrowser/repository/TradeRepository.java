package org.sanjeevenutan.marklogic.tradebrowser.repository;

import org.sanjeevenutan.marklogic.tradebrowser.domain.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Trade entity.
 */
public interface TradeRepository extends JpaRepository<Trade, Long> {

}
