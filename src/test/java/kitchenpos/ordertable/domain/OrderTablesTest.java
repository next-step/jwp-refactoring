package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.generic.exception.NotEnoughTablesException;
import kitchenpos.generic.guests.domain.NumberOfGuests;
import kitchenpos.tablegroup.domain.TableGroup;

@DisplayName("주문 테이블 컬렉션 단위 테스트")
class OrderTablesTest {

    @Test
    @DisplayName("2개 미만의 테이블 입력 시 생성 실패")
    void create_failed() {
        assertThatThrownBy(() -> OrderTables.of(new OrderTable()))
            .isInstanceOf(NotEnoughTablesException.class);
    }


    @Test
    @DisplayName("그룹 해제 후 각 테이블 결과 검증")
    void ungroup() {
        OrderTable 테이블1 = new OrderTable(1L, NumberOfGuests.of(0), true);
        OrderTable 테이블2 = new OrderTable(2L, NumberOfGuests.of(0), true);
        OrderTable 테이블3 = new OrderTable(3L, NumberOfGuests.of(0), true);
        TableGroup 그룹 = new TableGroup(OrderTables.of(테이블1, 테이블2, 테이블3));

        assertThat(테이블1.getTableGroup()).isPresent();
        assertThat(테이블1.getTableGroup()).isPresent();
        assertThat(테이블1.getTableGroup()).isPresent();

        그룹.ungroup();
        assertThat(테이블1.getTableGroup()).isNotPresent();
        assertThat(테이블1.getTableGroup()).isNotPresent();
        assertThat(테이블1.getTableGroup()).isNotPresent();
    }
}
