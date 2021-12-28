package kitchenpos.tobe.orders.ordertable.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.tobe.orders.ordertable.application.OrderTableService;
import kitchenpos.tobe.orders.ordertable.dto.OrderTableChangeEmptyRequest;
import kitchenpos.tobe.orders.ordertable.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.tobe.orders.ordertable.dto.OrderTableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderTableController {

    private static final String BASE_PATH = "/api/tables/";

    private final OrderTableService orderTableService;

    public OrderTableController(final OrderTableService orderTableService) {
        this.orderTableService = orderTableService;
    }

    @PostMapping(BASE_PATH)
    public ResponseEntity<OrderTableResponse> create() {
        final OrderTableResponse response = orderTableService.create();
        final URI uri = URI.create(BASE_PATH + response.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping(BASE_PATH)
    public ResponseEntity<List<OrderTableResponse>> list() {
        return ResponseEntity.ok().body(orderTableService.list());
    }

    @PutMapping("{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
        @PathVariable final Long orderTableId,
        @RequestBody final OrderTableChangeEmptyRequest request
    ) {
        return ResponseEntity.ok()
            .body(orderTableService.changeEmpty(orderTableId, request));
    }

    @PutMapping("{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
        @PathVariable final Long orderTableId,
        @RequestBody final OrderTableChangeNumberOfGuestsRequest request
    ) {
        return ResponseEntity.ok()
            .body(orderTableService.changeNumberOfGuests(orderTableId, request));
    }
}
