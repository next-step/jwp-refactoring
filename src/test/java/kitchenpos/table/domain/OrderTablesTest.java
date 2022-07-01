package kitchenpos.table.domain;

import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.common.Messages.TABLE_GROUP_ORDER_IDS_FIND_IN_NO_SUCH;
import static kitchenpos.common.Messages.TABLE_GROUP_ORDER_NOT_EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
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
    @DisplayName("주문 테이블 생성시 조회된 ID 숫자가 다른경우 실패")
    void tableGroupOrderIdsFindInNoSuch() {
        // given
        List<Long> requestOrderTablesIds = Arrays.asList(1L);

        // when
        OrderTables orderTables = OrderTables.of(Arrays.asList(주문_테이블_4명, 주문_테이블_2명));

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderTables.validateOrderTableGroup(requestOrderTablesIds))
                .withMessage(TABLE_GROUP_ORDER_IDS_FIND_IN_NO_SUCH)
        ;
    }

    @Test
    @DisplayName("주문 테이블 생성시 조회된 ID 숫자가 다른경우 실패")
    void tableGroupOrderNotEmpty() {
        // given
        List<Long> requestOrderTablesIds = Arrays.asList(1L, 2L);
        주문_테이블_4명 = OrderTable.of(NumberOfGuests.of(4), Empty.of(false));

        // when
        OrderTables orderTables = OrderTables.of(Arrays.asList(주문_테이블_4명, 주문_테이블_2명));

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderTables.validateOrderTableGroup(requestOrderTablesIds))
                .withMessage(TABLE_GROUP_ORDER_NOT_EMPTY)
        ;
    }

    @Test
    @DisplayName("테이블 그룹 해제")
    void ungroup() {
        // given
        OrderTables tables = OrderTables.of(Arrays.asList(주문_테이블_2명));
        TableGroup tableGroup = TableGroup.of(tables);
        OrderTable 주문_테이블_그룹_있음 = OrderTable.of(1L, tableGroup, NumberOfGuests.of(4), Empty.of(false));
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
