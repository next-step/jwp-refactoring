package kitchenpos.tablegroup.ui;

import kitchenpos.ordertable.exception.KitchenPosArgumentException;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class TableGroupRestController {
    private final TableGroupService tableGroupService;
    private final TableService tableService;

    public TableGroupRestController(final TableGroupService tableGroupService, TableService tableService) {
        this.tableGroupService = tableGroupService;
        this.tableService = tableService;
    }

    @PostMapping("/api/table-groups")
    public ResponseEntity<TableGroupResponse> create(@RequestBody final TableGroupRequest tableGroup) {
        final TableGroupResponse created = tableGroupService.create(tableGroup);
        final URI uri = URI.create("/api/table-groups/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @DeleteMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        tableService.ungroupTableByTableGroupId(tableGroupId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(KitchenPosArgumentException.class)
    public ResponseEntity handleKitchenPosArgException(KitchenPosArgumentException e) {
        return ResponseEntity.badRequest().build();
    }
}
