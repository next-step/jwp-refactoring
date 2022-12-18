package kitchenpos.domain;

import kitchenpos.domain.type.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class OrderTest {

    private MenuGroup 치킨;
    private Menu 후치콜세트;
    private OrderTable 주문테이블;

    @BeforeEach
    void setUp() {
        치킨 = new MenuGroup("치킨");

        후치콜세트 = new Menu("후치콜세트", new Price(BigDecimal.valueOf(5_000)), 치킨);

        주문테이블 = new OrderTable(1L, null, 0, false);
    }


    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    @DisplayName("주문상태가 COOKING, MEAL 이면 주문을 생성할수 없다")
    void orderStatusCheckValidCookingAndMeal(OrderStatus status) {
        Order 주문 = new Order(1L, 주문테이블, status, null);

        assertThatThrownBy(주문::validCheckOrderStatusIsCookingAndMeal)
                .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    @DisplayName("주문항목의 사이즈와 메뉴의 사이즈는 같아야한다.")
    void orderStatusCheckValidCookingAndMeal() {
        Order 주문 = new Order(1L, 주문테이블, OrderStatus.COMPLETION, null);
        OrderLineItem 주문_항목A = new OrderLineItem(1L, 주문, 후치콜세트, 1L);
        OrderLineItem 주문_항목B = new OrderLineItem(1L, 주문, 후치콜세트, 1L);


        assertThatThrownBy(() ->
                주문.addOrderLineItems(Arrays.asList(주문_항목A, 주문_항목B), Arrays.asList(후치콜세트))
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
