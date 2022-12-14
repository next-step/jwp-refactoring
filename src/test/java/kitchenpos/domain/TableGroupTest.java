package kitchenpos.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

@DisplayName("단체 지정 테스트")
class TableGroupTest {

    private List<OrderTable> 주문_테이블_목록;

    @BeforeEach
    void setUp() {
        주문_테이블_목록 = Arrays.asList(
                OrderTable.of(1L, 3, false),
                OrderTable.of(2L, 4, false)
        );
    }

    @DisplayName("id가 같은 두 객체는 같다.")
    @Test
    void equalsTest() {
        TableGroup tableGroup1 = TableGroup.of(1L, 주문_테이블_목록);
        TableGroup tableGroup2 = TableGroup.of(1L, 주문_테이블_목록);

        Assertions.assertThat(tableGroup1).isEqualTo(tableGroup2);
    }

    @DisplayName("id가 다르면 두 객체는 다르다.")
    @Test
    void equalsTest2() {
        TableGroup tableGroup1 = TableGroup.of(1L, 주문_테이블_목록);
        TableGroup tableGroup2 = TableGroup.of(2L, 주문_테이블_목록);

        Assertions.assertThat(tableGroup1).isNotEqualTo(tableGroup2);
    }
}
