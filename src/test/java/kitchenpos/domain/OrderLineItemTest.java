package kitchenpos.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 상품 도메인 테스트")
public class OrderLineItemTest {

    private Order 주문;
    private OrderLineItem 주문상품;
    private Menu 메뉴;

    @BeforeEach
    void setup() {
        Product 상품 = Product.of("상품", BigDecimal.valueOf(17_000));
        MenuGroup 메뉴그룹 = MenuGroup.of("메뉴그룹");
        MenuProduct 메뉴상품 = MenuProduct.of(null, 상품, 2L);

        메뉴 = Menu.of("더블 후라이드", BigDecimal.valueOf(30_000), 메뉴그룹);
        메뉴.addMenuProducts(Collections.singletonList(메뉴상품));

        OrderTable 테이블 = OrderTable.of(2, false);

        주문상품 = OrderLineItem.of(메뉴, 1L);
        주문 = Order.of(테이블, OrderStatus.COOKING);
    }

    @DisplayName("생성 테스트")
    @Test
    void createTest() {
        assertThat(OrderLineItem.of(메뉴, 1L))
                .isEqualTo(OrderLineItem.of(메뉴, 1L));
    }

    @DisplayName("주문을 설정할 수 있다")
    @Test
    void setOrderTest() {

        주문상품.decideOrder(주문);
        // then
        assertThat(주문상품.getOrder()).isEqualTo(주문);
    }

}