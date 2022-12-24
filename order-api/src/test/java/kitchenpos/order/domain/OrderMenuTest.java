package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.menu.fixture.MenuTestFixture.메뉴세트;
import static kitchenpos.menu.fixture.MenuTestFixture.짬뽕_탕수육_1인_메뉴_세트_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class OrderMenuTest {
    @DisplayName("주문메뉴 생성 작업을 성공한다.")
    @Test
    void of() {
        // given
        Menu menu = 메뉴세트(짬뽕_탕수육_1인_메뉴_세트_요청(), 1L);


        // when
        OrderMenu orderMenu = OrderMenu.from(menu);

        // then
        assertAll(
                () -> assertThat(orderMenu).isNotNull(),
                () -> assertThat(orderMenu.getMenuId()).isEqualTo(menu.getId())
        );
    }
}
