package kitchenpos.table.domain;

import kitchenpos.table.domain.exception.UngroupTableException;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class OrderTablesTest {

    @DisplayName("그룹화 요청 테이블과 실제 테이블이 다를 경우 예외를 발생시킨다.")
    @Test
    void create() {
        //when
        assertThatThrownBy(() -> OrderTables.create(Lists.list(OrderTable.of(4, false)), 3))
                .isInstanceOf(UngroupTableException.class); //then
    }

    @DisplayName("테이블 그룹을 지으려면 모든 테이블이 주문 불가능 상태여야한다.")
    @Test
    void validateTableGroupable() {
        //given
        OrderTable orderTable1 = OrderTable.of(4, true);
        OrderTable orderTable2 = OrderTable.of(2, false);
        OrderTable orderTable3 = OrderTable.of(3, true);

        //when
        Assertions.assertThatThrownBy(() -> OrderTables.create(Lists.list(orderTable1, orderTable2, orderTable3), 3))
                .isInstanceOf(UngroupTableException.class); //then
    }
}
