package kitchenpos.table.ui;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.dto.TableGroupRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/table-groups")
public class TableGroupController {
    private final TableGroupService tableGroupService;

    public TableGroupController(final TableGroupService tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    @PostMapping
    public ResponseEntity<TableGroupResponse> create(@RequestBody @Valid final TableGroupRequest tableGroupRequest) {
        final TableGroupResponse created = tableGroupService.create(tableGroupRequest);
        final URI uri = URI.create("/api/table-groups/" + created.getId());
        return ResponseEntity
                .created(uri)
                .body(created);
    }

    @DeleteMapping("/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable @Positive final Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);
        return ResponseEntity
                .noContent()
                .build();
    }
}
