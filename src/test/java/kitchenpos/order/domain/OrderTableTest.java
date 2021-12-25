package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTableTest {
    
    @DisplayName("주문 테이블이 잘 만들어지는지 확인")
    @Test
    void 주문_테이블_생성() {
        // when
        OrderTable 테이블 = OrderTable.of(3, false);
        
        // then
        assertThat(테이블).isEqualTo(OrderTable.of(3, false));
    
    }
    
    @DisplayName("조리중이거나 식사중인 테이블은 빈 테이블로 변경할 수 없다 - 예외처리")
    @Test
    void 조리중_식사중_테이블은_빈_테이블_변경_불가() {
        // given
        OrderTable 테이블 = OrderTable.of(3, false);
        테이블.addOrders(Arrays.asList(Order.of(테이블, OrderStatus.COOKING, new ArrayList<OrderLineItem>())));
    
        // when, then
        assertThatThrownBy(() -> {
            테이블.changeEmpty(true);
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("조리중, 식사중인 주문 테이블은 변경할 수 없습니다");
    
    }
    
    @DisplayName("테이블 최소 손님 수를 확인한다 - 예외처리")
    @Test
    void 테이블_최소_손님_수_확인() {
        // given
        OrderTable 테이블 = OrderTable.of(3, false);
    
        // when, then
        assertThatThrownBy(() -> {
            테이블.changeNumberOfGuests(-2);
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage(String.format("테이블의 손님 수는 최소 %d명 이상이어야합니다", OrderTable.MIN_NUMBER_OF_GUESTS));
    
    }
    

}
