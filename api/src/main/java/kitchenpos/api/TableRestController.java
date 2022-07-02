package kitchenpos.api;

import kitchenpos.service.table.application.TableService;
import kitchenpos.service.table.dto.OrderTableRequest;
import kitchenpos.service.table.dto.OrderTableResponse;
import kitchenpos.service.table.dto.OrderTableUpdateEmptyRequest;
import kitchenpos.service.table.dto.OrderTableUpdateNumberOfGuestsRequest;
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
    public ResponseEntity<OrderTableResponse> create(@RequestBody final OrderTableRequest request) {
        final OrderTableResponse created = tableService.create(request);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        return ResponseEntity.ok().body(tableService.list());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
            @PathVariable final Long orderTableId, @RequestBody final OrderTableUpdateEmptyRequest request) {
        return ResponseEntity.ok().body(tableService.changeEmpty(orderTableId, request));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId, @RequestBody final OrderTableUpdateNumberOfGuestsRequest request) {
        return ResponseEntity.ok().body(tableService.changeNumberOfGuests(orderTableId, request));
    }
}
