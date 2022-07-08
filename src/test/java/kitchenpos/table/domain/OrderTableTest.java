package kitchenpos.table.domain;

import static kitchenpos.table.domain.TableGroupTest.테이블_그룹_생성;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTableTest {
    private OrderTable 주문_테이블;

    @BeforeEach
    void init() {
        // given
        주문_테이블 = 주문_테이블_생성(4, true);
    }

    @Test
    @DisplayName("주문 테이블의 방문 손님 수를 0명 이하로 변경할 경우 - 오류")
    void changeNegativeQuantityGuest() {
        // when then
        assertThatThrownBy(() -> 주문_테이블.changeGuests(-5))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("비어 있지 않은 주문 테이블의 손님 수를 변경할 경우 - 오류")
    void changeGuestOfOrderTableIfEmptyIsTrue() {
        // when then
        assertThatThrownBy(() -> 주문_테이블.changeGuests(10))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("비어 있지 않은 주문 테이블을 그룹 지정할 경우 - 오류")
    void groupIfOrderTableIsNotEmpty() {
        // given
        TableGroup 테이블_그룹 = 테이블_그룹_생성(1L);
        OrderTable 비어_있지_않은_주문_테이블 = 주문_테이블_생성(4, false);

        assertThatThrownBy(() -> 비어_있지_않은_주문_테이블.group(테이블_그룹))
            .isInstanceOf(IllegalArgumentException.class);
    }

    public static OrderTable 주문_테이블_생성(int numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }
}
