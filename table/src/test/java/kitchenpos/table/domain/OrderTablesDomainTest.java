package kitchenpos.table.domain;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTablesDomainTest {
    

    @DisplayName("단체지정ID로 테이블를 그룹화시 주문테이블들을 단체지정 ID로 그룹화된다.")
    @Test
    void event_groupingOrderTable() {
        // given
        OrderTable 주문테이블 = OrderTable.of(0, true);
        OrderTable 주문테이블2 = OrderTable.of(0, true);
        OrderTables 주문테이블들 = OrderTables.of(List.of(주문테이블, 주문테이블2));
        
        
        // when
        주문테이블들.groupingTable(TableGroupId.of(1L));

        // then
        Assertions.assertThat(주문테이블.getTableGroupId()).isEqualTo(TableGroupId.of(1L));
        Assertions.assertThat(주문테이블2.getTableGroupId()).isEqualTo(TableGroupId.of(1L));
    }


    @DisplayName("단체지정된 테이블에대해 그룹 해제시 주문테이블들의 단체지정Id가 삭제된다.")
    @Test
    void event_ungroupOrderTable() {
        // given
        OrderTable 주문테이블 = OrderTable.of(0, true);
        OrderTable 주문테이블2 = OrderTable.of(0, true);
        OrderTables 주문테이블들 = OrderTables.of(List.of(주문테이블, 주문테이블2));
        주문테이블들.groupingTable(TableGroupId.of(1L));
        
        
        // when
        주문테이블들.unGroupTable();

        // then
        Assertions.assertThat(주문테이블.getTableGroupId()).isNull();
        Assertions.assertThat(주문테이블2.getTableGroupId()).isNull();
    }
}
