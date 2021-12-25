package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;

public class OrderLineItemTest {
    
    @DisplayName("주문 항목이 잘 만들어지는지 확인")
    @Test
    void 주문_항목_생성() {
        // given
        OrderTable 테이블 = OrderTable.of(3, false);
        Order 주문 = Order.of(테이블, OrderStatus.COOKING);
        Menu 메뉴 = Menu.of("메뉴", 5000, MenuGroup.from("메뉴그룹"));
        
        // when
        OrderLineItem 주문_항목 = OrderLineItem.of(주문, 메뉴, 3L);
        
        // then
        assertAll(
                () -> assertThat(주문_항목.getMenu()).isEqualTo(메뉴),
                () -> assertThat(주문_항목.getOrder()).isEqualTo(주문),
                () -> assertThat(주문_항목.getQuantity()).isEqualTo(3L),
                () -> assertThat(주문_항목).isEqualTo(OrderLineItem.of(주문, 메뉴, 3L))
        );
    }
    
    @DisplayName("주문 항목에 주문이 잘 반영되는지 확인")
    @Test
    void 주문_항목_주문_반영() {
        // given
        OrderTable 테이블 = OrderTable.of(3, false);
        Order 주문 = Order.of(테이블, OrderStatus.COOKING);
        Menu 메뉴 = Menu.of("메뉴", 5000, MenuGroup.from("메뉴그룹"));
        OrderLineItem 주문_항목 = OrderLineItem.of(주문, 메뉴, 3L);
        
        // when
        주문_항목.updateOrder(Order.of(테이블, OrderStatus.MEAL));
        
        // then
        assertThat(주문_항목.getOrder()).isEqualTo(Order.of(테이블, OrderStatus.MEAL));
    }

}
