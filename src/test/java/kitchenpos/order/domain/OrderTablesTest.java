package kitchenpos.order.domain;

import static kitchenpos.order.OrderFixture.주문항목;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTablesTest {

    OrderTable 일번조리중테이블;
    OrderTable 이번조리중테이블;
    OrderTable 일번식사중테이블;
    OrderTable 이번식사중테이블;
    OrderTable 일번빈테이블;
    OrderTable 이번빈테이블;
    OrderTable 주문테이블;

    @BeforeEach
    void setup() {
        OrderTable 일번테이블 = new OrderTable(1L, 0, false, Collections.emptyList());
        Order 조리중 = new Order(1L, 일번테이블, COOKING, null, Collections.singletonList(주문항목));
        Order 식사중 = new Order(1L, 일번테이블, MEAL, null, Collections.singletonList(주문항목));
        일번조리중테이블 = new OrderTable(1L, 0, true, Collections.singletonList(조리중));
        이번조리중테이블 = new OrderTable(2L, 0, true, Collections.singletonList(조리중));
        일번식사중테이블 = new OrderTable(1L, 0, true, Collections.singletonList(식사중));
        이번식사중테이블 = new OrderTable(2L, 0, true, Collections.singletonList(식사중));
        일번빈테이블 = new OrderTable(1L, 0, true, Collections.emptyList());
        이번빈테이블 = new OrderTable(2L, 0, true, Collections.emptyList());
        주문테이블 = new OrderTable(1L, 0, false, Collections.emptyList());
    }

    @Test
    @DisplayName("주문 테이블들 생성")
    void createOrderTables() {
        //when
        OrderTables orderTables = new OrderTables(Arrays.asList(일번빈테이블, 이번빈테이블));

        //then
        assertThat(orderTables.getOrderTables())
            .hasSize(2)
            .extracting(OrderTable::isEmpty)
            .containsExactly(false, false);
    }

    @Test
    @DisplayName("단체 지정할 테이블이 없거나 단체 지정 할 테이블 2개 미만시 에러 발생")
    void validateOrderTable() {
        //when & then
        assertThatThrownBy(() -> new OrderTables(Arrays.asList(일번빈테이블)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("단체 지정할 테이블이 없거나 단체 지정 할 테이블 2개 미만 입니다.");

        //when & then
        assertThatThrownBy(() -> new OrderTables(Arrays.asList(일번빈테이블, 주문테이블)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("테이블이 비어있지 않거나, 이미 단체 지정된 테이블 입니다.");

    }
}