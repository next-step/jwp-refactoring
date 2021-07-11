package kitchenpos.table.ui;

import kitchenpos.table.application.OrderTableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class TableRestController {
    private final OrderTableService orderTableService;

    public TableRestController(final OrderTableService orderTableService) {
        this.orderTableService = orderTableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTable> create(@RequestBody final OrderTable orderTable) {
        final OrderTable created = orderTableService.create(orderTable);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @PostMapping("/api/tables/temp")
    public ResponseEntity<OrderTableResponse> createTemp(@RequestBody final OrderTableRequest orderTableRequest) {
        final OrderTableResponse created = orderTableService.createTemp(orderTableRequest);
        final URI uri = URI.create("/api/tables/" + created);
        return ResponseEntity.created(uri)
                .body(created);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTable>> list() {
        return ResponseEntity.ok()
                .body(orderTableService.list())
                ;
    }

    @GetMapping("/api/tables/temp")
    public ResponseEntity<List<OrderTableResponse>> listTemp() {
        return ResponseEntity.ok()
                .body(orderTableService.listTemp());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTable> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTable orderTable
    ) {
        return ResponseEntity.ok()
                .body(orderTableService.changeEmpty(orderTableId, orderTable))
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/empty/temp")
    public ResponseEntity<OrderTableResponse> changeEmptyTemp(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableRequest orderTableRequest
    ) {
        return ResponseEntity.ok()
                .body(orderTableService.changeEmptyTemp(orderTableId, orderTableRequest))
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTable> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTable orderTable
    ) {
        return ResponseEntity.ok()
                .body(orderTableService.changeNumberOfGuests(orderTableId, orderTable))
                ;
    }
}
