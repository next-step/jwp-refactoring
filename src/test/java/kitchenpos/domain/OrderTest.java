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
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private Product 후라이드치킨;
    private Product 제로콜라;
    private MenuGroup 치킨;
    private MenuProduct 후라이드_이인분;
    private MenuProduct 제로콜라_삼인분;
    private Menu 후치콜세트;
    private OrderTable 주문테이블;

    @BeforeEach
    void setUp() {
        후라이드치킨 = Product.of(BigDecimal.valueOf(3_000), "후라이드치킨");
        제로콜라 = Product.of(BigDecimal.valueOf(2_000), "제로콜라");

        치킨 = MenuGroup.from("치킨");

        후치콜세트 = Menu.of("후치콜세트", Price.from(BigDecimal.valueOf(5_000)), 치킨, Arrays.asList(제로콜라_삼인분, 후라이드_이인분));

        후라이드_이인분 = MenuProduct.of(후치콜세트, 후라이드치킨, 2);
        제로콜라_삼인분 = MenuProduct.of(후치콜세트, 제로콜라, 2);

        주문테이블 = new OrderTable(1L, null, 0, false);
    }


    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    @DisplayName("주문상태가 COOKING, MEAL 이면 주문을 생성할수 없다")
    void orderStatusCheckValidCookingAndMeal(OrderStatus status) {
        Order 주문 = new Order(1L, 주문테이블, status, null);
        주문.validCheckOrderStatusIsCookingAndMeal();

        assertThatThrownBy(
                주문::validCheckOrderStatusIsCookingAndMeal)
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
