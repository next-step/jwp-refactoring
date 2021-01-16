package kitchenpos.ui;

import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
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
// 주문테이블을 등록한다.
	// - id, tableGroupId, 손님수, empty여부
	//-- setTableGroupId를 null로 하고 저장함. 이상하네
	// 주문테이블 목록을 조회한다.
	// 주문테이블의 empty 상태를 업데이트한다.
	//-- orderTableId를 조회한다.
	//-- getTableGroupId가 널이면 안된다.
	//-- orderTableId에 COOKING, MEAL상태가 있으면 안된다. (tableGroup이랑 똑같음)
	// 주문테이블의 손님수를 업데이트한다.
	//-- 손님수는 0보다 작으면 안된다. 음수면 안된다.


	@PostMapping("/api/tables")
	public ResponseEntity<OrderTable> create(@RequestBody final OrderTable orderTable) {
		final OrderTable created = tableService.create(orderTable);
		final URI uri = URI.create("/api/tables/" + created.getId());
		return ResponseEntity.created(uri).body(created);
	}

	@GetMapping("/api/tables")
	public ResponseEntity<List<OrderTable>> list() {
		return ResponseEntity.ok().body(tableService.list());
	}

	@PutMapping("/api/tables/{orderTableId}/empty")
	public ResponseEntity<OrderTable> changeEmpty(
			@PathVariable final Long orderTableId,
			@RequestBody final OrderTable orderTable) {
		return ResponseEntity.ok().body(tableService.changeEmpty(orderTableId, orderTable));
	}

	@PutMapping("/api/tables/{orderTableId}/number-of-guests")
	public ResponseEntity<OrderTable> changeNumberOfGuests(
			@PathVariable final Long orderTableId,
			@RequestBody final OrderTable orderTable) {
		return ResponseEntity.ok().body(tableService.changeNumberOfGuests(orderTableId, orderTable));
	}
}
