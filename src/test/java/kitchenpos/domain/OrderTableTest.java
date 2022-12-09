package kitchenpos.domain;

import static kitchenpos.domain.OrderTableTestFixture.generateOrderTable;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 테이블 관련 도메인 테스트")
public class OrderTableTest {

    @DisplayName("주문 테이블의 그룹 상태를 해제한다.")
    @Test
    void ungroupOrderTable() {
        // given
        OrderTable orderTable = generateOrderTable(1L, 1L, 5, true);

        // when
        orderTable.ungroup();

        // then
        assertThat(orderTable.getTableGroupId()).isNull();
    }
}
