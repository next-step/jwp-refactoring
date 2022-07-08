package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 항목 일급 컬랙션")
class OrderLineItemsTest {

    @DisplayName("주문 항목을 추가할 수 있다.")
    @Test
    void 주문_항목_추가() {
        OrderLineItems 주문_항목들 = new OrderLineItems();

        Long 메뉴_아이디 = 1L;
        주문_항목들.add(OrderLineItem.of(메뉴_아이디, 1L));

        assertThat(주문_항목들.value()).isNotEmpty();
    }

    @DisplayName("주문 항목 리스트가 비어 있는지 확인할 수 있다.")
    @Test
    void 주문_항목_리스트가_비어있는지_확인() {
        OrderLineItems 주문_항목들 = new OrderLineItems();
        assertThat(주문_항목들.isEmpty()).isTrue();
    }
}
