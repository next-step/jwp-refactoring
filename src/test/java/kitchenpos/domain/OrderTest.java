package kitchenpos.domain;

import kitchenpos.order.domain.type.OrderStatus;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.product.domain.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class OrderTest {

    private MenuGroup 치킨;
    private Menu 후치콜세트;
    private OrderTable 주문테이블;

    @BeforeEach
    void setUp() {
        치킨 = new MenuGroup("치킨");

        후치콜세트 = new Menu("후치콜세트", new Price(BigDecimal.valueOf(5_000)), 치킨.getId());

        주문테이블 = new OrderTable(1L, null, 0, false);
    }


    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    @DisplayName("주문상태가 COOKING, MEAL 이면 주문을 생성할수 없다")
    void orderStatusCheckValidCookingAndMeal(OrderStatus status) {
        Order 주문 = new Order(1L, 주문테이블.getId(), status, null);

        assertThatThrownBy(주문::validUngroupable)
                .isInstanceOf(IllegalArgumentException.class);

    }
}
