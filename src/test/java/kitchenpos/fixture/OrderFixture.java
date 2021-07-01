package kitchenpos.fixture;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.Arrays;

import static kitchenpos.fixture.OrderTableFixture.사용중인_1명_테이블;

public class OrderFixture {
    public static Order 결제완료1;
    public static Order 결제완료2;
    public static Order 결제완료3;
    public static Order 결제완료4;

    public static Order 식사1;

    public static void cleanUp() {
        결제완료1 = new Order(1L, 사용중인_1명_테이블, OrderStatus.COMPLETION, LocalDateTime.now(), Arrays.asList());
        결제완료2 = new Order(2L, 사용중인_1명_테이블, OrderStatus.COMPLETION, LocalDateTime.now(), Arrays.asList());
        결제완료3 = new Order(3L, 사용중인_1명_테이블, OrderStatus.COMPLETION, LocalDateTime.now(), Arrays.asList());
        결제완료4 = new Order(4L, 사용중인_1명_테이블, OrderStatus.COMPLETION, LocalDateTime.now(), Arrays.asList());

        식사1 = new Order(1L, 사용중인_1명_테이블, OrderStatus.MEAL, LocalDateTime.now(), Arrays.asList());
    }
}
