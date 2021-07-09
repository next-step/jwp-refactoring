package kitchenpos.ui;

import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.TableEmptyRequest;
import kitchenpos.dto.TableNumberOfGuestsRequest;
import kitchenpos.dto.TableRequest;
import kitchenpos.dto.TableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequestMapping("/api/tables")
@RestController
public class TableRestController {
    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping
    public ResponseEntity<TableResponse> create(@RequestBody final TableRequest tableRequest) {
        final TableResponse tableResponse = tableService.create(tableRequest);
        return ResponseEntity.created(URI.create("/api/tables/" + tableResponse.getId())).body(tableResponse);
    }

    @GetMapping
    public ResponseEntity<List<TableResponse>> list() {
        return ResponseEntity.ok().body(tableService.list());
    }

    @PutMapping("/{orderTableId}/empty")
    public ResponseEntity<TableResponse> changeEmpty(
            @PathVariable final Long orderTableId, @RequestBody final TableEmptyRequest tableEmptyRequest) {
        return ResponseEntity.ok().body(tableService.changeEmpty(orderTableId, tableEmptyRequest));
    }

    @PutMapping("/{orderTableId}/number-of-guests")
    public ResponseEntity<TableResponse> changeNumberOfGuests(@PathVariable final Long orderTableId,
            @RequestBody final TableNumberOfGuestsRequest tableNumberOfGuestsRequest) {
        return ResponseEntity.ok().body(tableService.changeNumberOfGuests(orderTableId, tableNumberOfGuestsRequest));
    }
}
