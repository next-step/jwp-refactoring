package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import kitchenpos.common.exception.CannotUpdatedException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

@DisplayName("테이블 그룹 도메인 테스트")
class TableGroupTest {

    final OrderTable orderTable_1 = OrderTable.of(0, true);
    final OrderTable orderTable_2 = OrderTable.of(0, true);
    TableGroup tableGroup;

    @Test
    @DisplayName("단체 지정")
    void addOrderTables() {
        tableGroup = TableGroup.fromOrderTables(Arrays.asList(orderTable_1, orderTable_2));

        ReflectionTestUtils.setField(tableGroup, "id", 1L);
        ReflectionTestUtils.setField(orderTable_1, "id", 1L);
        ReflectionTestUtils.setField(orderTable_2, "id", 2L);

        assertAll(
            () -> assertNotNull(tableGroup.getCreatedDate()),
            () -> assertThat(tableGroup.getOrderTables().size()).isEqualTo(2)
        );

    }

    @Test
    @DisplayName("단체 지정 해제")
    void clearOrderTable() {
        addOrderTables();

        tableGroup.clearOrderTable();

        assertThat(tableGroup.getOrderTables().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("주문이 진행중인 테이블이 있는 경우 단체 지정 해제 불가")
    void clearOrderTableOnGoingOrder() {
        addOrderTables();

        orderTable_1.changeTableStatus(TableStatus.ORDERED);
        orderTable_2.changeTableStatus(TableStatus.SEATED);

        assertThatThrownBy(() -> tableGroup.clearOrderTable())
            .isInstanceOf(CannotUpdatedException.class)
            .hasMessage("주문이 완료되지 않은 테이블이 있습니다.");
    }
}