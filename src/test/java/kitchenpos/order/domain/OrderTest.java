package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTest {

    private Product 하와이안피자;
    private MenuGroup 피자;
    private MenuProduct 하와이안피자상품;
    private Menu 하와이안피자세트;
    private OrderTable 주문테이블A;
    private OrderTable 주문테이블B;
    private OrderLineItemRequest 하와이안피자세트주문;

    @BeforeEach
    void setUp() {
        하와이안피자 = new Product("하와이안피자", BigDecimal.valueOf(15_000));
        피자 = new MenuGroup("피자");
        하와이안피자상품 = new MenuProduct(하와이안피자, 1);
        하와이안피자세트 = new Menu(1L, "하와이안피자세트", BigDecimal.valueOf(15_000L), 피자,
            MenuProducts.from(Arrays.asList(하와이안피자상품)));
        주문테이블A = new OrderTable(1L, null, 4, true);
        주문테이블B = new OrderTable(1L, null, 4, false);
        하와이안피자세트주문 = OrderLineItemRequest.from(하와이안피자세트.getId(), 1);
    }

    @DisplayName("주문 테이블이 빈 값이면 에러가 발생한다.")
    @Test
    void validateOrderTableNotEmptyException() {
        assertThatThrownBy(() -> Order.of(주문테이블A,
            OrderLineItems.from(Arrays.asList(하와이안피자세트주문.toOrderLineItem(하와이안피자세트)))))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        Order order = Order.of(주문테이블B,
            OrderLineItems.from(Arrays.asList(하와이안피자세트주문.toOrderLineItem(하와이안피자세트))));

        order.changeOrderStatus(OrderStatus.COMPLETION);

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    @DisplayName("완료된 주문은 상태 변경 시 오류가 발생한다.")
    @Test
    void validateOrderStatusCompleteException() {
        Order order = Order.of(주문테이블B,
            OrderLineItems.from(Arrays.asList(하와이안피자세트주문.toOrderLineItem(하와이안피자세트))));
        order.changeOrderStatus(OrderStatus.COMPLETION);

        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
            .isInstanceOf(IllegalArgumentException.class);
    }
}