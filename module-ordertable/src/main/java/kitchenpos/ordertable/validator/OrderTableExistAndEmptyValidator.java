package kitchenpos.ordertable.validator;

import kitchenpos.order.validator.OrderTableStatusValidator;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderTableExistAndEmptyValidator implements OrderTableStatusValidator {

    private final OrderTableRepository orderTableRepository;

    public OrderTableExistAndEmptyValidator(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public void validate(Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "주문 등록시, 등록된 주문 테이블만 지정할 수 있습니다 [orderTableId:" + orderTableId + "]"));
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 등록시, 주문 테이블은 비어있으면 안됩니다");
        }
    }
}
