package kitchenpos.order.ui;

import kitchenpos.order.application.OrderTableGroupService;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class OrderTableGroupRestController {

    private final OrderTableGroupService orderTableGroupService;

    public OrderTableGroupRestController(final OrderTableGroupService orderTableGroupService) {
        this.orderTableGroupService = orderTableGroupService;
    }

    @PostMapping("/api/table-groups")
    public ResponseEntity<TableGroupResponse> create(@RequestBody final TableGroupRequest request) {
        final TableGroupResponse created = orderTableGroupService.create(request);
        final URI uri = URI.create("/api/table-groups/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @DeleteMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        orderTableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent().build();
    }
}
