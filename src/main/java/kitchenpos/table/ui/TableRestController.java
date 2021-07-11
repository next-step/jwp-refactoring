package kitchenpos.table.ui;

import kitchenpos.table.application.OrderTableService;
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
    public ResponseEntity<OrderTableResponse> createTemp(@RequestBody final OrderTableRequest orderTableRequest) {
        final OrderTableResponse created = orderTableService.create(orderTableRequest);
        final URI uri = URI.create("/api/tables/" + created);
        return ResponseEntity.created(uri)
                .body(created);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> listTemp() {
        return ResponseEntity.ok()
                .body(orderTableService.list());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmptyTemp(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableRequest orderTableRequest
    ) {
        return ResponseEntity.ok()
                .body(orderTableService.changeEmpty(orderTableId, orderTableRequest));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuestsTemp(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableRequest orderTableRequest
    ) {
        return ResponseEntity.ok()
                .body(orderTableService.changeNumberOfGuests(orderTableId, orderTableRequest));
    }
}
