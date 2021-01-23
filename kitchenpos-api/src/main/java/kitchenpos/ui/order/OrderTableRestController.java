package kitchenpos.ui.order;


import kitchenpos.application.order.OrderTableService;
import kitchenpos.dto.order.OrderTableRequest;
import kitchenpos.dto.order.OrderTableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class OrderTableRestController {
	private final OrderTableService orderTableService;

	public OrderTableRestController(final OrderTableService orderTableService) {
		this.orderTableService = orderTableService;
	}

	@PostMapping("/api/tables")
	public ResponseEntity<OrderTableResponse> createOrderTable(@RequestBody final OrderTableRequest request) {
		final OrderTableResponse created = orderTableService.createOrderTable(request);
		final URI uri = URI.create("/api/tables/" + created.getId());
		return ResponseEntity.created(uri).body(created);
	}

	@GetMapping("/api/tables")
	public ResponseEntity<List<OrderTableResponse>> listTables() {
		return ResponseEntity.ok().body(orderTableService.listTables());
	}

	@PutMapping("/api/tables/{orderTableId}/empty")
	public ResponseEntity<OrderTableResponse> changeEmpty(
			@PathVariable final Long orderTableId,
			@RequestBody final OrderTableRequest orderTable) {
		return ResponseEntity.ok().body(orderTableService.changeEmpty(orderTableId, orderTable));
	}

	@PutMapping("/api/tables/{orderTableId}/number-of-guests")
	public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
			@PathVariable final Long orderTableId,
			@RequestBody final OrderTableRequest orderTable) {
		return ResponseEntity.ok().body(orderTableService.changeNumberOfGuests(orderTableId, orderTable));
	}
}
