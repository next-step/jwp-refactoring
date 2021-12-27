package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.exception.KitchenposException;

class OrderTableTest {

    @DisplayName("테이블 그룹에 포함되어 있으면 에러")
    @Test
    void checkNotGrouped() {
        OrderTable orderTable = new OrderTable(new TableGroup(1L), 4);

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
}