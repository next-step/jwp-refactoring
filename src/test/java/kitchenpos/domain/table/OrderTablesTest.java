package kitchenpos.domain.table;

import static kitchenpos.utils.generator.OrderTableFixtureGenerator.비어있는_주문_테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Domain:OrderTables")
class OrderTablesTest {

    private OrderTable 첫번째_테이블, 두번째_테이블;
    private List<OrderTable> 비어있는_테이블_목록;
    private List<Long> 비어있는_테이블_번호_목록;
    private TableGroup 단체석;

    @BeforeEach
    void setUp() {
        첫번째_테이블 = 비어있는_주문_테이블_생성();
        두번째_테이블 = 비어있는_주문_테이블_생성();
        비어있는_테이블_목록 = Arrays.asList(첫번째_테이블, 두번째_테이블);
        비어있는_테이블_번호_목록 = Stream.of(첫번째_테이블, 두번째_테이블)
            .map(OrderTable::getId)
            .collect(Collectors.toList());
        단체석 = TableGroup.of(비어있는_테이블_번호_목록, 비어있는_테이블_목록);
    }

    @Test
    @DisplayName("대상 주문 테이블을 단체석으로 지정한다.")
    public void allocateAll() {
        // Given
        OrderTables orderTables = new OrderTables(비어있는_테이블_목록);

        // When
        orderTables.allocateAll(단체석);

        // Then
        assertThat(비어있는_테이블_목록)
            .extracting(OrderTable::isGroupTable)
            .containsOnly(true);
    }

    @Test
    @DisplayName("단체석이 지정된 주문 테이블을 단체석에서 해제한다.")
    public void deallocateAll() {
        // Given
        OrderTables orderTables = new OrderTables(비어있는_테이블_목록);
        orderTables.allocateAll(단체석);

        // When
        orderTables.deallocateAll();

        // Then
        assertThat(비어있는_테이블_목록)
            .extracting(OrderTable::isGroupTable)
            .containsOnly(false);
    }
}
