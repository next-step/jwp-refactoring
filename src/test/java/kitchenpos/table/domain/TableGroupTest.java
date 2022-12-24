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
    @DisplayName("단체 지정 성공")
    void createTableGroup() {
        //when
        TableGroup tableGroup = new TableGroup(1L, Arrays.asList(일번빈테이블, 이번빈테이블));

        //then
        assertThat(tableGroup.getOrderTables())
            .hasSize(2)
            .extracting(OrderTable::isEmpty)
            .containsExactly(false, false);
    }

    @Test
    @DisplayName("단체 지정 해제")
    void ungroup() {
        //given
        TableGroup tableGroup = new TableGroup(1L, Arrays.asList(일번빈테이블, 이번빈테이블));

        //when
        tableGroup.ungroup();

        //then
        assertThat(tableGroup.getOrderTables())
            .hasSize(2)
            .extracting(OrderTable::getTableGroupId)
            .containsExactly(null, null);
    }

    @Test
    @DisplayName("단체 지정 해제할 대상 테이블의 주문 상태가 조리중 이거나 식사중 이라면 해제 불가능")
    void cannotUngroupWhenOrderOnMealOrCooking() {
        //given
        TableGroup 조리중테이블그룹 = new TableGroup(1L, Arrays.asList(일번조리중테이블, 이번조리중테이블));

        //when & then
        assertThatThrownBy(조리중테이블그룹::ungroup)
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("조리중이거나 식사중에는 단체 지정해제할 수 없습니다.");

        //given
        TableGroup 식사중테이블그룹 = new TableGroup(1L, Arrays.asList(일번식사중테이블, 이번식사중테이블));

        //when & then
        assertThatThrownBy(식사중테이블그룹::ungroup)
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("조리중이거나 식사중에는 단체 지정해제할 수 없습니다.");
    }

    @Test
    @DisplayName("단체 지정할 테이블이 없거나 2 미만일 경우 단체 지정 실패")
    void groupFailWhenTableEmptyOrLessThan2() {
        //when & then
        assertThatThrownBy(() -> new TableGroup(1L, Collections.emptyList()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("단체 지정할 테이블이 없거나 단체 지정 할 테이블 2개 미만 입니다.");

        //when & then
        assertThatThrownBy(() -> new TableGroup(1L, Collections.singletonList(일번빈테이블)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("단체 지정할 테이블이 없거나 단체 지정 할 테이블 2개 미만 입니다.");
    }

    @Test
    @DisplayName("테이블이 비어있지 않거나, 이미 단체 지정된 테이블일 경우 단체 지정 실패")
    void groupFailWhenTableNotEmptyOrAlreadyGrouped() {
        //when & then
        assertThatThrownBy(() -> new TableGroup(1L, Arrays.asList(일번빈테이블, 주문테이블)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("테이블이 비어있지 않거나, 이미 단체 지정된 테이블 입니다.");

        //given
        OrderTable 테이블3 = new OrderTable(3L, 0, true, Collections.emptyList());
        테이블3.setTableGroupId(1L);
        OrderTable 테이블4 = new OrderTable(4L, 0, true, Collections.emptyList());
        테이블4.setTableGroupId(1L);

        //when & then
        assertThatThrownBy(() -> new TableGroup(2L, Arrays.asList(테이블3, 테이블4)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("테이블이 비어있지 않거나, 이미 단체 지정된 테이블 입니다.");
    }

}
