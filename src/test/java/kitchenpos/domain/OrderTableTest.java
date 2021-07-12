package kitchenpos.domain;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.exception.AlreadyHaveTableGroupException;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTableTest {

    @DisplayName("테이블그룹이 Null인지 확인")
    @Test
    void 테이블그룹이_Null인지_확인() {
        //given
        OrderTable orderTable = new OrderTable(1L, new TableGroup(), 4, true);

        //when, then
        assertThatThrownBy(orderTable::validateTableGroupNonNull).isInstanceOf(AlreadyHaveTableGroupException.class);
    }
}
