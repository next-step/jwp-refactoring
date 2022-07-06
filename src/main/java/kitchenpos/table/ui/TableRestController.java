package kitchenpos.table.ui;

import kitchenpos.table.application.TableService;
import kitchenpos.table.dto.TableEmptyRequest;
import kitchenpos.table.dto.TableNumberOfGuestsRequest;
import kitchenpos.table.dto.TableRequest;
import kitchenpos.table.dto.TableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class TableRestController {
    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<TableResponse> create(@RequestBody final TableRequest request) {
        final TableResponse created = tableService.create(request);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<TableResponse>> list() {
        return ResponseEntity.ok()
                .body(tableService.list())
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<TableResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final TableEmptyRequest request
    ) {
        TableResponse table = tableService.changeEmpty(orderTableId, request.isEmpty());
        return ResponseEntity.ok()
                .body(table)
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<TableResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final TableNumberOfGuestsRequest request
    ) {
        TableResponse table = tableService.changeNumberOfGuests(orderTableId, request.getNumberOfGuests());
        return ResponseEntity.ok()
                .body(table)
                ;
    }
}
