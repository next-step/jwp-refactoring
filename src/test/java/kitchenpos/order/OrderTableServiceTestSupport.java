package kitchenpos.order;


import kitchenpos.menu.MenuServiceTestSupport;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
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
    public OrderTable 강정치킨_주문하기(OrderTable orderTable) {
        Menu 강정치킨 = menuServiceTestSupport.신메뉴_강정치킨_가져오기();
        Order 주문 = new Order(orderTable.getId());
        주문.addOrderLineItems(Arrays.asList(new OrderLineItem(강정치킨.getId(), 1L)));
        orderRepository.save(주문);
        return orderTable;
    }
}
