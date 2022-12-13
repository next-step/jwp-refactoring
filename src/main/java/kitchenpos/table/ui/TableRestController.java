package kitchenpos.table.ui;

import kitchenpos.table.application.TableService;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequestMapping("/api/tables")
@RestController
public class TableRestController {
    private final TableService tableService;

    public TableRestController(TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping
    public ResponseEntity<OrderTableResponse> create(@RequestBody OrderTableRequest orderTableRequest) {
        OrderTableResponse response = tableService.create(orderTableRequest);
        URI uri = URI.create("/api/tables/" + response.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderTableResponse>> list() {
        return ResponseEntity.ok().body(tableService.list());
    }

    @PutMapping("/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
            @PathVariable Long orderTableId, @RequestBody OrderTableRequest request) {
        return ResponseEntity.ok().body(tableService.changeEmpty(orderTableId, request));
    }

    @PutMapping("/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
            @PathVariable Long orderTableId, @RequestBody OrderTableRequest request) {
        return ResponseEntity.ok().body(tableService.changeNumberOfGuests(orderTableId, request));
    }

}
