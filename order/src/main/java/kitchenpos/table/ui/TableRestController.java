package kitchenpos.table.ui;

import kitchenpos.table.application.TableService;
import kitchenpos.table.dto.ChangeTableEmptyRequest;
import kitchenpos.table.dto.ChangeTableGuestRequest;
import kitchenpos.table.dto.TableRequest;
import kitchenpos.table.dto.TableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class TableRestController {
    private static final String BASE_URL = "/api/tables";

    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping(BASE_URL)
    public ResponseEntity<TableResponse> create(@RequestBody final TableRequest tableRequest) {
        final TableResponse created = tableService.create(tableRequest);
        final URI uri = URI.create(BASE_URL + "/" + created.getId());

        return ResponseEntity.created(uri)
                .body(created);
    }

    @GetMapping(BASE_URL)
    public ResponseEntity<List<TableResponse>> list() {
        return ResponseEntity.ok()
                .body(tableService.list());
    }

    @PutMapping(BASE_URL + "/{orderTableId}/empty")
    public ResponseEntity<TableResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final ChangeTableEmptyRequest changeTableEmptyRequest
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeEmpty(orderTableId, changeTableEmptyRequest));
    }

    @PutMapping(BASE_URL + "/{orderTableId}/number-of-guests")
    public ResponseEntity<TableResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final ChangeTableGuestRequest changeTableGuestRequest
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(orderTableId, changeTableGuestRequest));
    }
}
