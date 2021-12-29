package kitchenpos.domain;

import kitchenpos.common.exceptions.EmptyOrderTableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 도메인 테스트")
public class OrderTest {

    private OrderTable 주문테이블;
    private Order 주문;
    private OrderLineItem 주문항목;

    @BeforeEach
    void setup() {
        Product 상품 = Product.of("상품", BigDecimal.valueOf(17_000));
        MenuGroup 메뉴그룹 = MenuGroup.of("메뉴그룹");
        MenuProduct 메뉴상품 = MenuProduct.of(null, 상품, 2L);
        Menu 메뉴 = Menu.of("메뉴", BigDecimal.valueOf(30_000), 메뉴그룹);
        메뉴.addMenuProducts(Collections.singletonList(메뉴상품));

        주문테이블 = OrderTable.of(2, false);
        주문 = Order.of(주문테이블, OrderStatus.COOKING);
        주문항목 = OrderLineItem.of(메뉴, 1L);
    }

    @DisplayName("생성 테스트")
    @Test
    void createTest() {
        assertThat(Order.of(주문테이블, OrderStatus.COOKING))
                .isEqualTo(Order.of(주문테이블, OrderStatus.COOKING));
    }

    @DisplayName("생성 시, 주문 테이블 입력되어야 합니다")
    @Test
    void validateTest1() {
        assertThatThrownBy(() -> Order.of(null, OrderStatus.COOKING, null))
                .isInstanceOf(EmptyOrderTableException.class);
    }

    @DisplayName("주문 상품들을 추가할 수 있다")
    @Test
    void addOrderLineItemsTest() {
        주문.addOrderLineItems(Collections.singletonList(주문항목));
        assertThat(주문.getOrderLineItems().toList().size()).isEqualTo(1);
    }

    @DisplayName("주문 상태를 변경할 수 있다")
    @Test
    void updateStatusTest() {
        주문.updateStatus(OrderStatus.COMPLETION);
        assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }
}