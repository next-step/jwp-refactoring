package kitchenpos.table.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TableRestController {
    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableResponse> create(@RequestBody final OrderTableRequest orderTableRequest) {
        final OrderTable created = tableService.create(orderTableRequest);
        OrderTableResponse orderTableResponse = OrderTableResponse.of(created);
        final URI uri = URI.create("/api/tables/" + orderTableResponse.getId());
        return ResponseEntity.created(uri)
                .body(orderTableResponse)
                ;
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        List<OrderTable> orderTables = tableService.list();
        List<OrderTableResponse> orderTableResponses = orderTables.stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(orderTableResponses)
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableRequest orderTableRequest
    ) {
        OrderTable changeEmptyTable = tableService.changeEmpty(orderTableId, orderTableRequest);
        OrderTableResponse orderTableResponse = OrderTableResponse.of(changeEmptyTable);
        return ResponseEntity.ok()
                .body(orderTableResponse)
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableRequest orderTableRequest
    ) {
        OrderTable changeTable = tableService.changeNumberOfGuests(orderTableId, orderTableRequest);
        OrderTableResponse orderTableResponse = OrderTableResponse.of(changeTable);
        return ResponseEntity.ok()
                .body(orderTableResponse)
                ;
    }
}
