package kitchenpos.table.domain;

import static kitchenpos.table.application.TableServiceTest.두명;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.exception.ExistAssignedTableGroupException;
import kitchenpos.table.exception.ExistNonEmptyOrderTableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단체지정 테스트")
public class TableGroupTest {

    public static final OrderTable 첫번째_주문테이블 = new OrderTable(1L, 두명);
    public static final OrderTable 두번째_주문테이블 = new OrderTable(1L, 두명);
    public static final List<OrderTable> 주문테이블_목록 = new ArrayList<>(Arrays.asList(첫번째_주문테이블, 두번째_주문테이블));

    @DisplayName("단체 지정될 주문테이블들은 모두 빈 테이블이어야 한다.")
    @Test
    void create_Fail_01() {
        // Given
        List<OrderTable> 비어있지않은_주문테이블_목록 = 주문테이블_목록.stream()
            .peek(orderTable -> orderTable.changeEmpty(false))
            .collect(Collectors.toList());

        // When & Then
        assertThatThrownBy(() -> new TableGroup(1L, 비어있지않은_주문테이블_목록))
            .isInstanceOf(ExistNonEmptyOrderTableException.class);
    }

    @DisplayName("단체 지정될 주문테이블들은 다른 단체로 지정되어있지 않아야 한다.")
    @Test
    void create_Fail_02() {
        // Given
        Long 다른_주문_테이블_ID = 123L;
        OrderTable 단체지정된_첫번째_주문테이블 = new OrderTable(1L, 다른_주문_테이블_ID, 두명);
        OrderTable 단체지정된_두번째_주문테이블 = new OrderTable(2L, 다른_주문_테이블_ID, 두명);
        List<OrderTable> 다른_단체_지정되어있는_주문테이블_목록 = new ArrayList<>(Arrays.asList(단체지정된_첫번째_주문테이블, 단체지정된_두번째_주문테이블));

        // When & Then
        assertThatThrownBy(() -> new TableGroup(1L, 다른_단체_지정되어있는_주문테이블_목록))
            .isInstanceOf(ExistAssignedTableGroupException.class);
    }

}
