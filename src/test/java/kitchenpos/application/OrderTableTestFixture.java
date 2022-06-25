package kitchenpos.application;

import java.util.Arrays;
import java.util.Collections;
import javax.transaction.Transactional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderTableTestFixture {

    private final MenuTestFixture menuServiceTestSupport;
    private final OrderRepository orderRepository;

    public OrderTableTestFixture(MenuTestFixture menuServiceTestSupport,
        OrderRepository orderRepository) {
        this.menuServiceTestSupport = menuServiceTestSupport;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderTable 후라이드_양념_세트_주문하기(OrderTable orderTable) {
        Menu 후라이드_양념_세트 = menuServiceTestSupport.후라이드_양념_세트_가져오기();
        OrderLineItem 후라이드_양념_세트_주문 = new OrderLineItem(후라이드_양념_세트.getId(), 1);

        Order 주문 = new Order(orderTable.getId(), Collections.singletonList(후라이드_양념_세트_주문));

        orderRepository.save(주문);
        return orderTable;
    }

}
