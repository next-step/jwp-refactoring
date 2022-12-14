package kitchenpos.table.domain;

import static kitchenpos.table.domain.OrderTableTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import kitchenpos.common.exception.InvalidParameterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 테이블 묶음 테스트")
class OrderTablesTest {
    @Test
    @DisplayName("주문 테이블 묶음 객체 생성")
    void createOrderTables() {
        // when
        OrderTables actual = OrderTables.from(Arrays.asList(주문테이블, 주문테이블));

        // then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).isInstanceOf(OrderTables.class)
        );
    }

    @Test
    @DisplayName("주문 테이블 묶음 생성시 최소 2개이상 존재해야한다.")
    void createOrderTablesByLessThanTwo() {
        // when & then
        assertThatThrownBy(() -> OrderTables.from(Collections.singletonList(주문테이블)))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("주문 테이블은 2석 이상이어야 합니다.");
    }

    @Test
    @DisplayName("주문 테이블 묶음 생성시 주문 테이블은 비어있을 수 없다.")
    void createOrderTablesByIsEmptyTable() {
        // when & then
        assertThatThrownBy(() -> OrderTables.from(Collections.emptyList()))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("주문 테이블은 비어있을 수 없습니다.");
    }

    @Test
    @DisplayName("주문 테이블 묶음 내 단체 테이블이 존재할 경우 예외 발생한다.")
    void orderTablesAnyHasGroupId() {
        // when & then
        assertThatThrownBy(() -> OrderTables.from(Arrays.asList(주문테이블, 주문테이블, 단체테이블)))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("단체 테이블이 존재합니다.");
    }

    @Test
    @DisplayName("주문 테이블 묶음 해제")
    void ungroupOrderTables() {
        // given
        OrderTables actual = OrderTables.from(Arrays.asList(주문테이블, 주문테이블));

        // when
        actual.ungroup();

        // then
        assertThat(actual.list()
                .stream()
                .map(OrderTable::tableGroupId)
                .collect(Collectors.toList())).containsExactly(null, null);
    }

    @Test
    @DisplayName("주문 테이블 묶음에 단체 테이블 업데이트")
    void updateTableGroupInOrderTables() {
        // given
        TableGroup tableGroup = TableGroup.of(1L, Arrays.asList(비어있는_테이블, 비어있는_테이블2));
        OrderTable 주문테이블3 = OrderTable.of(6L, null, 7, true);
        OrderTable 주문테이블4 = OrderTable.of(7L, null, 7, true);
        OrderTables orderTables = OrderTables.from(Arrays.asList(주문테이블3, 주문테이블4));

        // when
        orderTables.updateTableGroup(tableGroup);

        // then
        assertAll(
                () -> assertThat(주문테이블3.tableGroupId()).isEqualTo(1L),
                () -> assertThat(주문테이블4.tableGroupId()).isEqualTo(1L)
        );
    }
}
