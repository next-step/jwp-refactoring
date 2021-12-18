package kitchenpos.table.domain;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@DisplayName("테이블 목록 테스트")
class OrderTablesTest {

    private OrderTable 일번테이블;

    @BeforeEach
    void setup() {
        일번테이블 = OrderTable.of(4, false);
    }

    @DisplayName("테이블 목록 생성")
    @Test
    void 테이블_목록_생성() {
        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = OrderTables::of;

        // then
        assertThatNoException().isThrownBy(생성_요청);
    }

    @DisplayName("테이블 목록 추가")
    @Test
    void 테이블_목록_추가() {
        // given
        OrderTables 테이블_목록 = OrderTables.of();

        // when
        테이블_목록.add(일번테이블);

        // then
        assertThat(테이블_목록.getOrderTables()).hasSize(1);
    }

    @DisplayName("테이블 목록 그룹제거")
    @Test
    void 테이블_목록_그룹제거() {
        // given
        OrderTables 테이블_목록 = OrderTables.of();
        테이블_목록.add(일번테이블);

        // when
        테이블_목록.ungroup();

        // then
        assertThat(테이블_목록.getOrderTables()).isEmpty();
    }
}
