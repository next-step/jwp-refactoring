package kitchenpos.table.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;

@RequestMapping("/api/tables")
@RestController
public class TableRestController {
	private final TableService tableService;

	public TableRestController(final TableService tableService) {
		this.tableService = tableService;
	}

	@PostMapping
	public ResponseEntity<OrderTableResponse> create(@RequestBody final OrderTableRequest orderTableRequest) {
		final OrderTable created = tableService.create(orderTableRequest);
		final URI uri = URI.create("/api/tables/" + created.getId());
		return ResponseEntity.created(uri)
			.body(OrderTableResponse.from(created));
	}

	@GetMapping
	public ResponseEntity<List<OrderTableResponse>> list() {
		return ResponseEntity.ok()
			.body(OrderTableResponse.ofList(tableService.list()));
	}

	@PutMapping("/{orderTableId}/empty")
	public ResponseEntity<OrderTableResponse> changeEmpty(@PathVariable final Long orderTableId,
		@RequestBody final OrderTableRequest orderTableRequest
	) {
		return ResponseEntity.ok()
			.body(OrderTableResponse.from(tableService.changeEmpty(orderTableId, orderTableRequest)));
	}

	@PutMapping("/{orderTableId}/number-of-guests")
	public ResponseEntity<OrderTableResponse> changeNumberOfGuests(@PathVariable final Long orderTableId,
		@RequestBody final OrderTableRequest orderTableRequest
	) {
		return ResponseEntity.ok()
			.body(OrderTableResponse.from(tableService.changeNumberOfGuests(orderTableId, orderTableRequest)));
	}
}
