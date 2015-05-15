package org.sanjeevenutan.marklogic.tradebrowser.repository.ml;

import java.util.Iterator;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.google.common.collect.Lists;
import com.marklogic.client.pojo.PojoPage;

public class PageAdaptor<T> implements Page<T> {

	public PojoPage<T> pojoPage;

	public PageAdaptor(PojoPage<T> p) {
		this.pojoPage = p;
	}

	@Override
	public int getNumber() {
		return (int) pojoPage.getPageNumber();
	}

	@Override
	public int getSize() {
		return (int) pojoPage.getPageSize();
	}

	@Override
	public int getNumberOfElements() {
		return (int) pojoPage.size();
	}

	@Override
	public List<T> getContent() {
		return Lists.newArrayList(pojoPage.iterator());
	}

	@Override
	public boolean hasContent() {
		return pojoPage.hasContent();
	}

	@Override
	public Sort getSort() {
		return null;
	}

	@Override
	public boolean isFirst() {
		return pojoPage.isFirstPage();
	}

	@Override
	public boolean isLast() {
		return pojoPage.isLastPage();
	}

	@Override
	public boolean hasNext() {
		return pojoPage.hasNext();
	}

	@Override
	public boolean hasPrevious() {
		return pojoPage.hasPreviousPage();
	}

	@Override
	public Pageable nextPageable() {
		return null;
	}

	@Override
	public Pageable previousPageable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<T> iterator() {
		return pojoPage.iterator();
	}

	@Override
	public long getTotalElements() {
		return pojoPage.getTotalSize();
	}

	public void close() {
		pojoPage.close();
	}

	public T next() {
		return pojoPage.next();
	}

	public long getStart() {
		return pojoPage.getStart();
	}

	public long getPageSize() {
		return pojoPage.getPageSize();
	}

	public long getTotalSize() {
		return pojoPage.getTotalSize();
	}

	public long size() {
		return pojoPage.size();
	}

	public boolean hasNextPage() {
		return pojoPage.hasNextPage();
	}

	public boolean hasPreviousPage() {
		return pojoPage.hasPreviousPage();
	}

	public long getPageNumber() {
		return pojoPage.getPageNumber();
	}

	public boolean isFirstPage() {
		return pojoPage.isFirstPage();
	}

	public boolean isLastPage() {
		return pojoPage.isLastPage();
	}

	@Override
	public int getTotalPages() {
		// TODO Auto-generated method stub
		return 0;
	}

}
