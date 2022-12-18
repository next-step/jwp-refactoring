package kitchenpos.ordertable.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.dto.TableChangeEmptyRequest;
import kitchenpos.ordertable.dto.TableChangeNumberOfGuestsRequest;
import kitchenpos.ordertable.dto.TableRequest;
import kitchenpos.ordertable.dto.TableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/tables")
@RestController
public class TableRestController {
    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping
    public ResponseEntity<TableResponse> create(@RequestBody TableRequest request) {
        final TableResponse created = tableService.create(request);
        final URI uri = URI.create(String.format("/api/tables/%d", created.getId()));
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping
    public ResponseEntity<List<TableResponse>> list() {
        return ResponseEntity.ok().body(tableService.list());
    }

    @PutMapping("/{orderTableId}/empty")
    public ResponseEntity<TableResponse> changeEmpty(
            @PathVariable Long orderTableId,
            @RequestBody TableChangeEmptyRequest request
    ) {
        return ResponseEntity.ok().body(tableService.changeEmpty(orderTableId, request.isEmpty()));
    }

    @PutMapping("/{orderTableId}/number-of-guests")
    public ResponseEntity<TableResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final TableChangeNumberOfGuestsRequest request
    ) {
        return ResponseEntity.ok().body(tableService.changeNumberOfGuests(orderTableId, request.getNumberOfGuests()));
    }
}

