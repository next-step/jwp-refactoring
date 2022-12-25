package kitchenpos.table.domain;

import static kitchenpos.order.OrderFixture.주문항목;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    OrderTable 일번조리중테이블;
    OrderTable 이번조리중테이블;
    OrderTable 일번식사중테이블;
    OrderTable 이번식사중테이블;
    OrderTable 일번빈테이블;
    OrderTable 이번빈테이블;
    OrderTable 주문테이블;

    @BeforeEach
    void setup() {
        OrderTable 일번테이블 = new OrderTable(1L, 0, false);
        Order 조리중 = new Order(1L, 일번테이블, COOKING, null, Collections.singletonList(주문항목));
        Order 식사중 = new Order(1L, 일번테이블, MEAL, null, Collections.singletonList(주문항목));
        일번조리중테이블 = new OrderTable(1L, 0, true);
        이번조리중테이블 = new OrderTable(2L, 0, true);
        일번식사중테이블 = new OrderTable(1L, 0, true);
        이번식사중테이블 = new OrderTable(2L, 0, true);
        일번빈테이블 = new OrderTable(1L, 0, true);
        이번빈테이블 = new OrderTable(2L, 0, true);
        주문테이블 = new OrderTable(1L, 0, false);
    }

    @Test
    @DisplayName("단체 지정 성공")
    void createTableGroup() {
        //when
        TableGroup tableGroup = new TableGroup(1L);
    }

    @Test
    @DisplayName("단체 지정 해제")
    void ungroup() {
        //given
        TableGroup tableGroup = new TableGroup(1L);

        //when
        tableGroup.ungroup();
    }

}
