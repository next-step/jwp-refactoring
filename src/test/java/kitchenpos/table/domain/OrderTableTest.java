package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.exception.KitchenposException;

class OrderTableTest {

    @DisplayName("테이블 그룹에 포함되어 있으면 에러")
    @Test
    void checkNotGrouped() {
        OrderTable orderTable = new OrderTable(1L, 4);

        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> orderTable.updateEmpty(true))
            .withMessage("주문 테이블이 그룹에 포함되어 있습니다.");
    }

    @DisplayName("테이블 그룹이 비어이있으면 에러")
    @Test
    void checkNotEmpty() {
        OrderTable orderTable = new OrderTable(4, true);

        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(orderTable::validateNotEmpty)
            .withMessage("주문 테이블이 비어있습니다.");
    }

    @DisplayName("그룹화할 수 없는 테이블인지 확인")
    @Test
    void cannotBeGroupedTrue() {
        OrderTable orderTable1 = new OrderTable(4, false);
        OrderTable orderTable2 = new OrderTable(1L, 4);

        assertAll(
            () -> assertThat(orderTable1.cannotBeGrouped()).isTrue(),
            () -> assertThat(orderTable2.cannotBeGrouped()).isTrue()
        );
    }

    @DisplayName("그룹화할 수 있는 테이블인지 확인")
    @Test
    void cannotBeGroupedFalse() {
        OrderTable orderTable1 = new OrderTable(4, true);

        assertThat(orderTable1.cannotBeGrouped()).isFalse();
    }

    @DisplayName("손님 숫자 변경")
    @Test
    void updateNumberOfGuests() {
        OrderTable orderTable = new OrderTable(4, false);

        orderTable.updateNumberOfGuests(new NumberOfGuests(2));

        assertThat(orderTable.getGuestNumber()).isEqualTo(2);
    }

    @DisplayName("비어있는 테이블의 손님 숫자 변경시 에러")
    @Test
    void updateNumberOfGuestsError() {
        OrderTable orderTable = new OrderTable(4, true);

        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> orderTable.updateNumberOfGuests(new NumberOfGuests(2)))
            .withMessage("주문 테이블이 비어있습니다.");
    }

    @DisplayName("테이블 그룹 제거")
    @Test
    void unGroup() {
        OrderTable orderTable = new OrderTable(1L, 1L, 4, false);

        orderTable.ungroup();

        assertThat(orderTable.getTableGroupId()).isNull();
    }

    @DisplayName("테이블 그룹화")
    @Test
    void group() {
        OrderTable orderTable = new OrderTable(1L, null, 4, true);

        orderTable.group(1L);

        assertAll(
            () -> assertThat(orderTable.getTableGroupId()).isEqualTo(1L),
            () -> assertThat(orderTable.isEmpty()).isFalse()
        );
    }
}