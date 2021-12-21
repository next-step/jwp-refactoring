package kitchenpos.ui;

import kitchenpos.application.TableService;
import kitchenpos.dto.order.OrderTableRequest;
import kitchenpos.dto.order.OrderTableResponse;
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
    public ResponseEntity<OrderTableResponse> create(
        @RequestBody final OrderTableRequest orderTable) {
        final OrderTableResponse created = tableService.create(orderTable);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
            .body(created);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        return ResponseEntity.ok()
            .body(tableService.list());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
        @PathVariable final Long orderTableId,
        @RequestBody final OrderTableRequest orderTable
    ) {
        return ResponseEntity.ok()
            .body(tableService.changeEmpty(orderTableId, orderTable));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
        @PathVariable final Long orderTableId,
        @RequestBody final OrderTableRequest orderTable
    ) {
        return ResponseEntity.ok()
            .body(tableService.changeNumberOfGuests(orderTableId, orderTable));
    }
}
