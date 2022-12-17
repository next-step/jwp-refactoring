package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.testfixture.MenuProductTestFixture;
import kitchenpos.menu.testfixture.MenuTestFixture;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupTestFixture;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.testfixture.OrderMenuTestFixture;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.testfixture.OrderTableTestFixture;
import kitchenpos.product.domain.Product;
import kitchenpos.product.testfixture.ProductTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    private Product 하와이안피자;
    private MenuGroup 피자;
    private MenuProduct 하와이안피자상품;
    private Menu 하와이안피자세트;
    private OrderTable 주문테이블A;
    private OrderTable 주문테이블B;
    private OrderLineItemRequest 하와이안피자세트주문;
    private OrderMenu 주문메뉴;

    @BeforeEach
    void setUp() {
        하와이안피자 = ProductTestFixture.create("하와이안피자", BigDecimal.valueOf(15_000));
        피자 = MenuGroupTestFixture.create("피자");
        하와이안피자상품 = MenuProductTestFixture.create(하와이안피자, 1);
        하와이안피자세트 = MenuTestFixture.create(1L, "하와이안피자세트", BigDecimal.valueOf(15_000L), 피자,
            Arrays.asList(하와이안피자상품));
        주문메뉴 = OrderMenuTestFixture.create(하와이안피자세트);
        주문테이블A = OrderTableTestFixture.create(1L, 4, true);
        주문테이블B = OrderTableTestFixture.create(1L, 4, false);
        하와이안피자세트주문 = OrderLineItemRequest.from(하와이안피자세트.getId(), 1);
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        Order order = Order.of(주문테이블B.getId(),
            OrderLineItems.from(Arrays.asList(하와이안피자세트주문.toOrderLineItem(주문메뉴))));

        order.changeOrderStatus(OrderStatus.COMPLETION);

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    @DisplayName("완료된 주문은 상태 변경 시 오류가 발생한다.")
    @Test
    void validateOrderStatusCompleteException() {
        Order order = Order.of(주문테이블B.getId(),
            OrderLineItems.from(Arrays.asList(하와이안피자세트주문.toOrderLineItem(주문메뉴))));
        order.changeOrderStatus(OrderStatus.COMPLETION);

        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
            .isInstanceOf(IllegalArgumentException.class);
    }
}