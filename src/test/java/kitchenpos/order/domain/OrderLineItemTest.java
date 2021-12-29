package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderLineItemTest {
    
    @DisplayName("주문 생성시 주문 항목도 세팅된다")
    @Test
    void 주문_항목_주문_반영() {
        // given
        OrderTable 테이블 = OrderTable.of(3, false);
        Long 메뉴_Id = 1L;
        OrderLineItem 주문_항목 = OrderLineItem.of(메뉴_Id, 3L);
        
        // when
        Order 주문 = Order.createOrder(테이블, Arrays.asList(주문_항목));
        
        // then
        assertThat(주문_항목.getOrder()).isEqualTo(주문);
    }

}
