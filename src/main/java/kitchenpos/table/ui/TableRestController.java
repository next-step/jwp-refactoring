package kitchenpos.table.ui;

import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.request.OrderTableRequest;
import kitchenpos.table.domain.response.OrderTableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/api/tables")
public class TableRestController {

    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping
    public ResponseEntity<OrderTable> create(@RequestBody final OrderTable orderTable) {
        final OrderTable created = tableService.create(orderTable);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @PostMapping
    public ResponseEntity<OrderTableResponse> createCopy(@RequestBody final OrderTableRequest orderTableRequest) {
        final OrderTableResponse created = tableService.createCopy(orderTableRequest);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping
    public ResponseEntity<List<OrderTable>> list() {
        return ResponseEntity.ok().body(tableService.list());
    }

    @GetMapping
    public ResponseEntity<List<OrderTableResponse>> listCopy() {
        return ResponseEntity.ok().body(tableService.listCopy());
    }

    @PutMapping("/{orderTableId}/empty")
    public ResponseEntity<OrderTable> changeEmpty(@PathVariable final Long orderTableId,
        @RequestBody final OrderTable orderTable) {
        return ResponseEntity.ok().body(tableService.changeEmpty(orderTableId, orderTable));
    }

    @PutMapping("/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmptyCopy(@PathVariable final Long orderTableId) {
        return ResponseEntity.ok().body(tableService.changeEmptyCopy(orderTableId));
    }

    @PutMapping("/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTable> changeNumberOfGuests(@PathVariable final Long orderTableId,
        @RequestBody final OrderTable orderTable) {
        return ResponseEntity.ok()
            .body(tableService.changeNumberOfGuests(orderTableId, orderTable));
    }

    @PutMapping("/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuestsCopy(@PathVariable final Long orderTableId,
        @RequestBody final OrderTableRequest orderTableRequest) {
        return ResponseEntity.ok()
            .body(tableService.changeNumberOfGuestsCopy(orderTableId, orderTableRequest));
    }
}
