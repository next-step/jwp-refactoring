package kitchenpos.ui.table;

import kitchenpos.dto.table.TableRequest;
import kitchenpos.dto.table.TableResponse;
import kitchenpos.service.table.TableService;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TableRestController {
    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/tables")
    public ResponseEntity<TableResponse> create(@RequestBody final TableRequest tableRequest) {
        final TableResponse created = tableService.save(tableRequest);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping("/tables")
    public ResponseEntity<List<TableResponse>> list() {
        return ResponseEntity.ok().body(tableService.findAll());
    }

    @PutMapping("/tables/{orderTableId}/empty")
    public ResponseEntity<TableResponse> changeEmpty(@PathVariable final Long orderTableId, @RequestBody final TableRequest tableRequest) {
        return ResponseEntity.ok().body(tableService.changeEmpty(orderTableId, tableRequest));
    }

    @PutMapping("/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<TableResponse> changeNumberOfGuests(@PathVariable final Long orderTableId, @RequestBody final TableRequest tableRequest) {
        return ResponseEntity.ok().body(tableService.changeGuestNumber(orderTableId, tableRequest));
    }
}
