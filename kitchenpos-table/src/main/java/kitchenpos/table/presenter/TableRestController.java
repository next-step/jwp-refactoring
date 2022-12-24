package kitchenpos.table.presenter;

import kitchenpos.table.application.TableService;
import kitchenpos.table.dto.ChaneNumberOfGuestRequest;
import kitchenpos.table.dto.ChangeEmptyRequest;
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
        final TableResponse tableResponse = tableService.create(request);
        final URI uri = URI.create("/api/tables/" + tableResponse.getId());
        return ResponseEntity.created(uri).body(tableResponse);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<TableResponse>> list() {
        return ResponseEntity.ok().body(tableService.list());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<TableResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final ChangeEmptyRequest request
    ) {
        return ResponseEntity.ok().body(tableService.changeEmpty(orderTableId, request));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<TableResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final ChaneNumberOfGuestRequest request
    ) {
        return ResponseEntity.ok().body(
                tableService.changeNumberOfGuests(orderTableId, request));
    }
}
