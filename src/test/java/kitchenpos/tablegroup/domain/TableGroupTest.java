package kitchenpos.tablegroup.domain;

import kitchenpos.ordertable.domain.OrderTable;
import tablegroup.domain.OrderTables;
import tablegroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static kitchenpos.common.Message.ERROR_ORDERTABLES_SHOULD_HAVE_AT_LEAST_TWO_TABLES;
import static kitchenpos.common.Message.ERROR_TABLES_CANNOT_BE_GROUPED;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableGroupTest {

//    private final boolean 비어있음 = true;
//    private final boolean 비어있지_않음 = false;
//    private final OrderTable 단체지정_안된_비어있는_테이블 = new OrderTable(3, 비어있음);
//    private final OrderTable 단체지정_안된_비어있지_않은_테이블 = new OrderTable(3, 비어있지_않음);
//    private final OrderTable 단체지정_된_비어있는_테이블 = new OrderTable(1L, 3, 비어있음);
//
//    @DisplayName("단체지정시 주문 테이블 목록이 비어있는 경우, 예외가 발생한다")
//    @Test
//    void 주문테이블_목록_비어있는_경우_예외발생() {
//        OrderTables orderTables = new OrderTables(Collections.EMPTY_LIST);
//        assertThatThrownBy(() -> new TableGroup(orderTables))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage(ERROR_ORDERTABLES_SHOULD_HAVE_AT_LEAST_TWO_TABLES.showText());
//    }
//
//    @DisplayName("단체지정시 주문 테이블 목록이 2개 미만인 경우, 예외가 발생한다")
//    @Test
//    void 주문테이블_목록_2개_미만_예외발생() {
//        assertThatThrownBy(() -> new TableGroup(new OrderTables(Arrays.asList(단체지정_안된_비어있는_테이블))))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage(ERROR_ORDERTABLES_SHOULD_HAVE_AT_LEAST_TWO_TABLES.showText());
//    }
//
//    @DisplayName("이미 단체 지정된 주문 테이블을 입력한 경우, 예외가 발생한다")
//    @Test
//    void 주문테이블이_이미_단체지정된_경우_예외발생() {
//        assertThatThrownBy(() -> new TableGroup(new OrderTables(Arrays.asList(단체지정_안된_비어있는_테이블, 단체지정_된_비어있는_테이블))))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage(ERROR_TABLES_CANNOT_BE_GROUPED.showText());
//    }
//
//    @DisplayName("주문 테이블이 비어있지 않은 경우, 예외가 발생한다")
//    @Test
//    void 주문테이블이_비어있지_않은_경우_예외발생() {
//        assertThatThrownBy(() -> new TableGroup(new OrderTables(Arrays.asList(단체지정_안된_비어있는_테이블, 단체지정_안된_비어있지_않은_테이블))))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage(ERROR_TABLES_CANNOT_BE_GROUPED.showText());
//    }
}
