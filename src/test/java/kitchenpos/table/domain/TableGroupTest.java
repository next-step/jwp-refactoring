package kitchenpos.table.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TableGroupTest {

    @DisplayName("테이블그룹에 테이블을 추가할경우 테이블이 없는경우 예외발생")
    @Test
    public void throwsExceptionWhenStatusNotComplete() {
        TableGroup tableGroup = TableGroup.builder().build();

        assertThatThrownBy(() -> tableGroup.addOrderTables(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블그룹에 테이블을 추가할경우 추가할 테이블이 2개 미만인경우 예외발생")
    @Test
    public void throwsExceptionWhenHasTableGroup() {
        TableGroup tableGroup = TableGroup
                .builder()
                .orderTables(OrderTables.of(Arrays.asList(OrderTable.builder().build())))
                .build();

        assertThatThrownBy(() -> tableGroup.addOrderTables(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블그룹에 테이블을 추가할경우 추가할 테이블이 공석이 아니면 예외발생")
    @Test
    public void throwsExceptionWhenNegativeGuest() {

    }

    @DisplayName("테이블그룹에 테이블을 추가할경우 추가할 테이블에 이미 그룹이 있는경우 예외발생")
    @Test
    public void throwsExceptionWhenEmptyTable() {

    }
}
