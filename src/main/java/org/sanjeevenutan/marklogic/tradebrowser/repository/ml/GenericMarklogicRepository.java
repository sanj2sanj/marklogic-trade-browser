package org.sanjeevenutan.marklogic.tradebrowser.repository.ml;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.google.common.collect.Lists;
import com.marklogic.client.pojo.PojoPage;
import com.marklogic.client.pojo.PojoRepository;
import com.marklogic.client.query.QueryManager;
import com.marklogic.client.query.StringQueryDefinition;

public abstract class GenericMarklogicRepository<T extends Identifiable> {

	@Inject
	PojoRepository<T, Long> repo;

	@Inject
	QueryManager qm;

	static AtomicLong idGen = new AtomicLong();

	public void save(T t) {
		t.setId(idGen.getAndIncrement());
		repo.write(t);
	}

	public Page<T> findAll(Pageable generatePageRequest) {
		return new PageAdaptor(repo.readAll(generatePageRequest.getOffset()));
	}

	public void delete(Long id) {
		repo.delete(id);
	}

	public T findOne(Long id) {
		return repo.read(id);
	}

	public List<T> search(String queryString) {
		StringQueryDefinition query = qm.newStringDefinition().withCriteria(
				queryString);
		PojoPage<T> matches = repo.search(query, 1);
		return Lists.newArrayList(matches.iterator());
	}

}
