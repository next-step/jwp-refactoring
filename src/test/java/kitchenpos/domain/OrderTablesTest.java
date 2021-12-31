package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문테이블들 도메인 테스트")
class OrderTablesTest {
    @DisplayName("테이블 그룹을 해제한다")
    @Test
    void unGroupTest() {
        final TableGroup 테이블그룹 = TableGroup.from(1L);
        final OrderTable 주문테이블 = OrderTable.of(테이블그룹, 4, false);
        final OrderTables 주문테이블들 = OrderTables.from(Collections.singletonList(주문테이블));

        주문테이블들.unGroup();

        assertThat(주문테이블.getTableGroup()).isNull();
    }
}
