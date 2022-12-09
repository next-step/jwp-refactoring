package kitchenpos.table.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static kitchenpos.table.domain.TableGroup.ORDER_TABLE_NOT_EMPTY_EXCEPTION_MESSAGE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("테이블 그룹")
class TableGroupTest {

//    @DisplayName("테이블 그룹을 생성한다.")
//    @Test
//    void create() {
//        List<OrderTable> orderTables = new ArrayList<>();
//        assertThatNoException().isThrownBy(() -> new TableGroup(orderTables));
//    }

    @DisplayName("주문 테이블이 비어있을 수 없다.")
    @Test
    void create() {
        List<OrderTable> orderTables = new ArrayList<>();
        assertThatThrownBy(() -> new TableGroup(orderTables))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_TABLE_NOT_EMPTY_EXCEPTION_MESSAGE);
    }
}