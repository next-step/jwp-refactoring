package api.order.ui;

import api.order.application.OrderTableGroupService;
import api.order.dto.OrderTableGroupRequest;
import api.order.dto.OrderTableGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class TableGroupRestController {
    private final OrderTableGroupService tableGroupService;

    public TableGroupRestController(final OrderTableGroupService tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    @DeleteMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent()
                .build()
                ;
    }

    @PostMapping("/api/table-groups")
    public ResponseEntity<OrderTableGroupResponse> create(@RequestBody final OrderTableGroupRequest request) {
        final OrderTableGroupResponse created = tableGroupService.create(request);
        final URI uri = URI.create("/api/table-groups/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }
}
