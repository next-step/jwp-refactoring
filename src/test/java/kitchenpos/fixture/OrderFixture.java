package kitchenpos.fixture;

import kitchenpos.domain.menu.Menus;
import kitchenpos.domain.order.*;
import kitchenpos.domain.table.OrderTable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.fixture.MenuFixture.*;
import static kitchenpos.fixture.OrderLineItemFixture.*;
import static kitchenpos.fixture.OrderTableFixture.사용중인_1명_테이블;

public class OrderFixture {
    public static Order 결제완료1;
    public static Order 결제완료2;
    public static Order 결제완료3;
    public static Order 결제완료4;

    public static Order 식사1;
    public static Order 조리1;

    public static Order 결제완료_음식_1;
    public static Order 결제완료_음식_2;

    public static Order 식사_음식_1;

    public static final List<OrderLineItem> 주문한개 = Arrays.asList(주문_연결_안된_양념치킨_콜라_1000원_1개);
    public static final List<OrderLineItem> 주문두개 = Arrays.asList(주문_연결_안된_양념치킨_콜라_1000원_2개, 주문_연결_안된_후라이드치킨_콜라_2000원_1개);
    public static final Menus 주문한개_메뉴 = new Menus(Arrays.asList(양념치킨_콜라_1000원_1개));
    public static final Menus 주문두개_메뉴 = new Menus(Arrays.asList(양념치킨_콜라_1000원_2개, 후라이드치킨_콜라_2000원_1개));

    public static void cleanUp() {
        결제완료1 = createOrder(1L, 사용중인_1명_테이블, OrderStatus.COMPLETION, 주문한개, 주문한개_메뉴);
        결제완료2 = createOrder(2L, 사용중인_1명_테이블, OrderStatus.COMPLETION, 주문한개, 주문한개_메뉴);
        결제완료3 = createOrder(3L, 사용중인_1명_테이블, OrderStatus.COMPLETION, 주문한개, 주문한개_메뉴);
        결제완료4 = createOrder(4L, 사용중인_1명_테이블, OrderStatus.COMPLETION, 주문한개, 주문한개_메뉴);

        식사1 = createOrder(5L, 사용중인_1명_테이블, OrderStatus.MEAL, 주문한개, 주문한개_메뉴);

        조리1 = createOrder(6L, 사용중인_1명_테이블, OrderStatus.COOKING, 주문한개, 주문한개_메뉴);

        결제완료_음식_1 = createOrder(7L, 사용중인_1명_테이블, OrderStatus.COMPLETION, 주문한개, 주문한개_메뉴);
        결제완료_음식_2 = createOrder(8L, 사용중인_1명_테이블, OrderStatus.COMPLETION, 주문두개, 주문두개_메뉴);

        식사_음식_1 = createOrder(9L, 사용중인_1명_테이블, OrderStatus.COOKING, 주문한개, 주문한개_메뉴);
    }

    private static Order createOrder(Long id, OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> orderLineItems, Menus menus) {
        List<OrderLineItemCreate> creates = orderLineItems.stream()
                .map(item -> new OrderLineItemCreate(item.getMenuId(), item.getQuantity()))
                .collect(Collectors.toList());

        OrderCreate orderCreate = new OrderCreate(orderTable.getId(), orderStatus, creates);
        Order order = Order.createOrder(id, orderCreate, menus);
        order.updateOrderLines(orderCreate, menus);

        return order;
    }
}
