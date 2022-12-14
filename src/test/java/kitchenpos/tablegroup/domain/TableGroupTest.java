package kitchenpos.tablegroup.domain;

import java.util.Arrays;
import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단체 지정 테스트")
class TableGroupTest {

    private List<OrderTable> 주문_테이블_목록1;
    private List<OrderTable> 주문_테이블_목록2;

    @BeforeEach
    void setUp() {
        주문_테이블_목록1 = Arrays.asList(
                OrderTable.of(1L, 3, true),
                OrderTable.of(2L, 4, true)
        );

        주문_테이블_목록2 = Arrays.asList(
                OrderTable.of(1L, 3, true),
                OrderTable.of(2L, 4, true)
        );
    }

    @DisplayName("id가 같은 두 객체는 동등하다.")
    @Test
    void equalsTest() {
        TableGroup tableGroup1 = TableGroup.of(1L, 주문_테이블_목록1);
        TableGroup tableGroup2 = TableGroup.of(1L, 주문_테이블_목록2);

        Assertions.assertThat(tableGroup1).isEqualTo(tableGroup2);
    }

    @DisplayName("id가 다르면 두 객체는 동등하지 않다.")
    @Test
    void equalsTest2() {
        TableGroup tableGroup1 = TableGroup.of(1L, 주문_테이블_목록1);
        TableGroup tableGroup2 = TableGroup.of(2L, 주문_테이블_목록2);

        Assertions.assertThat(tableGroup1).isNotEqualTo(tableGroup2);
    }
}
