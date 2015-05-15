package org.sanjeevenutan.marklogic.tradebrowser.repository.search;

import org.sanjeevenutan.marklogic.tradebrowser.domain.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the User entity.
 */
public interface UserSearchRepository extends
		ElasticsearchRepository<User, Long> {
}
