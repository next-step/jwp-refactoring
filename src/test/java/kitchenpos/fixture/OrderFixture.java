package kitchenpos.fixture;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;

import java.time.LocalDateTime;
import java.util.Arrays;

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

    public static void cleanUp() {
        결제완료1 = new Order(1L, 사용중인_1명_테이블, OrderStatus.COMPLETION, LocalDateTime.now(), Arrays.asList());
        결제완료2 = new Order(2L, 사용중인_1명_테이블, OrderStatus.COMPLETION, LocalDateTime.now(), Arrays.asList());
        결제완료3 = new Order(3L, 사용중인_1명_테이블, OrderStatus.COMPLETION, LocalDateTime.now(), Arrays.asList());
        결제완료4 = new Order(4L, 사용중인_1명_테이블, OrderStatus.COMPLETION, LocalDateTime.now(), Arrays.asList());

        식사1 = new Order(5L, 사용중인_1명_테이블, OrderStatus.MEAL, LocalDateTime.now(), Arrays.asList());

        조리1 = new Order(6L, 사용중인_1명_테이블, OrderStatus.COOKING, LocalDateTime.now(), Arrays.asList());

        결제완료_음식_1 = new Order(7L, 사용중인_1명_테이블, OrderStatus.COMPLETION, LocalDateTime.now(),
                Arrays.asList(주문_연결_안된_양념치킨_콜라_1000원_1개));
        결제완료_음식_2 = new Order(8L, 사용중인_1명_테이블, OrderStatus.COMPLETION, LocalDateTime.now(),
                Arrays.asList(주문_연결_안된_양념치킨_콜라_1000원_2개,
                주문_연결_안된_후라이드치킨_콜라_2000원_1개)
        );

        식사_음식_1 = new Order(9L, 사용중인_1명_테이블, OrderStatus.COOKING, LocalDateTime.now(),
                Arrays.asList(주문_연결_안된_후라이드치킨_콜라_2000원_2개));
    }
}
