package kitchenpos.tablegroup.dto;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("테이블 그룹 반환 객체 테스트")
class TableGroupResponseTest {

    private OrderTable 일번_테이블;
    private OrderTable 이번_테이블;
    private TableGroup 테이블_그룹;

    @BeforeEach
    void setUp() {
        테이블_그룹 = new TableGroup(1L, LocalDateTime.now());
        일번_테이블 = new OrderTable(1L,null, 0, true);
        이번_테이블 = new OrderTable(2L, null, 0, true);
        일번_테이블.withTableGroup(1L);
        이번_테이블.withTableGroup(1L);
    }

    @Test
    void 주문_테이블_일급_컬렉션_객체와_테이블_그룹_entity를_이용하여_테이블_그룹_반환_객체_생성() {
        OrderTables orderTables = new OrderTables(Arrays.asList(일번_테이블, 이번_테이블));
        TableGroupResponse tableGroupResponse = TableGroupResponse.of(테이블_그룹, orderTables);
        assertThat(tableGroupResponse.getId()).isEqualTo(테이블_그룹.getId());
        assertThat(tableGroupResponse.getCreatedDate()).isEqualTo(테이블_그룹.getCreatedDate());
        assertThat(tableGroupResponse.getOrderTableResponses().size()).isEqualTo(2);
    }
}
