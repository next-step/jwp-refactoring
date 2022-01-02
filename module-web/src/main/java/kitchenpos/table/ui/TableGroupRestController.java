package kitchenpos.table.ui;

import java.net.*;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import kitchenpos.table.application.*;
import kitchenpos.table.dto.*;

@RestController
@RequestMapping("/api/table-groups")
public class TableGroupRestController {
    private final TableGroupService tableGroupService;

    public TableGroupRestController(final TableGroupService tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    @PostMapping
    public ResponseEntity<TableGroupResponse> create(@RequestBody final TableGroupRequest tableGroupRequest) {
        final TableGroupResponse saveTableGroup = tableGroupService.saveTableGroup(tableGroupRequest);
        final URI uri = URI.create("/api/table-groups/" + saveTableGroup.getId());
        return ResponseEntity.created(uri)
                .body(saveTableGroup);
    }

    @DeleteMapping("/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent()
                .build();
    }
}
