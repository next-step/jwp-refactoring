package kitchenpos.table.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static kitchenpos.table.domain.OrderTables.*;
import static kitchenpos.table.domain.fixture.OrderTableFixture.*;
import static kitchenpos.table.domain.fixture.TableGroupFixture.tableGroupA;
import static org.assertj.core.api.Assertions.*;

@DisplayName("주문 테이블 일급 콜렉션")
class OrderTablesTest {

    @DisplayName("주문 테이블 일급 콜렉션을 생성한다.")
    @Test
    void create() {
        Assertions.assertThatNoException().isThrownBy(OrderTables::new);
    }

    @DisplayName("주문 테이블들이 empty 일 수 없다.")
    @Test
    void create_notEmpty() {
        Assertions.assertThatThrownBy(() -> new OrderTables(Arrays.asList(notEmptyOrderTable(), emptyOrderTable())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_TABLE_EMPTY_EXCEPTION_MESSAGE);
    }

    @DisplayName("주문 테이블의 갯수가 2보다 작을 수 없다.")
    @Test
    void create_orderTableMinimumSize() {
        Assertions.assertThatThrownBy(() -> new OrderTables(Collections.singletonList(emptyOrderTable())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_TABLE_MINIMUM_SIZE_EXCEPTION_MESSAGE);
    }

    @DisplayName("주문 테이블의 상태가 empty 가 아니면 안된다.")
    @Test
    void create_empty() {
        Assertions.assertThatThrownBy(() -> new OrderTables(Arrays.asList(notEmptyOrderTable(), notEmptyOrderTable())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_TABLE_EMPTY_EXCEPTION_MESSAGE);
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void upGroup() {
        //given
        OrderTables orderTables = new OrderTables();

        for (OrderTable orderTable : orderTables.getOrderTables()) {
            assertThat(orderTable.getTableGroup()).isNotNull();
        }

        //when
        orderTables.unGroup();

        //then
        for (OrderTable orderTable : orderTables.getOrderTables()) {
            assertThat(orderTable.getTableGroup()).isNull();
        }
    }

    @DisplayName("주문 테이블 아이디들을 반환한다.")
    @Test
    void orderTablesIds() {
        OrderTables orderTables = new OrderTables(Arrays.asList(emptyOrderTable(), emptyOrderTable()));
        assertThat(orderTables.getOrderTableIds()).hasSize(2);
    }

    @DisplayName("테이블 그룹을 매핑한다.")
    @Test
    void name() {
        TableGroup tableGroup = tableGroupA();
        OrderTables orderTables = new OrderTables(Arrays.asList(emptyOrderTable(), emptyOrderTable()));
        //when
        orderTables.mapTableGroup(tableGroup);
        //then
        for (OrderTable orderTable : orderTables.getOrderTables()) {
            assertThat(orderTable.getTableGroup().getCreatedDate()).isEqualTo(tableGroup.getCreatedDate());
        }
    }
}
