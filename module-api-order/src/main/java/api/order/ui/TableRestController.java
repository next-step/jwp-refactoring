package api.order.ui;

import api.order.application.TableService;
import api.order.dto.OrderTableRequest_ChangeEmpty;
import api.order.dto.OrderTableRequest_ChangeGuests;
import api.order.dto.OrderTableRequest_Create;
import api.order.dto.OrderTableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class TableRestController {
	private final TableService tableService;

	public TableRestController(final TableService tableService) {
		this.tableService = tableService;
	}

	@PostMapping("/api/tables")
	public ResponseEntity<OrderTableResponse> create(@RequestBody OrderTableRequest_Create request) {
		final OrderTableResponse created = tableService.create(request);
		final URI uri = URI.create("/api/tables/" + created.getId());
		return ResponseEntity.created(uri).body(created);
	}

	@GetMapping("/api/tables")
	public ResponseEntity<List<OrderTableResponse>> list() {
		return ResponseEntity.ok().body(tableService.list());
	}

	@PutMapping("/api/tables/{orderTableId}/empty")
	public ResponseEntity<OrderTableResponse> changeEmpty(@PathVariable Long orderTableId,
	                                                      @RequestBody OrderTableRequest_ChangeEmpty request) {
		return ResponseEntity.ok().body(tableService.changeEmpty(orderTableId, request));
	}

	@PutMapping("/api/tables/{orderTableId}/number-of-guests")
	public ResponseEntity<OrderTableResponse> changeNumberOfGuests(@PathVariable Long orderTableId,
	                                                               @RequestBody OrderTableRequest_ChangeGuests request) {
		return ResponseEntity.ok().body(tableService.changeNumberOfGuests(orderTableId, request));
	}
}
