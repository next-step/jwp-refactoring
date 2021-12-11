package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class OrderTablesTest {

    private static final OrderTables orderTables = new OrderTables(Arrays.asList(new OrderTable(null, 10, false)));

    @Test
    void check_요청한_주문_테이블_목록이_비어있으면_에러를_발생한다() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderTables.check(Arrays.asList()));
    }

    @Test
    void check_요청한_주문_테이블_목록이_2개_미만이면_에러를_발생한다() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderTables.check(Arrays.asList(1L)));
    }

    @Test
    void check_요청한_주문_테이블_목록과_저장된_주문_테이블_목록의_크기가_다르면_에러를_발생한다() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderTables.check(Arrays.asList(1L, 2L)));
    }

    @Test
    void check_저장된_주문_테이블이_빈_테이블이면_에러를_발생한다() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderTables.check(Arrays.asList(1L)));
    }

    @Test
    void check_저장된_주문_테이블의_테이블_그룹에_속하지_않으면_에러를_발생한다() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderTables.check(Arrays.asList(1L)));
    }
}