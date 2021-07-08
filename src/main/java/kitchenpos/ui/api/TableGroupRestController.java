package kitchenpos.ui.api;

import kitchenpos.application.TableGroupService;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.order.OrderTableGroupRequest;
import kitchenpos.ui.dto.order.OrderTableGroupResponse;
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

    @PostMapping("/api/table-groups2")
    public ResponseEntity<OrderTableGroupResponse> create2(@RequestBody final OrderTableGroupRequest request) {
        final TableGroup created = tableGroupService.create(request.toTableGroup());
        final URI uri = URI.create("/api/table-groups2/" + created.getId());
        return ResponseEntity.created(uri)
                .body(OrderTableGroupResponse.of(created))
                ;
    }
}
