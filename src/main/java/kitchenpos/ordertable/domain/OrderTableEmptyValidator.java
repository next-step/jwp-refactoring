package kitchenpos.ordertable.domain;

import kitchenpos.order.domain.PlaceOrderValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderTableEmptyValidator implements PlaceOrderValidator {
    private static final String NOT_EXIST_TABLE = "존재하지 않는 테이블입니다.";
    private static final String EMPTY_TABLE = "이용중이지 않은 테이블에서는 주문 할 수 없습니다.";

    private final OrderTableRepository orderTableRepository;

    public OrderTableEmptyValidator(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public void validateTableEmpty(Long tableId) {
        OrderTable table = orderTableRepository.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_TABLE));

        if (table.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_TABLE);
        }
    }
}
