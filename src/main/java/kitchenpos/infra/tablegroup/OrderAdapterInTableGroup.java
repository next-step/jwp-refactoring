package kitchenpos.infra.tablegroup;

import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.tablegroup.SafeOrderInTableGroup;
import kitchenpos.domain.tablegroup.exceptions.InvalidTableUngroupTryException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class OrderAdapterInTableGroup implements SafeOrderInTableGroup {
    private final OrderRepository orderRepository;

    public OrderAdapterInTableGroup(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void canUngroup(final List<Long> orderTableIds) {
        if(orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new InvalidTableUngroupTryException("조리 중이거나 식사 중인 단체 지정을 해제할 수 없습니다.");
        }
    }
}
