package kitchenpos.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문항목들 도메인 테스트")
public class OrderLineItemsTest {

    private Product 상품;
    private OrderLineItem 주문상품;
    private MenuGroup 메뉴그룹;
    private MenuProduct 메뉴상품;
    private Menu 메뉴;

    @BeforeEach
    void setup() {
        상품 = Product.of("상품", BigDecimal.valueOf(17_000));
        메뉴그룹 = MenuGroup.of("추천메뉴");
        메뉴상품 = MenuProduct.of(null, 상품, 2L);
        메뉴 = Menu.of("더블 후라이드", BigDecimal.valueOf(30_000), 메뉴그룹);
        메뉴.addMenuProducts(Collections.singletonList(메뉴상품));
        주문상품 = OrderLineItem.of(메뉴, 1L);
    }

    @DisplayName("생성 테스트")
    @Test
    void createTest() {
        assertThat(OrderLineItems.from(Collections.singletonList(주문상품)))
                .isEqualTo(OrderLineItems.from(Collections.singletonList(주문상품)));
    }

    @DisplayName("추가할 수 있다")
    @Test
    void addTest() {
        final OrderLineItems 주문항목들 = OrderLineItems.empty();
        주문항목들.add(주문상품);

        assertThat(주문항목들.toList()).containsAll(Collections.singletonList(주문상품));
    }
}
