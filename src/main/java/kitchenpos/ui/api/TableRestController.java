package kitchenpos.ui.api;

import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.order.OrderTableRequest;
import kitchenpos.ui.dto.order.OrderTableResponse;
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
    public ResponseEntity<OrderTable> create(@RequestBody final OrderTable orderTable) {
        final OrderTable created = tableService.create(orderTable);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTable>> list() {
        return ResponseEntity.ok()
                .body(tableService.list())
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTable> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTable orderTable
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeEmpty(orderTableId, orderTable))
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTable> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTable orderTable
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(orderTableId, orderTable))
                ;
    }

    @PostMapping("/api/tables2")
    public ResponseEntity<OrderTableResponse> create2(@RequestBody final OrderTableRequest request) {
        final OrderTable created = tableService.create(request.toOrderTable());
        final URI uri = URI.create("/api/tables2/" + created.getId());
        return ResponseEntity.created(uri)
                .body(OrderTableResponse.of(created))
                ;
    }

    @GetMapping("/api/tables2")
    public ResponseEntity<List<OrderTableResponse>> list2() {
        return ResponseEntity.ok()
                .body(OrderTableResponse.ofList(tableService.list()))
                ;
    }

    @PutMapping("/api/tables2/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty2(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableRequest request
    ) {
        return ResponseEntity.ok()
                .body(OrderTableResponse.of(tableService.changeEmpty(orderTableId, request.toOrderTable())))
                ;
    }

    @PutMapping("/api/tables2/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests2(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableRequest request
    ) {
        return ResponseEntity.ok()
                .body(OrderTableResponse.of(tableService.changeNumberOfGuests(orderTableId, request.toOrderTable())))
                ;
    }
}
