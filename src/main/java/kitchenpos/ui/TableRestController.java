package kitchenpos.ui;

import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableRequest;

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
    public ResponseEntity<OrderTable> create(@RequestBody final OrderTableRequest orderTableRequest) {
        final OrderTable created = tableService.create(orderTableRequest);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTable>> list() {
        final List<OrderTable> orderTables = tableService.list();

        return ResponseEntity.ok()
                .body(orderTables);
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTable> changeEmpty(@PathVariable final Long orderTableId,
            @RequestBody final OrderTableRequest orderTableRequest) {
        final OrderTable orderTable1 = tableService.changeEmpty(orderTableId, orderTableRequest);

        return ResponseEntity.ok()
                .body(orderTable1);
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTable> changeNumberOfGuests(@PathVariable final Long orderTableId,
            @RequestBody final OrderTableRequest orderTableRequest) {
        final OrderTable orderTable = tableService.changeNumberOfGuests(orderTableId, orderTableRequest);

        return ResponseEntity.ok()
                .body(orderTable);
    }
}
