package order.application;

import order.domain.OrderStatus;
import order.repository.OrderRepository;
import table.application.TableGroupValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class TableGroupValidatorImpl implements TableGroupValidator {

    private final OrderRepository orderRepository;

    public TableGroupValidatorImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateUngroup(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("조리중이거나 식사중인 테이블이 있습니다.");
        }
    }
}
