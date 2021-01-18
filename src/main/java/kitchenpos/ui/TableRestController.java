package kitchenpos.ui;

import kitchenpos.application.TableService;
import kitchenpos.dto.TableRequest;
import kitchenpos.dto.TableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class TableRestController {
    private final TableService tableService;

    public TableRestController(TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<TableResponse> create() {
        TableResponse created = tableService.create();
        URI uri = URI.create("/api/tables/" + created.getId());
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

    @PutMapping("/api/tables/{orderTableId}")
    public ResponseEntity<TableResponse> change(
            @PathVariable Long orderTableId,
            @Valid @RequestBody TableRequest request
    ) {
        return ResponseEntity.ok()
                .body(tableService.update(orderTableId, request))
                ;
    }
}
