package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu_group.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.fixture.MenuFixture;
import kitchenpos.menu_group.fixture.MenuGroupFixture;
import kitchenpos.menu.fixture.MenuProductFixture;
import kitchenpos.product.domain.Product;
import kitchenpos.product.fixture.ProductFixture;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 항목 테스트")
class OrderLineItemTest {
    private Menu 더블강정;

    @BeforeEach
    void setup() {
        Product 강정치킨 = ProductFixture.create(1L, "강정치킨", BigDecimal.valueOf(17_000));
        MenuGroup 추천메뉴 = MenuGroupFixture.create(1L, "추천메뉴");
        MenuProduct 메뉴_상품 = MenuProductFixture.create(강정치킨.getId(), 2L);

        더블강정 = MenuFixture.create(1L, "더블강정", BigDecimal.valueOf(32_000), 추천메뉴, 메뉴_상품);
    }

    @DisplayName("주문 항목 생성")
    @Test
    void 주문_항목_생성() {
        // given
        Long 수량 = 2L;

        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = () -> OrderLineItem.of(더블강정, 수량);

        // then
        assertThatNoException().isThrownBy(생성_요청);
    }

    @DisplayName("생성시 메뉴는 비어있을 수 없음")
    @Test
    void 생성시_메뉴는_비어있을_수_없음() {
        // given
        Long 수량 = 2L;

        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = () -> OrderLineItem.of(null, 수량);

        // then
        assertThatThrownBy(생성_요청).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("생성시 수량은 비어있을 수 없음")
    @Test
    void 생성시_수량은_비어있을_수_없음() {
        // given
        Long 수량 = null;

        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = () -> OrderLineItem.of(더블강정, 수량);

        // then
        assertThatThrownBy(생성_요청).isInstanceOf(IllegalArgumentException.class);
    }
}
