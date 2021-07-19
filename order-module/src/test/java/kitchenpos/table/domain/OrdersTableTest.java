package kitchenpos.table.domain;

import kitchenpos.exception.OrderTableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 테이블 entity 테스트")
class OrdersTableTest {


    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable(1L, 0, true);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void 주문_테이블_상태_변경(boolean expectedBoolean) {
        orderTable.changeEmpty(expectedBoolean);
        assertThat(orderTable.isEmpty()).isEqualTo(expectedBoolean);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4})
    void 주문_테이블_상태_변경(int numberOfGuests) {
        orderTable.changeNumberOfGuests(numberOfGuests);
        assertThat(orderTable.getNumberOfGuests().numberOfGuests()).isEqualTo(numberOfGuests);
    }

    @Test
    void 상태_변경시_비어있는_주문_테이블인_경우_에러발생() {
        assertThatThrownBy(() -> orderTable.checkIsEmpty()).isInstanceOf(OrderTableException.class);
    }
}
