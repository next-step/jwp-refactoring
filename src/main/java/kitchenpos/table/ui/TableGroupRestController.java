package kitchenpos.table.ui;

import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.domain.request.TableGroupRequest;
import kitchenpos.table.domain.response.TableGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/table-groups")
public class TableGroupRestController {

    private final TableGroupService tableGroupService;

    public TableGroupRestController(final TableGroupService tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    @PostMapping
    public ResponseEntity<TableGroup> create(@RequestBody final TableGroup tableGroup) {
        final TableGroup created = tableGroupService.create(tableGroup);
        final URI uri = URI.create("/api/table-groups/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @PostMapping("/copy")
    public ResponseEntity<TableGroupResponse> createCopy(@RequestBody final TableGroupRequest request) {
        final TableGroupResponse created = tableGroupService.createCopy(request);
        final URI uri = URI.create("/api/table-groups/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @DeleteMapping("/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{tableGroupId}/copy")
    public ResponseEntity<Void> ungroupCopy(@PathVariable final Long tableGroupId) {
        tableGroupService.ungroupCopy(tableGroupId);
        return ResponseEntity.noContent().build();
    }
}
