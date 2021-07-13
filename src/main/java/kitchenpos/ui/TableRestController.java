package kitchenpos.ui;

import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.response.OrderTableResponse;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/tables", produces = MediaType.APPLICATION_JSON_VALUE)
public class TableRestController {
    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping
    public ResponseEntity<OrderTableResponse> create(@RequestBody final OrderTableRequest orderTableRequest) {
        final OrderTable created = tableService.create(orderTableRequest);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
            .body(OrderTableResponse.of(created));
    }

    @GetMapping
    public ResponseEntity<List<OrderTableResponse>> list() {
        List<OrderTableResponse> orderTableResponses = tableService.list().stream()
            .map(OrderTableResponse::of)
            .collect(Collectors.toList());

        return ResponseEntity.ok()
            .body(orderTableResponses);
    }

    @PutMapping("/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
        @PathVariable final Long orderTableId,
        @RequestBody final OrderTableRequest orderTableRequest
    ) {
        OrderTable orderTable = tableService.changeEmpty(orderTableId, orderTableRequest);

        return ResponseEntity.ok()
            .body(OrderTableResponse.of(orderTable));
    }

    @PutMapping("/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
        @PathVariable final Long orderTableId,
        @RequestBody final OrderTableRequest orderTableRequest
    ) {
        OrderTable orderTable = tableService.changeNumberOfGuests(orderTableId, orderTableRequest);

        return ResponseEntity.ok()
            .body(OrderTableResponse.of(orderTable));
    }
}
