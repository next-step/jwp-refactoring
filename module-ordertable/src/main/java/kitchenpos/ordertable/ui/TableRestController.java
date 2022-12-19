package kitchenpos.ordertable.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableChangeEmptyRequest;
import kitchenpos.ordertable.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
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
        OrderTableResponse orderTableResponse = new OrderTableResponse(created.getId(), created.getTableGroupId(),
                created.getNumberOfGuests(), created.isEmpty());

        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(orderTableResponse)
                ;
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        List<OrderTableResponse> orderTableResponses = tableService.list().stream()
                .map(orderTable -> new OrderTableResponse(orderTable.getId(), orderTable.getTableGroupId(),
                        orderTable.getNumberOfGuests(), orderTable.isEmpty()))
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .body(orderTableResponses)
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableChangeEmptyRequest emptyRequest
    ) {
        OrderTable orderTable = tableService.changeEmpty(orderTableId, emptyRequest);
        OrderTableResponse orderTableResponse = new OrderTableResponse(orderTable.getId(), orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(), orderTable.isEmpty());

        return ResponseEntity.ok()
                .body(orderTableResponse)
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableChangeNumberOfGuestsRequest numberOfGuestsRequest
    ) {
        OrderTable orderTable = tableService.changeNumberOfGuests(orderTableId, numberOfGuestsRequest);
        OrderTableResponse orderTableResponse = new OrderTableResponse(orderTable.getId(), orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(), orderTable.isEmpty());

        return ResponseEntity.ok()
                .body(orderTableResponse)
                ;
    }
}
