package kitchenpos.domain;

import kitchenpos.domin.Order;
import kitchenpos.domin.OrderLineItem;
import kitchenpos.domin.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static kitchenpos.domain.MenuFixture.메뉴;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Order 클래스 테스트")
public class OrderTest {

    private OrderTable 테이블1;
    private OrderTable 빈테이블;

    private Order 주문1;
    private Order 주문2;

    private Menu 풀코스;
    private Menu 오일2인세트;

    private OrderLineItem 풀코스_주문;
    private OrderLineItem 오일2인세트_주문;

    @BeforeEach
    void setup() {
        테이블1 = OrderTableFixture.주문테이블(1L, null, 6, false);
        빈테이블 = OrderTableFixture.빈주문테이블(3L);

        주문1 = OrderFixture.주문(1L, OrderStatus.COOKING.name(), 테이블1.getId());
        주문2 = OrderFixture.주문(1L, OrderStatus.COMPLETION.name(), 테이블1.getId());

        오일2인세트 = 메뉴(1L, "오일2인세트", 34000, null);
        풀코스 = 메뉴(2L, "풀코스", 62000, null);

        풀코스_주문 = OrderLineItemFixture.주문라인아이템(1L, 주문1, 풀코스.getId(), 1);
        오일2인세트_주문 = OrderLineItemFixture.주문라인아이템(2L, 주문1, 오일2인세트.getId(), 1);
    }

    @DisplayName("Order 를 생성한다")
    @Test
    void Order_생성_테스트() {
        // when
        Order order = new Order(OrderStatus.COOKING.name(), LocalDateTime.now(), 테이블1.getId());

        // then
        assertThat(order).isNotNull();
    }

    @DisplayName("Order 상태를 변경한다")
    @Test
    void Order_상태변경_테스트() {
        // when
        주문1.updateOrderStatus(OrderStatus.COMPLETION.name());

        // then
        assertThat(주문1.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @DisplayName("완료된 Order 상태를 변경한다")
    @Test
    void 완료된_Order_상태변경_테스트() {
        // when & then
        assertThatThrownBy(
                () -> 주문2.updateOrderStatus(OrderStatus.COMPLETION.name())
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
