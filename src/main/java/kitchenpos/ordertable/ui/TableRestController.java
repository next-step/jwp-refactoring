package kitchenpos.ordertable.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableChangeEmptyRequest;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;

@RestController
public class TableRestController {
	private final TableService tableService;

	public TableRestController(final TableService tableService) {
		this.tableService = tableService;
	}

	@PostMapping("/api/tables")
	public ResponseEntity<OrderTableResponse> create(@RequestBody final OrderTableRequest orderTableRequest) {
		final OrderTableResponse created = tableService.create(orderTableRequest);
		final URI uri = URI.create("/api/tables/" + created.getId());
		return ResponseEntity.created(uri)
			.body(created);
	}

	@GetMapping("/api/tables")
	public ResponseEntity<List<OrderTableResponse>> list() {
		return ResponseEntity.ok()
			.body(tableService.list())
			;
	}

	@PutMapping("/api/tables/{orderTableId}/empty")
	public ResponseEntity<OrderTable> changeEmpty(
		@PathVariable final Long orderTableId,
		@RequestBody final OrderTableChangeEmptyRequest orderTableChangeEmptyRequest
	) {
		return ResponseEntity.ok()
			.body(tableService.changeEmpty(orderTableId, orderTableChangeEmptyRequest));
	}

	@PutMapping("/api/tables/{orderTableId}/number-of-guests")
	public ResponseEntity<OrderTable> changeNumberOfGuests(
		@PathVariable final Long orderTableId,
		@RequestBody final OrderTableRequest orderTableRequest
	) {
		return ResponseEntity.ok()
			.body(tableService.changeNumberOfGuests(orderTableId, orderTableRequest))
			;
	}
}
