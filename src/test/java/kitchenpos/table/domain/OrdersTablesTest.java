package kitchenpos.table.domain;

import kitchenpos.exception.OrderTableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 테이블 일급 컬렉션 객체 테스트")
class OrdersTablesTest {

    private OrderTable 일번_테이블;
    private OrderTable 이번_테이블;

    private TableGroup 테이블_그룹;

    @BeforeEach
    void setUp() {
        일번_테이블 = new OrderTable(1L, 0, true);
        이번_테이블 = new OrderTable(2L, 0, true);
        테이블_그룹 = new TableGroup(1L, LocalDateTime.now());
    }

    @Test
    void 주문_테이블_일급_컬렉션_객체의_아이템_갯수가_대상_갯수와_일치하지않을때_에러_발생() {
        OrderTables orderTables = new OrderTables(Arrays.asList(일번_테이블));
        assertThatThrownBy(() -> orderTables.checkValidEqualToRequestSize(Arrays.asList(1L, 2L))).isInstanceOf(OrderTableException.class);
    }

    @Test
    void 그룹핑된_주문_테이블이_존재하는경우_에러_발생() {
        일번_테이블.withTableGroup(테이블_그룹);
        OrderTables orderTables = new OrderTables(Arrays.asList(일번_테이블, 이번_테이블));
        assertThatThrownBy(() -> orderTables.checkValidEmptyTableGroup()).isInstanceOf(OrderTableException.class);
    }

    @Test
    void 주문_테이블_그룹핑_처리() {
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now());
        OrderTables orderTables = new OrderTables(Arrays.asList(일번_테이블, 이번_테이블));
        일번_테이블.withTableGroup(테이블_그룹);
        이번_테이블.withTableGroup(테이블_그룹);
        orderTables.updateGrouping(tableGroup);
        assertThat(orderTables.orderTables().get(0).getTableGroupId()).isEqualTo(1L);
        assertThat(orderTables.orderTables().get(0).isEmpty()).isEqualTo(false);
        assertThat(orderTables.orderTables().get(1).getTableGroupId()).isEqualTo(1L);
        assertThat(orderTables.orderTables().get(1).isEmpty()).isEqualTo(false);
    }

    @Test
    void 소속된_주문_테이블의_아이디_리스트_생성() {
        OrderTables orderTables = new OrderTables(Arrays.asList(일번_테이블, 이번_테이블));
        List<Long> expected = orderTables.generateOrderTableIds();
        assertThat(expected).containsExactly(1L, 2L);
    }

    @Test
    void 소속된_주문_테이블_그룹_해제() {
        OrderTables orderTables = new OrderTables(Arrays.asList(일번_테이블, 이번_테이블));
        일번_테이블.withTableGroup(테이블_그룹);
        이번_테이블.withTableGroup(테이블_그룹);

        orderTables.updateUnGroup();
        assertThat(orderTables.orderTables().get(0).getTableGroupId()).isNull();
        assertThat(orderTables.orderTables().get(1).getTableGroupId()).isNull();
    }
}
