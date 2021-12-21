package kitchenpos.ordertablegroup.ui;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.ordertablegroup.application.OrderTableGroupService;
import kitchenpos.ordertablegroup.dto.OrderTableGroupCreateRequest;
import kitchenpos.ordertablegroup.dto.OrderTableGroupDto;

@RestController(value = "ToBeOrderTableGroupRestController")
public class OrderTableGroupRestController {
    private final OrderTableGroupService orderTableGroupService;

    public OrderTableGroupRestController(OrderTableGroupService orderTableGroupService) {
        this.orderTableGroupService = orderTableGroupService;
    }

    @PostMapping("/api/table-groups")
    public ResponseEntity<OrderTableGroupDto> create(@RequestBody OrderTableGroupCreateRequest request) {
        OrderTableGroupDto orderTableGroup = orderTableGroupService.create(request);
        final URI uri = URI.create("/api/table-groups/" + orderTableGroup.getId());
        return ResponseEntity.created(uri).body(orderTableGroup);
    }

    @DeleteMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable Long tableGroupId) {
        orderTableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent().build();
    }
}
