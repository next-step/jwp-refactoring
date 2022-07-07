package kitchenpos.ui.table;

import java.net.URI;
import kitchenpos.application.table.TableGroupService;
import kitchenpos.dto.table.CreateTableGroupRequest;
import kitchenpos.dto.table.TableGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TableGroupRestController {

    private final TableGroupService tableGroupService;

    public TableGroupRestController(final TableGroupService tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    @PostMapping("/api/table-groups")
    public ResponseEntity<TableGroupResponse> create(@RequestBody final CreateTableGroupRequest createTableGroupRequest) {
        final TableGroupResponse created = tableGroupService.create(createTableGroupRequest);
        final URI uri = URI.create("/api/table-groups/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @DeleteMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<TableGroupResponse> getAll(@PathVariable("tableGroupId") Long id) {
        return ResponseEntity.ok().body(tableGroupService.findById(id));
    }
}
