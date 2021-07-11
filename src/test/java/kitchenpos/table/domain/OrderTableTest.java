package kitchenpos.table.domain;

import static kitchenpos.order.domain.OrderTest.주문_항목_목록;
import static kitchenpos.order.domain.OrderTest.주문테이블;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.exception.CannotOrderException;
import kitchenpos.table.exception.CannotChangeNumberOfGuestException;
import kitchenpos.table.exception.CannotChangeTableEmptyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문테이블 테스트")
public class OrderTableTest {

    @DisplayName("단체 지정된 주문테이블인 경우 빈 테이블로 변경이 불가능합니다.")
    @Test
    void changeEmpty_Fail01() {
        // Given
        OrderTable 주문테이블 = new OrderTable(1L, 1L, 2, false);

        // When & Then
        assertThatThrownBy(() -> 주문테이블.changeEmpty(true))
            .isInstanceOf(CannotChangeTableEmptyException.class);
    }

    @DisplayName("진행중(조리 or 식사)인 경우 빈 테이블로 변경이 불가능하다.")
    @Test
    void changeEmpty_Fail02() {
        // Given
        Order 진행중인_주문 = new Order(1L, 주문테이블.getId(), OrderStatus.COOKING, LocalDateTime.now(), 주문_항목_목록);
        OrderTable 주문테이블 = new OrderTable(1L, 2, 진행중인_주문);

        // When & Then
        assertThatThrownBy(() -> 주문테이블.changeEmpty(true))
            .isInstanceOf(CannotChangeTableEmptyException.class);
    }

    @DisplayName("빈 테이블의 주문 테이블은 손님 수를 변경할 수 없습니다.")
    @Test
    void changeNumberOfGuests_Fail01() {
        // Given
        OrderTable 비어있는_주문테이블 = new OrderTable(1L, 2, true);

        // When & Then
        assertThatThrownBy(() -> 비어있는_주문테이블.changeNumberOfGuests(3))
            .isInstanceOf(CannotChangeNumberOfGuestException.class);
    }

    @DisplayName("변경하려는 손님 수는 최소 1명 이상이어야 한다.")
    @Test
    void changeNumberOfGuests_Fail02() {
        // Given
        OrderTable 주문테이블 = new OrderTable(1L, 2, true);

        // When & Then
        assertThatThrownBy(() -> 주문테이블.changeNumberOfGuests(-1))
            .isInstanceOf(CannotChangeNumberOfGuestException.class);
    }

    @DisplayName("빈 테이블인경우 주문할 수 없다.")
    @Test
    void addOrder_fail() {
        // Given
        OrderTable 주문테이블 = new OrderTable(1L, 2, true);
        Order 주문 = new Order(1L);

        // When & Then
        assertThatThrownBy(() -> 주문테이블.addOrder(주문))
            .isInstanceOf(CannotOrderException.class);
    }


}
