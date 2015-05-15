package org.sanjeevenutan.marklogic.tradebrowser.repository;

import java.util.List;

import org.joda.time.LocalDate;
import org.sanjeevenutan.marklogic.tradebrowser.domain.PersistentToken;
import org.sanjeevenutan.marklogic.tradebrowser.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the PersistentToken entity.
 */
public interface PersistentTokenRepository extends
		JpaRepository<PersistentToken, String> {

	List<PersistentToken> findByUser(User user);

	List<PersistentToken> findByTokenDateBefore(LocalDate localDate);

}
