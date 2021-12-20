package kitchenpos.order.domain;

import kitchenpos.fixture.MenuGroupTestFixture;
import kitchenpos.fixture.MenuTestFixture;
import kitchenpos.fixture.OrderLineItemTestFixture;
import kitchenpos.fixture.OrderTestFixture;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menuGroup.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderLineItemsTest {

    @DisplayName("주문항목 일급 콜렉션 생성")
    @Test
    void create() {
        OrderLineItems orderLineItems = new OrderLineItems();

        assertThat(orderLineItems).isNotNull();
    }

    @DisplayName("주문항목 추가")
    @Test
    void add() {
        MenuGroup 치킨류 = MenuGroupTestFixture.생성(1L, "치킨");
        Menu 후라이드두마리세트 = MenuTestFixture.생성(1L, "후라이드두마리세트", new BigDecimal("10000"), 치킨류);
        OrderTable 테이블1번 = new OrderTable();
        Order 총주문 = OrderTestFixture.생성(테이블1번);
        OrderLineItem 후라이드두마리세트_두개 = OrderLineItemTestFixture.생성(총주문, 후라이드두마리세트, 2L);
        OrderLineItems orderLineItems = new OrderLineItems();

        orderLineItems.add(후라이드두마리세트_두개);

        assertThat(orderLineItems.getOrderLineItems().contains(후라이드두마리세트_두개)).isTrue();
    }
}
