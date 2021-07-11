package kitchenpos.order.ui;

import kitchenpos.order.application.TableGroupService;
import kitchenpos.order.domain.TableGroup;
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
    public ResponseEntity<TableGroup> create(@RequestBody final TableGroup tableGroup) {
        final TableGroup created = tableGroupService.create(tableGroup);
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

    //TODO re -------

    @PostMapping("/api/table-groups_re")
    public ResponseEntity<TableGroup> create_re(@RequestBody final TableGroup tableGroup) {
        final TableGroup created = tableGroupService.create_re(tableGroup);
        final URI uri = URI.create("/api/table-groups/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @DeleteMapping("/api/table-groups_re/{tableGroupId}")
    public ResponseEntity<Void> ungroup_re(@PathVariable final Long tableGroupId) {
        tableGroupService.ungroup_re(tableGroupId);
        return ResponseEntity.noContent()
                .build()
                ;
    }
}
