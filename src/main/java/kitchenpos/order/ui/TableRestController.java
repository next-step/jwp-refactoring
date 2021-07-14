package kitchenpos.order.ui;

import kitchenpos.order.service.TableService;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TableRestController {
    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableResponse> create(@RequestBody final OrderTableRequest request) {
        final OrderTable created = tableService.create(request);
        return ResponseEntity.created(URI.create("/api/tables/" + created.getId()))
            .body(OrderTableResponse.of(created));
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        List<OrderTable> orderTables = tableService.list();
        return ResponseEntity.ok()
            .body(orderTables.stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList()));
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableRequest request) {
        OrderTable changedTable = tableService.changeEmpty(orderTableId, request);
        return ResponseEntity.ok().body(OrderTableResponse.of(changedTable));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableRequest request) {
        OrderTable changedTable = tableService.changeNumberOfGuests(orderTableId, request);
        return ResponseEntity.ok().body(OrderTableResponse.of(changedTable));
    }
}
