package kitchenpos.ui;

import kitchenpos.application.table.TableGroupService;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.dto.table.TableGroupRequest;
import kitchenpos.dto.table.TableGroupResponse;
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
    public ResponseEntity<TableGroupResponse> create(@RequestBody final TableGroupRequest tableGroupRequest) {
        final TableGroup created = tableGroupService.create(tableGroupRequest);
        final URI uri = URI.create("/api/table-groups/" + created.getId());
        return ResponseEntity.created(uri)
                .body(TableGroupResponse.from(created))
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
