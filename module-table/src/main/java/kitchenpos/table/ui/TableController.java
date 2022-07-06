package kitchenpos.table.ui;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import kitchenpos.table.application.TableService;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tables")
public class TableController {
    private final TableService tableService;

    public TableController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping
    public ResponseEntity<OrderTableResponse> create(@RequestBody @Valid final OrderTableRequest orderTableRequest) {
        final OrderTableResponse created = tableService.create(orderTableRequest);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity
                .created(uri)
                .body(created);
    }

    @GetMapping
    public ResponseEntity<List<OrderTableResponse>> list() {
        return ResponseEntity
                .ok()
                .body(tableService.list());
    }

    @PutMapping("/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(@PathVariable final Long orderTableId) {
        return ResponseEntity
                .ok()
                .body(tableService.changeEmpty(orderTableId));
    }

    @PutMapping("/{orderTableId}/number-of-guests/{guestNumber}")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @PathVariable final Integer guestNumber
    ) {
        return ResponseEntity
                .ok()
                .body(tableService.changeNumberOfGuests(orderTableId, guestNumber));
    }
}
