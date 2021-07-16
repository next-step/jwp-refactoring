package kitchenpos.table.application;

import org.springframework.stereotype.Component;

import kitchenpos.order.application.OrderOrderTableValidator;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.exception.NonEmptyOrderTableNotFoundException;

@Component
public class OrderTableValidator implements OrderOrderTableValidator {
    private final OrderTableRepository orderTableRepository;

    public OrderTableValidator(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public void validateExistsOrderTableByIdAndEmptyIsFalse(Long orderTableId) {
        this.orderTableRepository.findByIdAndEmptyIsFalse(orderTableId)
                .orElseThrow(() -> new NonEmptyOrderTableNotFoundException("비어있지 않은 테이블 대상이 존재하지 않습니다. 입력 ID : " + orderTableId));
    }
}
