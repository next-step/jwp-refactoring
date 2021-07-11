package kitchenpos.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class OrderTableTest {
    @DisplayName("빈테이블 여부 변경")
    @Test
    public void 빈테이블여부_변경_확인() throws Exception {
        //given
        OrderTable orderTable = new OrderTable(5, false);

        //when
        orderTable.changeEmpty(true);

        //then
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @DisplayName("빈테이블 여부 변경 예외 - 단체지정이 되어 있는 경우")
    @Test
    public void 단체지정이되어있는경우_빈테이블여부_변경_예외() throws Exception {
        //given
        OrderTable orderTable1 = new OrderTable(5, true);
        OrderTable orderTable2 = new OrderTable(5, true);
        new TableGroup(Arrays.asList(orderTable1, orderTable2));

        //when
        //then
        assertThatThrownBy(() -> orderTable1.changeEmpty(true))
                .hasMessage("단체지정이 되어있으면 안됩니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블 빈테이블 여부 변경 예외 - 주문상태가 조리인 경우")
    @Test
    public void 주문상태가조리인경우_빈테이블여부_변경_예외() throws Exception {
        //given
        OrderTable orderTable = new OrderTable(5, false);
        new Order(orderTable, Arrays.asList(new OrderLineItem(1L, 1L)));
        orderTable.getOrders().stream()
                .forEach(order -> order.setOrderStatus(OrderStatus.COOKING));

        //when
        //then
        assertThatThrownBy(() -> orderTable.changeEmpty(true))
                .hasMessage("주문테이블의 주문상태가 조리나 식사입니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블 빈테이블 여부 변경 예외 - 주문상태가 식사인 경우")
    @Test
    public void 주문상태가식사인경우_빈테이블여부_변경_예외() throws Exception {
        //given
        OrderTable orderTable = new OrderTable(5, false);
        new Order(orderTable, Arrays.asList(new OrderLineItem(1L, 1L)));
        orderTable.getOrders().stream()
                .forEach(order -> order.setOrderStatus(OrderStatus.MEAL));

        //when
        //then
        assertThatThrownBy(() -> orderTable.changeEmpty(true))
                .hasMessage("주문테이블의 주문상태가 조리나 식사입니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문한 손님 수 변경")
    @Test
    public void 방문한손님수_변경_확인() throws Exception {
        //given
        OrderTable orderTable = new OrderTable(5, false);

        //when
        orderTable.changeNumberOfGuests(2);

        //then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(new NumberOfGuests(2));
    }

    @DisplayName("방문한 손님 수 변경 예외 - 방문한 손님 수가 음수인 경우")
    @Test
    public void 방문한손님수가음수인경우_방문한손님수_변경_확인() throws Exception {
        //given
        OrderTable orderTable = new OrderTable(5, false);

        //when
        //then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문한 손님 수 변경 예외 - 빈테이블인 경우")
    @Test
    public void 빈테이블인경우_방문한손님수_변경_확인() throws Exception {
        //given
        OrderTable orderTable = new OrderTable(0, true);

        //when
        //then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(5)).isInstanceOf(IllegalArgumentException.class);
    }
}
