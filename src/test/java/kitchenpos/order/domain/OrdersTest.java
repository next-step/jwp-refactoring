package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrdersTest {
    
    @DisplayName("주문 목록에 주문을 추가한다")
    @Test
    void 주문_추가() {
     // given
        OrderTable 테이블 = OrderTable.of(3, false);
        Order 첫번째_주문 = Order.of(테이블, OrderStatus.COOKING);
        Order 두번째_주문 = Order.of(테이블, OrderStatus.MEAL);
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
        OrderTable 테이블 = OrderTable.of(3, false);
        Order 주문 = Order.of(테이블, OrderStatus.COOKING);
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
        OrderTable 테이블 = OrderTable.of(3, false);
        Order 주문 = Order.of(테이블, OrderStatus.MEAL);
        Orders 주문_목록 = Orders.from(Arrays.asList(주문));
        
        // when
        boolean 조리중_주문_유무 = 주문_목록.isContainsMealStatus();
        
        // then
        assertThat(조리중_주문_유무).isTrue();
    }

}
