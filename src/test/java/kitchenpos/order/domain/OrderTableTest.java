package kitchenpos.order.domain;

import static kitchenpos.order.OrderFixture.주문항목;
import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static kitchenpos.table.TableFixture.일번테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {
    @Test
    @DisplayName("조리중이거나 식사중일떄는 테이블을 빈 상태로 변경 불가능")
    void cantEmptyWhenCookingOrMeal(){
        //given
        Order 식사중 = new Order(1L, 일번테이블, MEAL.name(), null, Collections.singletonList(주문항목));
        OrderTable 식사중테이블 = new OrderTable(1L, null, 0, false, Collections.singletonList(식사중));

        //when & then
        assertThatThrownBy(() -> 식사중테이블.changeEmpty(true))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("조리중 이거나 식사중에는 테이블을 비울 수 없습니다.");

        //given
        Order 조리중 = new Order(1L, 일번테이블, COOKING.name(), null, Collections.singletonList(주문항목));
        OrderTable 조리중테이블 = new OrderTable(1L, null, 0, false, Collections.singletonList(조리중));

        //when & then
        assertThatThrownBy(() -> 조리중테이블.changeEmpty(true))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("조리중 이거나 식사중에는 테이블을 비울 수 없습니다.");
    }

    @Test
    @DisplayName("테이블의 손님수는 음수가 될수 없다.")
    void noNumberOfGuestLessThanZero(){
        //given
        Order 식사중 = new Order(1L, 일번테이블, MEAL.name(), null, Collections.singletonList(주문항목));
        OrderTable 식사중테이블 = new OrderTable(1L, null, 0, false, Collections.singletonList(식사중));

        //when & then
        assertThatThrownBy(() -> 식사중테이블.changeNumberOfGuest(-1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("손님의 수는 0명 이하일 수 없습니다.");
    }

    @Test
    @DisplayName("빈 테이블은 손님 수를 변경할 수 없다.")
    void emptyTableChangeNumberOfGuestException(){
        //given
        OrderTable 빈테이블 = new OrderTable(1L, null, 0, true, Collections.emptyList());

        //when & then
        assertThatThrownBy(() -> 빈테이블.changeNumberOfGuest(2))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("빈 테이블의 손님 수를 변경할 수 없습니다.");
    }

    @Test
    @DisplayName("손님 수 변경")
    void changeNumberOfGuest(){
        //given
        Order 식사중 = new Order(1L, 일번테이블, MEAL.name(), null, Collections.singletonList(주문항목));
        OrderTable 식사중테이블 = new OrderTable(1L, null, 0, false, Collections.singletonList(식사중));

        //when
        식사중테이블.changeNumberOfGuest(5);

        //then
        assertThat(식사중테이블.getNumberOfGuests()).isEqualTo(5);
    }
}
