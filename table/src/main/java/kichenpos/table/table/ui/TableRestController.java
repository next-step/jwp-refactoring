package kichenpos.table.table.ui;

import java.net.URI;
import java.util.List;
import kichenpos.table.table.application.TableService;
import kichenpos.table.table.ui.request.EmptyRequest;
import kichenpos.table.table.ui.request.OrderTableRequest;
import kichenpos.table.table.ui.request.TableGuestsCountRequest;
import kichenpos.table.table.ui.response.OrderTableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tables")
public class TableRestController {

    private final TableService tableService;

    public TableRestController(TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping
    public ResponseEntity<OrderTableResponse> create(@RequestBody OrderTableRequest request) {
        OrderTableResponse created = tableService.create(request);
        URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
            .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderTableResponse> find(@PathVariable long id) {
        return ResponseEntity.ok()
            .body(tableService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<OrderTableResponse>> list() {
        return ResponseEntity.ok()
            .body(tableService.list());
    }

    @PutMapping("/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
        @PathVariable long orderTableId, @RequestBody EmptyRequest request) {
        return ResponseEntity.ok()
            .body(tableService.changeEmpty(orderTableId, request));
    }

    @PutMapping("/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
        @PathVariable long orderTableId,
        @RequestBody TableGuestsCountRequest request) {
        return ResponseEntity.ok()
            .body(tableService.changeNumberOfGuests(orderTableId, request));
    }

    @PostMapping("/{orderTableId}/order")
    public ResponseEntity<Void> changeOrdered(@PathVariable long orderTableId) {
        tableService.changeOrdered(orderTableId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{orderTableId}/finish")
    public ResponseEntity<Void> changeFinish(@PathVariable long orderTableId) {
        tableService.changeFinish(orderTableId);
        return ResponseEntity.noContent().build();
    }
}
