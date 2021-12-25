package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;

public class OrderTest {
    
    @DisplayName("주문이 잘 만들어지는지 확인")
    @Test
    void 주문_생성() {
        // when
        OrderTable 테이블 = OrderTable.of(3, false);
        Order 주문 = Order.of(테이블, OrderStatus.COOKING);
        
        // then
        assertThat(주문).isEqualTo(Order.of(테이블, OrderStatus.COOKING));
    }
    
    @DisplayName("주문 상태가 잘 변경되는지 확인")
    @Test
    void 주문_상태_변경() {
        // given
        OrderTable 테이블 = OrderTable.of(3, false);
        Order 주문 = Order.of(테이블, OrderStatus.COOKING);
        
        // when
        주문.changeOrderStatus(OrderStatus.MEAL);
        
        // then
        assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    
    }
    
    @DisplayName("완료된 주문은 상태를 변경할 수 없다 - 예외처리")
    @Test
    void 완료_주문_상태_변경_불가() {
        // given
        OrderTable 테이블 = OrderTable.of(3, false);
        Order 주문 = Order.of(테이블, OrderStatus.COMPLETION);
    
        // when, then
        assertThatThrownBy(() -> {
            주문.changeOrderStatus(OrderStatus.COOKING);
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("계산이 완료된 주문은 상태를 변경 할 수 없습니다");
    
    }
    
    @DisplayName("주문에 메뉴 추가")
    @Test
    void 주문_메뉴_추가() {
        // given
        OrderTable 테이블 = OrderTable.of(3, false);
        Order 주문 = Order.of(테이블, OrderStatus.COOKING);
        List<OrderLineItem> 주문_메뉴 = Arrays.asList(OrderLineItem.of(주문, Menu.of("메뉴", 5000, MenuGroup.from("메뉴그룹")), 2L));
        
        // when
        주문.addOrderLineItems(주문_메뉴);
        
        // then
        assertThat(주문.getOrderLineItems().size()).isEqualTo(1);
    
    }
    
    @DisplayName("주문에 메뉴가 없으면 오류 - 예외처리")
    @Test
    void 주문_메뉴_확인() {
        // given
        OrderTable 테이블 = OrderTable.of(3, false);
        Order 주문 = Order.of(테이블, OrderStatus.COOKING);
        List<OrderLineItem> 주문_메뉴 = new ArrayList<OrderLineItem>();
        
        // when, then
        assertThatThrownBy(() -> {
            주문.addOrderLineItems(주문_메뉴);
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("주문에 메뉴가 없습니다");
    
    }

}
