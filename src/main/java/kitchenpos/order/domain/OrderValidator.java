package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderValidator {

    private final OrderTableRepository orderTableRepository;

    public OrderValidator(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(Order order) {
        OrderTable orderTable = findOrderTable(order.getOrderTableId());
        if (orderTable.isEmptyTable()) {
            throw new IllegalArgumentException("[ERROR] 빈테이블인 경우 주문을 등록 할 수 없습니다.");
        }
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 테이블이 등록되어있지 않습니다."));
    }

}
