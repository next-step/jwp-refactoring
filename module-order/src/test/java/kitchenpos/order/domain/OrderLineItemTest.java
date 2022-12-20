package kitchenpos.order.domain;

import kitchenpos.core.exception.InvalidQuantityException;
import kitchenpos.menu.domain.FixtureMenu;
import kitchenpos.menu.domain.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("OrderLineItem 클래스 테스트")
class OrderLineItemTest {

    private Menu menu = new FixtureMenu("강정치킨");

    @DisplayName("OrderLineItem 생성한다.")
    @Test
    void successfulCreate() {
        OrderLineItem orderLineItem = new OrderLineItem(menu.toOrderedMenu(), 1);
        assertThat(orderLineItem).isNotNull();
    }

    @DisplayName("메뉴없이 OrderLineItem 생성한다.")
    @Test
    void failureCreateWithEmptyMenu() {
        assertThatThrownBy(() -> {
            new OrderLineItem(null, 1);
        }).isInstanceOf(NullPointerException.class);
    }

    @DisplayName("주량이 -1인 OrderLineItem 생성한다.")
    @Test
    void failureCreateWithNegativeQuantity() {
        assertThatThrownBy(() -> {
            new OrderLineItem(menu.toOrderedMenu(), -1);
        }).isInstanceOf(InvalidQuantityException.class);
    }
}
