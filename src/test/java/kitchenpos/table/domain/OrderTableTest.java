package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTableTest {
    
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
