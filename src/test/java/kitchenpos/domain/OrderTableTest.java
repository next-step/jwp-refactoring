package kitchenpos.domain;



import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;



class OrderTableTest {

    @Test
    @DisplayName("테이블 그룹이 존재하면 변경할수 없다.")
    void changeEmptyValid() {
        //given
        OrderTable orderTable = new OrderTable(1L,3, false);

        //when & then
        assertThatIllegalStateException()
                .isThrownBy(orderTable::changeEmptyTable);
    }

    @Test
    @DisplayName("방문자는 한명 이상이어야 합니다.")
    void changeNumberOfGuests() {

    }

}