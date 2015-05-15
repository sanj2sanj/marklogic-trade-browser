package org.sanjeevenutan.marklogic.tradebrowser.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.sanjeevenutan.marklogic.tradebrowser.domain.Trade;
import org.sanjeevenutan.marklogic.tradebrowser.repository.ml.MarklogicTradeRepository;
import org.sanjeevenutan.marklogic.tradebrowser.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

/**
 * REST controller for managing Trade.
 */
@RestController
@RequestMapping("/api")
public class TradeResource {

	private final Logger log = LoggerFactory.getLogger(TradeResource.class);

	@Inject
	private MarklogicTradeRepository tradeRepository;

	/**
	 * POST /trades -> Create a new trade.
	 */
	@RequestMapping(value = "/trades", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Void> create(@Valid @RequestBody Trade trade)
			throws URISyntaxException {
		log.debug("REST request to save Trade : {}", trade);
		if (trade.getId() != null) {
			return ResponseEntity.badRequest()
					.header("Failure", "A new trade cannot already have an ID")
					.build();
		}
		tradeRepository.save(trade);
		return ResponseEntity.created(new URI("/api/trades/" + trade.getId()))
				.build();
	}

	/**
	 * PUT /trades -> Updates an existing trade.
	 */
	@RequestMapping(value = "/trades", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Void> update(@Valid @RequestBody Trade trade)
			throws URISyntaxException {
		log.debug("REST request to update Trade : {}", trade);
		if (trade.getId() == null) {
			return create(trade);
		}
		tradeRepository.save(trade);
		return ResponseEntity.ok().build();
	}

	/**
	 * GET /trades -> get all the trades.
	 */
	@RequestMapping(value = "/trades", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<Trade>> getAll(
			@RequestParam(value = "page", required = false) Integer offset,
			@RequestParam(value = "per_page", required = false) Integer limit)
			throws URISyntaxException {
		Page<Trade> page = tradeRepository.findAll(PaginationUtil
				.generatePageRequest(offset, limit));
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
				page, "/api/trades", offset, limit);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /trades/:id -> get the "id" trade.
	 */
	@RequestMapping(value = "/trades/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Trade> get(@PathVariable Long id) {
		log.debug("REST request to get Trade : {}", id);
		return Optional.ofNullable(tradeRepository.findOne(id))
				.map(trade -> new ResponseEntity<>(trade, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /trades/:id -> delete the "id" trade.
	 */
	@RequestMapping(value = "/trades/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public void delete(@PathVariable Long id) {
		log.debug("REST request to delete Trade : {}", id);
		tradeRepository.delete(id);
	}

	/**
	 * SEARCH /_search/trades/:query -> search for the trade corresponding to
	 * the query.
	 */
	@RequestMapping(value = "/_search/trades/{query}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<Trade> search(@PathVariable String query) {
		return tradeRepository.search(query);
		// return StreamSupport
		// .stream(tradeRepository.search(query).spliterator(), false)
		// .collect(Collectors.toList());
	}
}
