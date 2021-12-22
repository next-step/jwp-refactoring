package kitchenpos.order.testfixtures;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemDao;

public class OrderTestFixtures {

    public static void 특정_테이블이_특정_상태인지_조회_모킹(OrderDao orderDao, boolean isExist) {
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), anyList()))
            .willReturn(isExist);
    }

    public static void 특정_테이블들이_특정상태인지_조회_모킹(OrderDao orderDao, boolean isExist) {
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), anyList()))
            .willReturn(isExist);
    }

    public static void 주문_저장_결과_모킹(OrderDao orderDao, Order order) {
        given(orderDao.save(any()))
            .willReturn(order);
    }

    public static void 주문항목리스트_저장_결과_모킹(OrderLineItemDao orderLineItemDao,
        List<OrderLineItem> orderLineItems) {
        orderLineItems.stream()
            .forEach(orderLineItem -> given(orderLineItemDao.save(orderLineItem))
                .willReturn(orderLineItem));
    }

    public static void 주문_전체_조회_모킹(OrderDao orderDao, List<Order> orders) {
        given(orderDao.findAll())
            .willReturn(orders);
    }

    public static void 특정_주문에_해당하는_주문항목_조회_모킹(OrderLineItemDao orderLineItemDao,
        List<Order> orders) {
        orders.stream().forEach(order -> given(orderLineItemDao.findAllByOrderId(order.getId()))
            .willReturn(order.getOrderLineItems()));
    }

    public static void 특정_주문_조회_모킹(OrderDao orderDao, Order order) {
        given(orderDao.findById(any()))
            .willReturn(Optional.of(order));
    }
}
