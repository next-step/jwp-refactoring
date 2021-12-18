package kitchenpos.domain.table;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.exception.table.HasOtherTableGroupException;
import kitchenpos.exception.table.NotEmptyOrderTableException;
import kitchenpos.exception.table.NotGroupingOrderTableCountException;

public class TableGroupDomainTest {
    @DisplayName("주문테이블의 개수가 2개 미만으로 단체지정시 예외가 발생된다.")
    @Test
    void exception_createTableGroup_underTwoCountOrderTable() {
        // given
        OrderTable 치킨_주문_단체테이블 = OrderTable.of(0, true);
        List<OrderTable> 조회된_주문테이블_리스트 = List.of(치킨_주문_단체테이블);

        // when
        // then
        Assertions.assertThatExceptionOfType(NotGroupingOrderTableCountException.class)
                    .isThrownBy(() -> TableGroup.of(OrderTables.of(조회된_주문테이블_리스트)));
    }

    @DisplayName("단체지정 될 주문테이블이 이미 단체지정에 등록된 경우 예외가 발생된다.")
    @Test
    void exception_createTableGroup_existOrderTableInOtherTableGroup() {
        // given
        OrderTable 치킨_주문_단체테이블 = OrderTable.of(0, true);
        OrderTable 치킨2_주문_단체테이블 = OrderTable.of(0, true);
        OrderTable 치킨3_주문_단체테이블 =  OrderTable.of(0, true);

        TableGroup.of(OrderTables.of(List.of(치킨_주문_단체테이블, 치킨2_주문_단체테이블)));

        // when
        // then
        Assertions.assertThatExceptionOfType(HasOtherTableGroupException.class)
                    .isThrownBy(() -> TableGroup.of(OrderTables.of(List.of(치킨2_주문_단체테이블, 치킨3_주문_단체테이블))));
    }

    @DisplayName("빈테이블이 아닌 주문테이블 포함된 단체지정은 에러가 발생된다.")
    @Test
    void exception_createTableGroup_existEmptyOrderTable() {
        // given
        OrderTable 치킨_주문_단체테이블 = OrderTable.of(0, true);
        OrderTable 치킨2_주문_단체테이블 = OrderTable.of(10, false);

        // when
        // then
        Assertions.assertThatExceptionOfType(NotEmptyOrderTableException.class)
                    .isThrownBy(() -> TableGroup.of(OrderTables.of(List.of(치킨_주문_단체테이블, 치킨2_주문_단체테이블))));
    }
}
