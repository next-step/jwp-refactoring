package kitchenpos.domain;

import kitchenpos.exception.InvalidTableGroupException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class TableGroupTest {
    @DisplayName("오더테이블이 없거나 오더테이블이 하나인 테이블 그룹은 등록할 수 없다.")
    @Test
    void create() {
        // given
        OrderTable orderTable = OrderTable.of(4, false);

        //then
        assertAll(
                () -> assertThatThrownBy(
                        () -> TableGroup.of(1L, null, Arrays.asList(orderTable))
                ).isInstanceOf(InvalidTableGroupException.class),
                () -> assertThatThrownBy(
                        () -> TableGroup.of(1L, null, new ArrayList<>())
                ).isInstanceOf(InvalidTableGroupException.class)
        );
    }

    @DisplayName("오더상태가 조리중이거나 요리중인 주문테이블에 테이블 그룹을 해제할 수 없다.")
    @Test
    void ungroup() {
        // given
        OrderTable orderTable = OrderTable.of(1L, null, 4, false);
        OrderTable orderTable2 = OrderTable.of(2L, null, 4, false);
        OrderLineItem orderLineItem = OrderLineItem.of(1L, null, null, 1);
        Order.of(orderTable, OrderStatus.COOKING, Arrays.asList(orderLineItem));
        TableGroup tableGroup = TableGroup.of(1L, null, Arrays.asList(orderTable, orderTable2));

        // then
        assertThatThrownBy(
                () -> tableGroup.validateUngroup()
        ).isInstanceOf(InvalidTableGroupException.class);
    }
}
