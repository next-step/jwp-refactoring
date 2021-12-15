package kitchenpos.table.ui;

import java.net.URI;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.order.ui.request.TableGroupRequest;
import kitchenpos.order.ui.response.TableGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/table-groups")
public class TableGroupRestController {

    private final TableGroupService tableGroupService;

    public TableGroupRestController(TableGroupService tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    @PostMapping
    public ResponseEntity<TableGroupResponse> create(@RequestBody TableGroupRequest request) {
        TableGroupResponse created = tableGroupService.create(request);
        return ResponseEntity.created(uri(created))
            .body(created);
    }

    @DeleteMapping("/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent()
            .build();
    }

    private URI uri(TableGroupResponse created) {
        return URI.create("/api/table-groups/" + created.getId());
    }
}
