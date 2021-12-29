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
            .isThrownBy(orderTable::checkNotGrouped)
            .withMessage("주문 테이블이 그룹에 포함되어 있습니다.");
    }

    @DisplayName("테이블 그룹이 비어이있으면 에러")
    @Test
    void checkNotEmpty() {
        OrderTable orderTable = new OrderTable(4, true);

        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> orderTable.checkNotEmpty())
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
}