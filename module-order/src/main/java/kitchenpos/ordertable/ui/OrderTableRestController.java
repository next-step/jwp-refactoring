package kitchenpos.ordertable.ui;

import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class OrderTableRestController {
    private final OrderTableService tableService;

    public OrderTableRestController(final OrderTableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableResponse> create(@RequestBody final OrderTableRequest OrderTableRequest) {
        final OrderTable created = tableService.create(OrderTableRequest);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri).body(OrderTableResponse.of(created));
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        return ResponseEntity.ok().body(OrderTableResponse.ofList(tableService.list()));
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableRequest OrderTableRequest
    ) {
        OrderTable update = tableService.changeEmpty(orderTableId, OrderTableRequest);
        return ResponseEntity.ok().body(OrderTableResponse.of(update));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableRequest OrderTableRequest
    ) {
        OrderTable update = tableService.changeNumberOfGuests(orderTableId, OrderTableRequest);
        return ResponseEntity.ok().body(OrderTableResponse.of(update));
    }

}
