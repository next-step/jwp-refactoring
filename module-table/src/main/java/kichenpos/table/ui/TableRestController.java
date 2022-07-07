package kichenpos.table.ui;

import kichenpos.table.application.TableService;
import kichenpos.table.ui.dto.*;
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
    public ResponseEntity<OrderTableCreateResponse> create(@RequestBody final OrderTableCreateRequest request) {
        final OrderTableCreateResponse created = new OrderTableCreateResponse(tableService.create(request.toEntity()));
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        return ResponseEntity.ok()
                .body(tableService.list()
                        .stream()
                        .map(OrderTableResponse::new)
                        .collect(Collectors.toList())
                )
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableUpdateResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableUpdateRequest request
    ) {
        OrderTableUpdateResponse response = new OrderTableUpdateResponse(tableService.changeEmpty(orderTableId, request.toEntity()));
        return ResponseEntity.ok()
                .body(response)
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableUpdateResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableUpdateRequest request
    ) {
        OrderTableUpdateResponse response = new OrderTableUpdateResponse(tableService.changeNumberOfGuests(orderTableId, request.toEntity()));
        return ResponseEntity.ok()
                .body(response)
                ;
    }
}
