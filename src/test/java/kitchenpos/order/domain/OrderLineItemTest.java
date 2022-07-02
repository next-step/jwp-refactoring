package kitchenpos.order.domain;

import static kitchenpos.fixture.MenuFixture.메뉴_생성;
import static kitchenpos.fixture.MenuFixture.메뉴그룹_생성;
import static kitchenpos.fixture.MenuFixture.메뉴상품_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemTest {

    private Product product;
    private MenuGroup menuGroup;
    private Menu menu;

    @BeforeEach
    void setUp() {
        //given
        product = ProductFixture.상품_생성(1L, "봉골레파스타", 13000);
        menuGroup = 메뉴그룹_생성(1L, "파스타메뉴");
        menu = 메뉴_생성("봉골레파스타세트", product.getPrice(), menuGroup, Arrays.asList(메뉴상품_생성(1L, product, 1L)));
    }

    @DisplayName("주문항목을 생성한다.")
    @Test
    void craete() {
        //when
        OrderLineItem orderLineItem = new OrderLineItem(menu, 1L);

        //then
        assertThat(orderLineItem).isNotNull();
        assertThat(orderLineItem.getMenu().getName()).isEqualTo(menu.getName());
        assertThat(orderLineItem.getQuantity()).isEqualTo(1L);
    }

    @DisplayName("수량이 0 미만인 경우, 주문항목 생성에 실패한다.")
    @Test
    void create_invalidQuantity() {
        //when & then
        assertThatThrownBy(() -> new OrderLineItem(menu, -1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("수량 0 이상이어야 합니다.");
    }
}
