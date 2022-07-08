package kitchenpos.table.domain;

import static kitchenpos.table.__fixture__.OrderTableTestFixture.주문_테이블_생성;
import static kitchenpos.table.__fixture__.TableGroupTestFixture.테이블_그룹_생성;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TableGroupTest {
    @Test
    @DisplayName("주문 테이블이 비어있으면 Exception")
    void checkOrderTablesEmpty() {
        final TableGroup 테이블_그룹 = 테이블_그룹_생성(1L, LocalDateTime.now(), Collections.emptyList());

        assertThatThrownBy(() -> 테이블_그룹.checkOrderTablesEmptyOrSizeOne())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 2개 미만이면 Exception")
    void checkOrderTablesSizeLessThanTwo() {
        final OrderTable 주문_테이블 = 주문_테이블_생성(1L, null, 4, false);
        final TableGroup 테이블_그룹 = 테이블_그룹_생성(1L, LocalDateTime.now(), Arrays.asList(주문_테이블));

        assertThatThrownBy(() -> 테이블_그룹.checkOrderTablesEmptyOrSizeOne())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("저장된 주문테이블과 요청 주문테이블의 갯수가 다르면 Exception")
    void checkOrderTablesSizeEqualTo() {
        final OrderTable 주문_테이블 = 주문_테이블_생성(1L, null, 4, false);
        final OrderTable 주문_테이블2 = 주문_테이블_생성(2L, null, 4, false);
        final OrderTable 저장된_주문_테이블 = 주문_테이블_생성(1L, null, 4, false);
        final TableGroup 테이블_그룹 = 테이블_그룹_생성(1L, LocalDateTime.now(), Arrays.asList(주문_테이블, 주문_테이블2));

        assertThatThrownBy(() -> 테이블_그룹.checkAllOrderSavedOrderTables(Arrays.asList(저장된_주문_테이블)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("저장된 주문테이블이 비어있지 않으면 Exception")
    void checkSavedOrderTablesEmpty() {
        final OrderTable 주문_테이블 = 주문_테이블_생성(1L, null, 4, false);
        final OrderTable 주문_테이블2 = 주문_테이블_생성(2L, null, 4, false);
        final OrderTable 저장된_주문_테이블 = 주문_테이블_생성(1L, null, 4, false);
        final OrderTable 저장된_주문_테이블2 = 주문_테이블_생성(2L, null, 4, false);
        final TableGroup 테이블_그룹 = 테이블_그룹_생성(1L, LocalDateTime.now(), Arrays.asList(주문_테이블, 주문_테이블2));

        assertThatThrownBy(() -> 테이블_그룹.checkAllOrderSavedOrderTables(Arrays.asList(저장된_주문_테이블, 저장된_주문_테이블2)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("저장된 주문테이블이 이미 그룹되어 있으면 Exception")
    void checkSavedOrderTablesAlreadyGrouped() {
        final OrderTable 주문_테이블 = 주문_테이블_생성(1L, null, 4, false);
        final OrderTable 주문_테이블2 = 주문_테이블_생성(2L, null, 4, false);
        final TableGroup 저장된_테이블_그룹 = 테이블_그룹_생성(2L, LocalDateTime.now(), Collections.emptyList());
        final OrderTable 저장된_주문_테이블 = 주문_테이블_생성(1L, 저장된_테이블_그룹, 4, true);
        final OrderTable 저장된_주문_테이블2 = 주문_테이블_생성(2L, 저장된_테이블_그룹, 4, true);
        final TableGroup 테이블_그룹 = 테이블_그룹_생성(1L, LocalDateTime.now(), Arrays.asList(주문_테이블, 주문_테이블2));

        assertThatThrownBy(() -> 테이블_그룹.checkAllOrderSavedOrderTables(Arrays.asList(저장된_주문_테이블, 저장된_주문_테이블2)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
