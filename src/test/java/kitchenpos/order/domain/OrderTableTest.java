package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTableTest {
    
    @DisplayName("조리중이거나 식사중인 테이블은 빈 테이블로 변경할 수 없다")
    @Test
    void 조리중_식사중_테이블은_빈_테이블_변경_불가() {
        // given
        Long 메뉴_Id = 1L;
        OrderLineItem 주문_항목 = OrderLineItem.of(메뉴_Id, 3L);
        OrderTable 테이블 = OrderTable.of(3, false);
        Order 주문 = Order.of(테이블, OrderStatus.COOKING, Arrays.asList(주문_항목));
    
        // when, then
        assertThatThrownBy(() -> {
            테이블.changeEmpty(true);
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("조리중, 식사중인 주문 테이블은 변경할 수 없습니다");
    
    }
    
    @DisplayName("테이블 최소 손님 수를 확인한다")
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
