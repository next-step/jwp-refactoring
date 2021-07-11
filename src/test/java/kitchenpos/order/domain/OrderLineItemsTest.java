package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.exception.NotInitMenuException;
import kitchenpos.order.exception.NotOrderLineItemsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderLineItemsTest {

    private Menu 치킨메뉴 = new Menu(1L, "치킨메뉴", BigDecimal.valueOf(20000));
    private Menu 튀김메뉴 = new Menu(2L, "튀김메뉴", BigDecimal.valueOf(10000));
    private OrderLineItem 주문수량메뉴1 = new OrderLineItem(1L, 1L, 치킨메뉴.getId(), 1L);
    private OrderLineItem 주문수량메뉴2 = new OrderLineItem(2L, 1L, 튀김메뉴.getId(), 1L);
    private List<OrderLineItem> 주문내역들 = Arrays.asList(주문수량메뉴1, 주문수량메뉴2);

    @DisplayName("주문이 생성된다.")
    @Test
    void create() {
        OrderLineItems orderLineItems = new OrderLineItems(주문내역들);

        assertThat(orderLineItems.getOrderLineItemValues()).containsExactly(주문수량메뉴1, 주문수량메뉴2);
    }

    @DisplayName("주문 생성에 실패한다. - 주문 내역이 없으면 주문 등록에 실패한다.")
    @Test
    void fail_create1() {
        assertThatThrownBy(() -> new OrderLineItems(Collections.emptyList()))
                .isInstanceOf(NotOrderLineItemsException.class);
    }

    @DisplayName("주문 생성에 실패한다. - 등록되지 않은 메뉴 주문시 등록 실패한다.")
    @Test
    void fail_create2() {
        int 주문내역중_등록된메뉴_갯수 = 주문내역들.size() - 1;
        OrderLineItems orderLineItems = new OrderLineItems(주문내역들);
        assertThatThrownBy(() -> orderLineItems.checkInitOrderLineItems(주문내역중_등록된메뉴_갯수))
                .isInstanceOf(NotInitMenuException.class);
    }
}
