package kitchenpos.table.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 테이블들 테스트")
class OrderTablesTest {

    private OrderTable 테이블1;
    private OrderTable 테이블2;

    @BeforeEach
    public void setUp() {
        테이블1 = OrderTable.of(1L, Empty.of(false));
        테이블2 = OrderTable.of(2L, Empty.of(false));
    }

    @DisplayName("주문 테이블들 생성 성공 테스트")
    @Test
    void instantiate_success() {
        // when
        OrderTables orderTables = OrderTables.of(Arrays.asList(테이블1));

        // then
        assertAll(
                () -> assertThat(orderTables).isNotNull()
                , () -> assertThat(orderTables.getOrderTables()).isEqualTo(Arrays.asList(테이블1))
        );
    }

    @DisplayName("주문 테이블들 ID 목록 조회 성공 테스트")
    @Test
    void getOrderTableIds() {
        // given
        OrderTables orderTables = OrderTables.of(Arrays.asList(테이블1, 테이블2));

        // when
        List<Long> orderTableIds = orderTables.getOrderTableIds();

        // then
        assertThat(orderTableIds).containsExactly(테이블1.getId(), 테이블2.getId());
    }
}
