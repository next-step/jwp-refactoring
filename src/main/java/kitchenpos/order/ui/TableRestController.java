package kitchenpos.order.ui;

import kitchenpos.order.applicaiton.OrderTableService;
import kitchenpos.order.domain.OrderEmpty;
import kitchenpos.order.domain.OrderGuests;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
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
    public ResponseEntity<OrderTableResponse> create(@RequestBody final OrderTableRequest orderTableRequest) {
        final OrderTableResponse created = orderTableService.create(orderTableRequest);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        return ResponseEntity.ok()
                .body(orderTableService.list())
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderEmpty orderEmptyRequest
    ) {
        return ResponseEntity.ok()
                .body(orderTableService.changeEmpty(orderTableId, orderEmptyRequest))
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderGuests changeGuestRequest
    ) {
        return ResponseEntity.ok()
                .body(orderTableService.changeNumberOfGuests(orderTableId, changeGuestRequest))
                ;
    }
}
