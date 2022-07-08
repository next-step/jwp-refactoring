package kitchenpos.table.domain;

import static kitchenpos.table.__fixture__.OrderTableTestFixture.주문_테이블_리스트_생성;
import static kitchenpos.table.__fixture__.OrderTableTestFixture.주문_테이블_생성;
import static kitchenpos.table.__fixture__.TableGroupTestFixture.테이블_그룹_생성;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTablesTest {
    @Test
    @DisplayName("ungroupAll 시 테이블 그룹 아이디가 모두 null이 된다.")
    void ungropAll() {
        //given
        final TableGroup 테이블_그룹 = 테이블_그룹_생성(1L, LocalDateTime.now(), null);
        final OrderTables 주문_테이블_리스트 = OrderTables.of(주문_테이블_리스트_생성(
                주문_테이블_생성(1L, 테이블_그룹, 4, false),
                주문_테이블_생성(2L, 테이블_그룹, 4, false),
                주문_테이블_생성(3L, 테이블_그룹, 4, false),
                주문_테이블_생성(4L, 테이블_그룹, 4, false)
        ));

        //when
        주문_테이블_리스트.upgroupAll();

        //then
        final List<Long> 테이블_그룹_아이디_리스트 = 주문_테이블_리스트
                .getOrderTables()
                .stream()
                .map(OrderTable::getTableGroupId)
                .collect(Collectors.toList());
        assertThat(테이블_그룹_아이디_리스트).containsOnlyNulls();
    }
}
