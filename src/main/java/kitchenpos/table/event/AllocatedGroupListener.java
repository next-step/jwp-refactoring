package kitchenpos.table.event;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@EnableAsync
@Component
public class AllocatedGroupListener implements ApplicationListener<AllocatedGroupEvent> {

    private final OrderTableRepository orderTableRepository;

    public AllocatedGroupListener(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    @Async
    @Override
    public void onApplicationEvent(AllocatedGroupEvent event) {
        List<OrderTable> orderTableList = orderTableRepository.findAllByIdIn(event.getOrderTableIds());
        orderTableList.forEach(it -> it.updateGroup(event.getTableGroupId()));
        orderTableRepository.saveAll(orderTableList);
    }
}
