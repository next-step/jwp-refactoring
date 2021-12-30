package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CompletionOrdersTest {
    
    @DisplayName("목록에 조리중인 주문이 있는지 확인한다")
    @Test
    void 조리중_주문_확인() {
        // given
        Long 메뉴_Id = 1L;
        OrderLineItem 주문_항목 = OrderLineItem.of(메뉴_Id, 3L);
        
        Long 테이블_Id = 1L;
        Order 주문 = Order.of(테이블_Id, OrderStatus.COOKING, Arrays.asList(주문_항목));
        
        // when, then
        assertThatThrownBy(() -> {
            CompletionOrders.from(Arrays.asList(주문));
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("조리중, 식사중인 주문 테이블은 변경할 수 없습니다");
        
    }
    
    @DisplayName("목록에 식사중인 주문이 있는지 확인한다")
    @Test
    void 식사중_주문_확인() {
        // given
        Long 메뉴_Id = 1L;
        OrderLineItem 주문_항목 = OrderLineItem.of(메뉴_Id, 3L);
        
        Long 테이블_Id = 1L;
        Order 주문 = Order.of(테이블_Id, OrderStatus.MEAL, Arrays.asList(주문_항목));
        
        // when, then
        assertThatThrownBy(() -> {
            CompletionOrders.from(Arrays.asList(주문));
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("조리중, 식사중인 주문 테이블은 변경할 수 없습니다");
    }

}
