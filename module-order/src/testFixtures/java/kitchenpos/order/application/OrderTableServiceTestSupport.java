package kitchenpos.order.application;

import kitchenpos.menu.domain.MenuServiceTestSupport;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
public class OrderTableServiceTestSupport {

    private final MenuServiceTestSupport menuServiceTestSupport;
    private final OrderRepository orderRepository;

    public OrderTableServiceTestSupport(MenuServiceTestSupport menuServiceTestSupport,
                                        OrderRepository orderRepository) {
        this.menuServiceTestSupport = menuServiceTestSupport;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void 강정치킨_주문하기(Long orderTableId) {
        Menu 강정치킨 = menuServiceTestSupport.신메뉴_강정치킨_가져오기();
        Order 주문 = new Order(orderTableId);
        주문.addOrderLineItems(Arrays.asList(new OrderLineItem(강정치킨.toOrderedMenu(), 1L)));
        orderRepository.save(주문);
    }
}
