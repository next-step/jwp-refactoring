package kitchenpos.ui;

import kitchenpos.application.TableService;
import kitchenpos.domain.NumberOfGuest;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableCreate;
import kitchenpos.dto.request.ChangeEmptyRequest;
import kitchenpos.dto.request.ChangeNumberOfGuestsRequest;
import kitchenpos.dto.request.OrderTableCreateRequest;
import kitchenpos.dto.response.OrderTableViewResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TableRestController {
    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableViewResponse> create(@RequestBody final OrderTableCreateRequest orderTableCreateRequest) {
        OrderTableCreate orderTable = new OrderTableCreate(
                new NumberOfGuest(orderTableCreateRequest.getNumberOfGuests()),
                orderTableCreateRequest.isEmpty()
        );

        final OrderTable created = tableService.create(orderTable);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(OrderTableViewResponse.of(created))
                ;
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableViewResponse>> list() {
        List<OrderTableViewResponse> results = tableService.list()
                .stream()
                .map(OrderTableViewResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .body(results);
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableViewResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final ChangeEmptyRequest changeEmptyRequest
    ) {
        return ResponseEntity.ok()
                .body(OrderTableViewResponse.of(tableService.changeEmpty(orderTableId, changeEmptyRequest.isEmpty())));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableViewResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final ChangeNumberOfGuestsRequest changeNumberOfGuestsRequest
    ) {
        NumberOfGuest numberOfGuest = new NumberOfGuest(changeNumberOfGuestsRequest.getNumberOfGuests());

        return ResponseEntity.ok()
                .body(
                        OrderTableViewResponse.of(
                                tableService.changeNumberOfGuests(
                                        orderTableId,
                                        numberOfGuest
                                ))
                );
    }
}
