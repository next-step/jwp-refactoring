package kitchenpos.domain.table;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.vo.TableGroupId;

public class OrderTableDomainTest {
    @DisplayName("주문테이블이 그룹이 해제된다.")
    @Test
    void unGroup_ordertable() {
        // given
        OrderTable 주문테이블1 = OrderTable.of(0, true);
        OrderTable 주문테이블2 = OrderTable.of(0, true);

        TableGroup 치킨_주문_단체테이블 = TableGroup.of(1L);       
        주문테이블1.groupingTable(TableGroupId.of(치킨_주문_단체테이블));
        주문테이블2.groupingTable(TableGroupId.of(치킨_주문_단체테이블));

        // when
        주문테이블1.unGroupTable();

        // then
        Assertions.assertThat(주문테이블1.getTableGroupId()).isNull();
    }

    @DisplayName("주문테이블이 그룹화 된다.")
    @Test
    void group_ordertable() {
        // given
        OrderTable 주문테이블1 = OrderTable.of(0, true);
        TableGroup 치킨_주문_단체테이블 = TableGroup.of(1L);       

        // when
        주문테이블1.groupingTable(TableGroupId.of(치킨_주문_단체테이블.getId()));

        // then
        Assertions.assertThat(주문테이블1.getTableGroupId().value()).isEqualTo(치킨_주문_단체테이블.getId());
    }

    @DisplayName("주문테이블이 상태변경된다.")
    @Test
    void change_updateOrderTable_EmptyStatus() {
        // given
        OrderTable 주문테이블1 = OrderTable.of(10, false);
        
        // when
        주문테이블1.changeEmpty(true);

        // then
        Assertions.assertThat(주문테이블1.getEmpty()).isTrue();
    }

    @DisplayName("주문테이블의 고객수가 변경된다.")
    @Test
    void change_numberOfGuests() {
        // given
        OrderTable 치킨_주문_단체테이블 = OrderTable.of(10, false);

        // when
        치킨_주문_단체테이블.changeNumberOfGuests(3);

        // then
        Assertions.assertThat(치킨_주문_단체테이블.getNumberOfGuests()).isEqualTo(3);
        
    }
}
