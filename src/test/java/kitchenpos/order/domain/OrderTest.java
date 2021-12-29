package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTest {
    
    @DisplayName("식사중으로 주문 상태를 변경할 수 있다")
    @Test
    void 식사중_상태_변경() {
        // given
        Long 메뉴_Id = 1L;
        OrderLineItem 주문_항목 = OrderLineItem.of(메뉴_Id, 3L);
        
        OrderTable 테이블 = OrderTable.of(3, false);
        Order 주문 = Order.of(테이블, OrderStatus.COOKING, Arrays.asList(주문_항목));
        
        // when
        주문.onMealing();
        
        // then
        assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    
    }
    
    @DisplayName("계산완료로 주문 상태를 변경할 수 있다")
    @Test
    void 계산_완료_상태_변경() {
        // given
        Long 메뉴_Id = 1L;
        OrderLineItem 주문_항목 = OrderLineItem.of(메뉴_Id, 3L);
        
        OrderTable 테이블 = OrderTable.of(3, false);
        Order 주문 = Order.of(테이블, OrderStatus.MEAL, Arrays.asList(주문_항목));
        
        // when
        주문.completed();
        
        // then
        assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    
    }
    
    @DisplayName("완료된 주문은 상태를 변경할 수 없다")
    @Test
    void 완료_주문_상태_변경_불가() {
        // given
        Long 메뉴_Id = 1L;
        OrderLineItem 주문_항목 = OrderLineItem.of(메뉴_Id, 3L);
        
        OrderTable 테이블 = OrderTable.of(3, false);
        Order 주문 = Order.of(테이블, OrderStatus.COMPLETION, Arrays.asList(주문_항목));
    
        // when, then
        assertThatThrownBy(() -> {
            주문.onMealing();
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("계산이 완료된 주문은 상태를 변경 할 수 없습니다");
    
    }
    
    
    @DisplayName("주문에 메뉴가 있어야 한다")
    @Test
    void 주문_메뉴_확인() {
        // given
        OrderTable 테이블 = OrderTable.of(3, false);
        
        // when, then
        assertThatThrownBy(() -> {
            Order.of(테이블, OrderStatus.COOKING, new ArrayList<OrderLineItem>());
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("주문에 메뉴가 없습니다");
    
    }

}
