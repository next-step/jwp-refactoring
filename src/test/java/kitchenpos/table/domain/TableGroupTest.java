package kitchenpos.table.domain;

import static kitchenpos.order.OrderFixture.주문항목;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static kitchenpos.table.TableFixture.일번테이블;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
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
        TableGroup 조리중테이블그룹 = new TableGroup(1L, LocalDateTime.now(), Collections.singletonList(조리중테이블));

        //when & then
        Assertions.assertThatThrownBy(() -> 조리중테이블그룹.ungroup())
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("조리중이거나 식사중에는 단체 지정해제할 수 없습니다.");

        //given
        Order 식사중 = new Order(1L, 일번테이블, MEAL.name(), null, Collections.singletonList(주문항목));
        OrderTable 식사중테이블 = new OrderTable(1L, null, 0, false, Collections.singletonList(식사중));
        TableGroup 식사중테이블그룹 = new TableGroup(1L, LocalDateTime.now(), Collections.singletonList(식사중테이블));

        //when & then
        Assertions.assertThatThrownBy(() -> 식사중테이블그룹.ungroup())
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("조리중이거나 식사중에는 단체 지정해제할 수 없습니다.");
    }

}
