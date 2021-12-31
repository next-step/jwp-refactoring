package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.dto.OrderRequest;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    private final OrderTableRepository orderTableRepository;

    public OrderValidator(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(OrderRequest orderRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 빈 테이블인 경우 주문할 수 없습니다.");
        }
    }
}
