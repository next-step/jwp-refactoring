package kitchenpos.table.domain;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.table.message.NumberOfGuestsMessage;
import kitchenpos.table.message.OrderTableMessage;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableTest {

    @Test
    @DisplayName("주문 테이블 생성에 성공한다")
    void createOrderTableTest() {
        // when
        OrderTable orderTable = OrderTable.of(0, true);

        // then
        assertThat(orderTable).isEqualTo(OrderTable.of(0, true));
    }

    @Test
    @DisplayName("주문 테이블의 이용 여부 상태를 사용중인 상태로 변경한다")
    void changeOrderTableNotEmptyTest() {
        // given
        OrderTable orderTable = OrderTable.of(0, true);

        // when
        orderTable.changeEmpty(false);

        // then
        assertThat(orderTable.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("주문 테이블을 이용 여부 상태를 빈테이블 상태로 변경한다")
    void changeOrderTableEmptyTest() {
        // given
        OrderTable orderTable = OrderTable.of(0, false);

        // when
        orderTable.changeEmpty(true);

        // then
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("주문 테이블의 이용 여부 변경시 테이블 그룹에 속해져있는 경우 예외처리되어 상태 변경에 실패한다")
    void changeOrderTableEmptyThrownByEnrolledTableGroupTest() {
        // given
        TableGroup tableGroup = TableGroup.empty();
        OrderTable orderTable = OrderTable.of(0, false);
        orderTable.enrollGroup(tableGroup);

        // when & then
        assertThatThrownBy(() -> orderTable.changeEmpty(true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderTableMessage.CHANGE_EMPTY_ERROR_TABLE_GROUP_MUST_BE_NOT_ENROLLED.message());
    }

    @Test
    @DisplayName("주문 테이블의 이용 여부 변경시 테이블의 상태가 조리 또는 식사중인경우 변경에 실패한다")
    void changeOrderTableEmptyThrownByTableStateTest() {
        // given
        OrderTable orderTable = OrderTable.of(0, false);
        orderTable.addOrder(new Order(OrderStatus.COOKING));

        // when & then
        assertThatThrownBy(() -> orderTable.changeEmpty(true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderTableMessage.CHANGE_EMPTY_ERROR_INVALID_ORDER_STATE.message());
    }

    @Test
    @DisplayName("주문 테이블의 손님수 변경시 변경에 성공한다")
    void changeNumberOfGuestsTest() {
        // given
        OrderTable orderTable = OrderTable.of(1, false);

        // when
        orderTable.changeNumberOfGuests(3);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(NumberOfGuests.of(3));
    }

    @Test
    @DisplayName("주문 테이블의 손님수 변경시 변경가능한 최소 인원보다 적게 주어진다면 변경에 실패한다")
    void changeNumberOfGuestsThrownByLessThanMinGuestsTest() {
        // given
        OrderTable orderTable = OrderTable.of(1, false);

        // when & then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(NumberOfGuestsMessage.CREATE_ERROR_GUESTS_MUST_BE_MORE_THAN_ZERO.message());
    }

    @Test
    @DisplayName("주문 테이블의 손님수 변경시 해당 테이블이 빈테이블이라면 변경에 실패한다")
    void changeNumberOfGuestsThrownByEmptyTableTest() {
        // given
        OrderTable orderTable = OrderTable.of(1, true);

        // when & then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderTableMessage.CHANGE_GUESTS_ERROR_TABLE_MUST_BE_NOT_EMPTY_STATE.message());
    }
}
