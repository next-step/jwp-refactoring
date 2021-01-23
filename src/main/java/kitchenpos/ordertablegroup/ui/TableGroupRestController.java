package kitchenpos.ordertablegroup.ui;

import kitchenpos.ordertablegroup.application.OrderTableGroupService;
import kitchenpos.ordertablegroup.dto.OrderTableGroupRequest;
import kitchenpos.ordertablegroup.dto.OrderTableGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class TableGroupRestController {
    private final OrderTableGroupService orderTableGroupService;

    public TableGroupRestController(final OrderTableGroupService tableGroupService) {
        this.orderTableGroupService = tableGroupService;
    }

    @PostMapping("/api/table-groups")
    public ResponseEntity<OrderTableGroupResponse> create(@RequestBody final OrderTableGroupRequest request) {
        final OrderTableGroupResponse created = orderTableGroupService.create(request);
        final URI uri = URI.create("/api/table-groups/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @DeleteMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        orderTableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent().build();
    }
}
