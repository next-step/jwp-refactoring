package kitchenpos.table.domain;

import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderTablesTest {

    private OrderTable 주문_테이블_2명;
    private OrderTable 주문_테이블_4명;

    @BeforeEach
    void setUp() {
        주문_테이블_2명 = OrderTable.of(NumberOfGuests.of(2), Empty.of(true));
        주문_테이블_4명 = OrderTable.of(NumberOfGuests.of(4), Empty.of(true));
    }

    @Test
    @DisplayName("주문 테이블 생성 테스트")
    void orderTablesCreate() {
        // when
        OrderTables orderTables = OrderTables.of(Arrays.asList(주문_테이블_4명));

        // then
        assertAll(
                () -> assertThat(orderTables).isNotNull(),
                () -> assertThat(orderTables.getOrderTables()).isEqualTo(Arrays.asList(주문_테이블_4명))
        );
    }

    @Test
    @DisplayName("테이블 그룹 해제")
    void ungroup() {
        // given
        TableGroup tableGroup = TableGroup.of(LocalDateTime.now());
        OrderTable 주문_테이블_그룹_있음 = OrderTable.of(1L, tableGroup.getId(), NumberOfGuests.of(4), Empty.of(false));
        OrderTables orderTables = OrderTables.of(Arrays.asList(주문_테이블_그룹_있음));

        // when
        orderTables.ungroup();

        // then
        assertAll(
                () -> assertThat(orderTables.getOrderTables()).size().isEqualTo(1),
                () -> assertThat(orderTables.getOrderTables().get(0).getTableGroupId()).isNull()
        );
    }
}
