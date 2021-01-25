package kitchenpos.ui.table;

import kitchenpos.dto.table.TableGroupRequest;
import kitchenpos.dto.table.TableGroupResponse;
import kitchenpos.service.table.TableGroupService;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TableGroupRestController {
    private final TableGroupService tableGroupService;

    public TableGroupRestController(final TableGroupService tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    @PostMapping("/table-groups")
    public ResponseEntity<TableGroupResponse> create(@RequestBody final TableGroupRequest tableGroupRequest) {
        final TableGroupResponse created = tableGroupService.applyGroup(tableGroupRequest);
        final URI uri = URI.create("/api/table-groups/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @DeleteMapping("/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        tableGroupService.applyUnGroup(tableGroupId);
        return ResponseEntity.noContent().build();
    }
}
