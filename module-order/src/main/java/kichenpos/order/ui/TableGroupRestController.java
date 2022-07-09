package kichenpos.order.ui;

import kichenpos.order.application.TableGroupService;
import kichenpos.order.ui.dto.TableGroupCreateRequest;
import kichenpos.order.ui.dto.TableGroupCreateResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class TableGroupRestController {
    private final TableGroupService tableGroupService;

    public TableGroupRestController(final TableGroupService tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    @PostMapping("/api/table-groups")
    public ResponseEntity<TableGroupCreateResponse> create(@RequestBody final TableGroupCreateRequest request) {
        final TableGroupCreateResponse created = new TableGroupCreateResponse(tableGroupService.create(request.toEntity()));
        final URI uri = URI.create("/api/table-groups/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @DeleteMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent()
                .build()
                ;
    }
}
