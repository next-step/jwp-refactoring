package kitchenpos.table.domain;

import static kitchenpos.order.OrderFixture.주문항목;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static kitchenpos.table.TableFixture.일번테이블;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @Test
    @DisplayName("단체 지정 해제할 대상 테이블의 주문 상태가 조리중 이거나 식사중 이라면 해제 불가능")
    void cannotUngroupWhenOrderOnMealOrCooking(){
        //given
        Order 조리중 = new Order(1L, 일번테이블, COOKING.name(), null, Collections.singletonList(주문항목));
        OrderTable 조리중테이블 = new OrderTable(1L, null, 0, false, Collections.singletonList(조리중));
        OrderTable 조리중테이블2 = new OrderTable(2L, null, 0, false, Collections.singletonList(조리중));
        TableGroup 조리중테이블그룹 = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(조리중테이블, 조리중테이블2));

        //when & then
        assertThatThrownBy(() -> 조리중테이블그룹.ungroup())
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("조리중이거나 식사중에는 단체 지정해제할 수 없습니다.");

        //given
        Order 식사중 = new Order(1L, 일번테이블, MEAL.name(), null, Collections.singletonList(주문항목));
        OrderTable 식사중테이블 = new OrderTable(1L, null, 0, false, Collections.singletonList(식사중));
        OrderTable 식사중테이블2 = new OrderTable(2L, null, 0, false, Collections.singletonList(식사중));
        TableGroup 식사중테이블그룹 = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(식사중테이블, 식사중테이블2));

        //when & then
        assertThatThrownBy(() -> 식사중테이블그룹.ungroup())
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("조리중이거나 식사중에는 단체 지정해제할 수 없습니다.");
    }

    @Test
    @DisplayName("단체 지정 성공")
    void createTableGroup(){
        //given
        OrderTable 테이블 = new OrderTable(1L, null, 0, true, Collections.emptyList());
        OrderTable 테이블2 = new OrderTable(2L, null, 0, true, Collections.emptyList());

        //when & then
        new TableGroup(1L, LocalDateTime.now(), Arrays.asList(테이블, 테이블2));
    }

    @Test
    @DisplayName("단체 지정할 테이블이 없거나 2 미만일 경우 단체 지정 실패")
    void groupFailWhenTableEmptyOrLessThan2(){
        //when & then
        assertThatThrownBy(() -> new TableGroup(1L, LocalDateTime.now(), Collections.emptyList()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("단체 지정할 테이블이 없거나 단체 지정 할 테이블 2개 미만 입니다.");
        //given
        Order 식사중 = new Order(1L, 일번테이블, MEAL.name(), null, Collections.singletonList(주문항목));
        OrderTable 식사중테이블 = new OrderTable(1L, null, 0, false, Collections.singletonList(식사중));

        //when
        assertThatThrownBy(() -> new TableGroup(1L, LocalDateTime.now(), Collections.singletonList(식사중테이블)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("단체 지정할 테이블이 없거나 단체 지정 할 테이블 2개 미만 입니다.");
    }

    @Test
    @DisplayName("테이블이 비어있지 않거나, 이미 단체 지정된 테이블일 경우 단체 지정 실패")
    void groupFailWhenTableNotEmptyOrAlreadyGrouped(){
        //given
        OrderTable 테이블 = new OrderTable(1L, null, 0, false, Collections.emptyList());
        OrderTable 테이블2 = new OrderTable(2L, null, 0, true, Collections.emptyList());

        //when & then
        assertThatThrownBy(() -> new TableGroup(1L, LocalDateTime.now(), Arrays.asList(테이블, 테이블2)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("테이블이 비어있지 않거나, 이미 단체 지정된 테이블 입니다.");

        //given
        OrderTable 테이블3 = new OrderTable(3L, 1L, 0, true, Collections.emptyList());
        OrderTable 테이블4 = new OrderTable(4L, 1L, 0, true, Collections.emptyList());

        //when & then
        assertThatThrownBy(() -> new TableGroup(2L, LocalDateTime.now(), Arrays.asList(테이블3, 테이블4)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("테이블이 비어있지 않거나, 이미 단체 지정된 테이블 입니다.");
    }

}
