package kitchenpos.table.ui;

import kitchenpos.table.application.TableService;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.http.MediaType;
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

    @PostMapping(value = "/api/tables", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderTableResponse> create(@RequestBody final OrderTableRequest orderTable) {
        final OrderTableResponse created = tableService.create(orderTable);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created);
    }

    @GetMapping(value = "/api/tables", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderTableResponse>> list() {
        return ResponseEntity.ok()
                .body(tableService.list());
    }

    @PutMapping(value = "/api/tables/{orderTableId}/empty", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderTableResponse> changeEmpty(@PathVariable final Long orderTableId, @RequestBody final OrderTableRequest orderTableRequest) {
        return ResponseEntity.ok()
                .body(tableService.changeEmpty(orderTableId, orderTableRequest));
    }

    @PutMapping(value = "/api/tables/{orderTableId}/number-of-guests", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(@PathVariable final Long orderTableId, @RequestBody final OrderTableRequest orderTableRequest) {
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(orderTableId, orderTableRequest));
    }
}
