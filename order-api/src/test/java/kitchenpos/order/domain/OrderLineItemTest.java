package kitchenpos.order.domain;

import kitchenpos.menu.dto.MenuRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static kitchenpos.menu.fixture.MenuProductTestFixture.짜장면메뉴상품요청;
import static kitchenpos.menu.fixture.MenuTestFixture.메뉴세트;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderLineItemTest {

    @DisplayName("주문정보 생성 작업을 성공한다.")
    @Test
    void of() {
        // given
        long expectedMenuId = 1L;
        long expectedQuantity = 1L;
        OrderMenu orderMenu = OrderMenu.from(메뉴세트(MenuRequest.of("메뉴", BigDecimal.ONE, 1L,
                Collections.singletonList(짜장면메뉴상품요청())), 1L));
        OrderLineItem orderLineItem = OrderLineItem.of(orderMenu, 1L);

        // when & then
        assertAll(
                () -> assertThat(orderLineItem.getQuantity()).isEqualTo(expectedQuantity)
        );
    }
}
