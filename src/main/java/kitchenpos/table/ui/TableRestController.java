package kitchenpos.table.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.table.application.TableService;
import kitchenpos.table.ui.dto.OrderTableRequest;
import kitchenpos.table.ui.dto.OrderTableResponse;

@RestController
public class TableRestController {
	private final TableService tableService;

	public TableRestController(TableService tableService) {
		this.tableService = tableService;
	}

	@PostMapping("/api/tables")
	public ResponseEntity<OrderTableResponse> create(@RequestBody OrderTableRequest request) {
		OrderTableResponse created = new OrderTableResponse(
			tableService.create(
				request.toOrderTable()));
		URI uri = URI.create("/api/tables/" + created.getId());

		return ResponseEntity
			.created(uri)
			.body(created);
	}

	@GetMapping("/api/tables")
	public ResponseEntity<List<OrderTableResponse>> list() {
		List<OrderTableResponse> orderTables = OrderTableResponse.of(
			tableService.findAll());

		return ResponseEntity
			.ok()
			.body(
				orderTables);
	}

	@PutMapping("/api/tables/{orderTableId}/empty")
	public ResponseEntity<OrderTableResponse> changeEmpty(
		@PathVariable Long orderTableId,
		@RequestBody OrderTableRequest request) {
		OrderTableResponse orderTable = new OrderTableResponse(
			tableService.changeEmpty(orderTableId, request.toOrderTable()));

		return ResponseEntity.ok()
							 .body(orderTable);
	}

	@PutMapping("/api/tables/{orderTableId}/number-of-guests")
	public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
		@PathVariable Long orderTableId,
		@RequestBody OrderTableRequest request) {
		OrderTableResponse orderTable = new OrderTableResponse(
			tableService.changeNumberOfGuests(orderTableId, request.toOrderTable()));

		return ResponseEntity
			.ok()
			.body(
				orderTable);
	}
}
