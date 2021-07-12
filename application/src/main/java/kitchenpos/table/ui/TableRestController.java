package kitchenpos.table.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.table.application.TableService;
import kitchenpos.table.dto.CreateOrderTableRequest;
import kitchenpos.table.dto.OrderTableDto;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.UpdateEmptyRequest;
import kitchenpos.table.dto.UpdateNumberOfGuestsRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static java.util.stream.Collectors.toList;

@RestController
public class TableRestController {
    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableResponse> create(@RequestBody CreateOrderTableRequest request) {
        OrderTableDto created = tableService.create(request.toDomainDto());
        URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri).body(OrderTableResponse.of(created));
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        return ResponseEntity.ok()
                             .body(tableService.list()
                                               .stream()
                                               .map(OrderTableResponse::of)
                                               .collect(toList()));
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(@PathVariable Long orderTableId,
                                                          @RequestBody UpdateEmptyRequest request) {
        return ResponseEntity.ok().body(
            OrderTableResponse.of(tableService.changeEmpty(orderTableId, request.isEmpty())));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(@PathVariable Long orderTableId,
                                                                   @RequestBody UpdateNumberOfGuestsRequest request) {
        return ResponseEntity.ok().body(OrderTableResponse.of(
            tableService.changeNumberOfGuests(orderTableId, request.getNumberOfGuests())));
    }
}
