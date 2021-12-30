package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTableTest {
    
    @DisplayName("테이블 손님 수는 0명 이상이어야한다")
    @Test
    void 테이블_손님_수_확인() {
        // given
        OrderTable 테이블 = OrderTable.of(3, false);
    
        // when, then
        assertThatThrownBy(() -> {
            테이블.changeNumberOfGuests(-2);
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("테이블의 손님 수는 최소 0명 이상이어야합니다");
    
    }
    

}
