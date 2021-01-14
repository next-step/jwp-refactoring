package kitchenpos.ordertable.ui;

import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.dto.ChangeEmptyRequest;
import kitchenpos.ordertable.dto.ChangeNumberOfGuestsRequest;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
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
    public ResponseEntity<OrderTableResponse> create(@RequestBody final OrderTableRequest orderTableRequest) {
        OrderTableResponse orderTable = tableService.create(orderTableRequest);
        final URI uri = URI.create("/api/tables/" + orderTable.getId());
        return ResponseEntity.created(uri)
                .body(orderTable);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        List<OrderTableResponse> orderTables = tableService.findAll();
        return ResponseEntity.ok()
                .body(orderTables);
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final ChangeEmptyRequest request) {
        return ResponseEntity.ok()
                .body(tableService.changeEmpty(orderTableId, request.isEmpty()));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final ChangeNumberOfGuestsRequest request) {
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(orderTableId, request.getNumberOfGuests()));
    }
}
