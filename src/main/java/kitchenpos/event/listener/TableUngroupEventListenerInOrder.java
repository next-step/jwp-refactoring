package kitchenpos.event.listener;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.event.TableUngroupEventDTO;
import kitchenpos.event.customEvent.TableUngroupEvent;
import kitchenpos.exception.TableGroupException;
import kitchenpos.repository.OrderRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class TableUngroupEventListenerInOrder implements ApplicationListener<TableUngroupEvent> {

    private final OrderRepository orderRepository;

    public TableUngroupEventListenerInOrder(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void onApplicationEvent(TableUngroupEvent event) {
        TableUngroupEventDTO tableUngroupEventDTO = (TableUngroupEventDTO) event.getSource();

        checkAllMenuIsCompleteInTableGroup(tableUngroupEventDTO.getOrderTableIds());
    }

    private void checkAllMenuIsCompleteInTableGroup(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
            orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new TableGroupException("TABLE_CONTAIN_NOT_COMPLETE_ORDER");
        }
    }
}
