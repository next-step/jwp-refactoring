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

import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.dto.OrderTableDto;
import kitchenpos.ordertable.dto.OrderTableRequest;

@RestController
public class OrderTableRestController {
	private final OrderTableService orderTableService;

	public OrderTableRestController(OrderTableService orderTableService) {
		this.orderTableService = orderTableService;
	}

	@PostMapping("/api/tables")
	public ResponseEntity<OrderTableDto> create(@RequestBody OrderTableRequest request) {
		OrderTableDto orderTable = orderTableService.create(request);
		URI uri = URI.create("/api/tables/" + orderTable.getId());
		return ResponseEntity.created(uri).body(orderTable);
	}

	@GetMapping("/api/tables")
	public ResponseEntity<List<OrderTableDto>> list() {
		List<OrderTableDto> orderTables = orderTableService.list();
		return ResponseEntity.ok().body(orderTables);
	}

	@PutMapping("/api/tables/{orderTableId}/empty")
	public ResponseEntity<OrderTableDto> changeEmpty(
		@PathVariable Long orderTableId,
		@RequestBody OrderTableRequest request
	) {
		OrderTableDto orderTable = orderTableService.changeEmpty(orderTableId, request);
		return ResponseEntity.ok().body(orderTable);
	}

	@PutMapping("/api/tables/{orderTableId}/number-of-guests")
	public ResponseEntity<OrderTableDto> changeNumberOfGuests(
		@PathVariable Long orderTableId,
		@RequestBody OrderTableRequest request
	) {
		OrderTableDto orderTable = orderTableService.changeNumberOfGuests(orderTableId, request);
		return ResponseEntity.ok().body(orderTable);
	}
}
