package kitchenpos.table.ui;

import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class TableGroupRestController {
    private final TableGroupService tableGroupService;

    public TableGroupRestController(final TableGroupService tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    @PostMapping(value = "/api/table-groups", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TableGroupResponse> group(@RequestBody final TableGroupRequest tableGroupRequest) {
        final TableGroupResponse created = tableGroupService.group(tableGroupRequest);
        final URI uri = URI.create("/api/table-groups/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created);
    }

    @DeleteMapping(value = "/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent()
                .build();
    }
}
