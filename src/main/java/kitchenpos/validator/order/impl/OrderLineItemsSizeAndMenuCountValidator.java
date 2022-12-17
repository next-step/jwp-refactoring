package kitchenpos.validator.order.impl;

import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.validator.order.OrderValidator;
import org.springframework.stereotype.Component;

@Component
@org.springframework.core.annotation.Order(2)
public class OrderLineItemsSizeAndMenuCountValidator implements OrderValidator {

    private final MenuRepository menuRepository;

    public OrderLineItemsSizeAndMenuCountValidator(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public void validate(Order order) {
        long menuCount = menuRepository.countByIdIn(order.makeMenuIds());
        if (order.getOrderLineItems().size() != menuCount) {
            throw new IllegalArgumentException(
                    "주문 등록시, 등록 된 메뉴만 지정 가능합니다[orderLineItemsSize:" + order.getOrderLineItems().size() +
                            "/menuCount:" + menuCount + "]");
        }
    }
}
