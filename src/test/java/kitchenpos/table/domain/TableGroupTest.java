package kitchenpos.table.domain;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.table.domain.fixture.OrderTableFixtureFactory;
import kitchenpos.table.domain.fixture.TableGroupFixtureFactory;
import kitchenpos.table.exception.CannotMakeTableGroupException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @Test
    @DisplayName("테이블 그룹 지정")
    void 테이블그룹_지정() {
        OrderTable emptyTable = OrderTableFixtureFactory.createEmptyOrderTable();
        OrderTable emptyTable2 = OrderTableFixtureFactory.createEmptyOrderTable();
        TableGroup tableGroup = TableGroupFixtureFactory.createTableGroup(emptyTable, emptyTable2);

        List<OrderTable> orderTables = tableGroup.getOrderTables();
        List<OrderTable> notEmptyOrderTables = orderTables.stream()
                .filter(orderTable -> orderTable.isEmpty() == false)
                .collect(toList());

        Assertions.assertAll("테이블 그룹 생성"
                , () -> assertThat(orderTables).hasSize(2)
                , () -> assertThat(notEmptyOrderTables).hasSize(2)
        );
    }

    @Test
    @DisplayName("2개 미만의 테이블을 그룹 지정할 경우 실패")
    void 테이블그룹_지정_테이블이_2개미만인경우() {
        OrderTable emptyTable = OrderTableFixtureFactory.createEmptyOrderTable();
        assertThatThrownBy(() -> TableGroupFixtureFactory.createTableGroup(emptyTable))
                .isInstanceOf(CannotMakeTableGroupException.class);
    }

    @Test
    @DisplayName("비어있지않은 테이블이 포함된 경우 테이블 그룹 지정 실패")
    void 테이블그룹_지정_비어있지않은_테이블이_포함된_경우() {
        int numberOfGuests = 1;
        OrderTable emptyTable = OrderTableFixtureFactory.createEmptyOrderTable();
        OrderTable notEmptyTable = OrderTableFixtureFactory.createNotEmptyOrderTable(numberOfGuests);
        assertThatThrownBy(() -> TableGroupFixtureFactory.createTableGroup(emptyTable, notEmptyTable))
                .isInstanceOf(CannotMakeTableGroupException.class);
    }

    @Test
    @DisplayName("테이블 그룹에 주문이 없는 경우 테이블 그룹 해제가능")
    void 테이블그룹_지정해제_주문이_없는_경우() {
        OrderTable emptyTable = OrderTableFixtureFactory.createEmptyOrderTable();
        OrderTable emptyTable2 = OrderTableFixtureFactory.createEmptyOrderTable();
        TableGroup tableGroup = TableGroupFixtureFactory.createTableGroup(emptyTable, emptyTable2);

        tableGroup.ungroup();
        Assertions.assertAll("테이블 그룹에서 지정 해제되었는지 확인"
                , () -> assertThat(emptyTable.getTableGroup()).isNull()
                , () -> assertThat(emptyTable.isEmpty()).isFalse()
                , () -> assertThat(emptyTable2.getTableGroup()).isNull()
                , () -> assertThat(emptyTable2.isEmpty()).isFalse()
        );
    }
}
