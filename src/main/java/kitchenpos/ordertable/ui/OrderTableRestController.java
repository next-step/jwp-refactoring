package kitchenpos.ordertable.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderTableRestController {
	private final OrderTableService tableService;

	public OrderTableRestController(OrderTableService tableService) {
		this.tableService = tableService;
	}

	@PostMapping("/api/tables")
	public ResponseEntity<OrderTableResponse> create(@RequestBody OrderTableRequest request) {
		final OrderTableResponse created = tableService.create(request);
		final URI uri = URI.create("/api/tables/" + created.getId());
		return ResponseEntity.created(uri)
			  .body(created)
			  ;
	}

	@GetMapping("/api/tables")
	public ResponseEntity<List<OrderTableResponse>> list() {
		return ResponseEntity.ok()
			  .body(tableService.list())
			  ;
	}

	@PutMapping("/api/tables/{orderTableId}/empty")
	public ResponseEntity<OrderTableResponse> changeEmpty(
		  @PathVariable final Long orderTableId,
		  @RequestBody final OrderTableRequest request
	) {
		OrderTableResponse response = tableService.changeEmpty(orderTableId, request);
		return ResponseEntity.ok()
			  .body(response)
			  ;
	}

	@PutMapping("/api/tables/{orderTableId}/number-of-guests")
	public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
		  @PathVariable final Long orderTableId,
		  @RequestBody final OrderTableRequest request
	) {
		return ResponseEntity.ok()
			  .body(tableService.changeNumberOfGuests(orderTableId, request))
			  ;
	}
}
