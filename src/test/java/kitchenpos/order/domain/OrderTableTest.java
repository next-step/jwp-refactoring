package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.error.OrderTableEmptyException;

@DisplayName("주문 테이블 도메인 테스트")
class OrderTableTest {

    private OrderTable orderTable;

    @BeforeEach
    void setup() {
        orderTable = OrderTable.of(new NumberOfGuests(2), true);
    }

    @DisplayName("생성")
    @Test
    void create() {
        // given
        // when
        // then
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @DisplayName("고객수 변경")
    @Test
    void changeNumberOfGuests() {
        // given
        // when
        orderTable.changeNumberOfGuests(new NumberOfGuests(10));
        // then
        assertThat(orderTable.numberOfGuestsToInt()).isEqualTo(10);
    }

    @DisplayName("비어 있는지 체크")
    @Test
    void checkEmpty() {
        // given
        // when
        // then
        assertThatThrownBy(() -> orderTable.checkEmpty())
                .isInstanceOf(OrderTableEmptyException.class);
    }
}