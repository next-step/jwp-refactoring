package kitchenpos.tablegroup.domain;

import kitchenpos.table.domain.Empty;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static kitchenpos.common.Messages.HAS_ORDER_TABLE_GROUP;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

class TableGroupTest {
    private OrderTable 주문_테이블_2명;
    private OrderTables 주문_테이블_그룹;
    private TableGroup 테이블_그룹;

    @BeforeEach
    void setUp() {
        주문_테이블_2명 = OrderTable.of(NumberOfGuests.of(2), Empty.of(true));
        주문_테이블_그룹 = OrderTables.of(Arrays.asList(주문_테이블_2명));
        테이블_그룹 = TableGroup.of(1L, LocalDateTime.now(), 주문_테이블_그룹);
    }

    @Test
    @DisplayName("테이블 데이터 검증시 테이블 그룹이 존재하는 경우 실패")
    void validateHasOrderTable() {
        // given
        OrderTable 주문_테이블_그룹_있음 = OrderTable.of(1L, 테이블_그룹, NumberOfGuests.of(4), Empty.of(false));

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(주문_테이블_그룹_있음::validateHasOrderTable)
                .withMessage(HAS_ORDER_TABLE_GROUP)
        ;
    }
}
