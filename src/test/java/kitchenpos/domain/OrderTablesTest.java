package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 테이블 컬렉션 단위 테스트")
class OrderTablesTest {

    @Test
    @DisplayName("2개 미만의 테이블 입력 시 생성 실패")
    void create_failed() {
        assertThatThrownBy(() -> OrderTables.of(new OrderTable()))
            .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    void ungroup() {
        OrderTable 테이블1 = new OrderTable(1L, NumberOfGuests.of(0), true);
        OrderTable 테이블2 = new OrderTable(2L, NumberOfGuests.of(0), true);
        OrderTable 테이블3 = new OrderTable(3L, NumberOfGuests.of(0), true);
        TableGroup 그룹 = new TableGroup(OrderTables.of(테이블1, 테이블2, 테이블3));

        assertThat(테이블1.getTableGroup().isPresent()).isTrue();
        assertThat(테이블1.getTableGroup().isPresent()).isTrue();
        assertThat(테이블1.getTableGroup().isPresent()).isTrue();

        그룹.ungroup();
        assertThat(테이블1.getTableGroup().isPresent()).isFalse();
        assertThat(테이블1.getTableGroup().isPresent()).isFalse();
        assertThat(테이블1.getTableGroup().isPresent()).isFalse();
    }
}
