package kitchenpos.domain.order;

import java.util.List;

public interface SafeMenu {
    void isMenuExists(List<OrderLineItem> orderLineItems);
}
