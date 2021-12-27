package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;

public class OrdersTest {
    
    @DisplayName("주문 목록에 주문을 추가한다")
    @Test
    void 주문_추가() {
     // given
        Menu 메뉴 = Menu.of("메뉴", 5000L, MenuGroup.from("메뉴그룹"));
        OrderLineItem 첫번째_주문_항목 = OrderLineItem.of(메뉴, 3L);
        OrderLineItem 두번째_주문_항목 = OrderLineItem.of(메뉴, 3L);
        
        OrderTable 테이블 = OrderTable.of(3, false);
        Order 첫번째_주문 = Order.of(테이블, OrderStatus.COOKING, Arrays.asList(첫번째_주문_항목));
        Order 두번째_주문 = Order.of(테이블, OrderStatus.MEAL, Arrays.asList(두번째_주문_항목));
        Orders 주문_목록 = Orders.from(new ArrayList<Order>(Arrays.asList(첫번째_주문)));
        
        // when
        주문_목록.add(두번째_주문);
        
        // then
        assertThat(주문_목록.getOrders()).containsExactlyElementsOf(Arrays.asList(첫번째_주문, 두번째_주문));
    }
    
    @DisplayName("목록에 조리중인 주문이 있는지 확인한다")
    @Test
    void 조리중_주문_확인() {
        // given
        Menu 메뉴 = Menu.of("메뉴", 5000L, MenuGroup.from("메뉴그룹"));
        OrderLineItem 주문_항목 = OrderLineItem.of(메뉴, 3L);
        
        OrderTable 테이블 = OrderTable.of(3, false);
        Order 주문 = Order.of(테이블, OrderStatus.COOKING, Arrays.asList(주문_항목));
        Orders 주문_목록 = Orders.from(Arrays.asList(주문));
        
        // when
        boolean 조리중_주문_유무 = 주문_목록.isContainsCookingStatus();
        
        // then
        assertThat(조리중_주문_유무).isTrue();
    }
    
    @DisplayName("목록에 식사중인 주문이 있는지 확인한다")
    @Test
    void 식사중_주문_확인() {
        // given
        Menu 메뉴 = Menu.of("메뉴", 5000L, MenuGroup.from("메뉴그룹"));
        OrderLineItem 주문_항목 = OrderLineItem.of(메뉴, 3L);
        
        OrderTable 테이블 = OrderTable.of(3, false);
        Order 주문 = Order.of(테이블, OrderStatus.MEAL, Arrays.asList(주문_항목));
        Orders 주문_목록 = Orders.from(Arrays.asList(주문));
        
        // when
        boolean 조리중_주문_유무 = 주문_목록.isContainsMealStatus();
        
        // then
        assertThat(조리중_주문_유무).isTrue();
    }

}
