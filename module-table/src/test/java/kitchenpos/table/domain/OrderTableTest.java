package kitchenpos.table.domain;

import static kitchenpos.helper.MenuFixtures.메뉴_만들기;
import static kitchenpos.helper.OrderFixtures.주문_만들기;
import static kitchenpos.helper.OrderLineItemFixtures.주문_항목_만들기;
import static kitchenpos.helper.TableFixtures.빈_테이블_만들기;
import static kitchenpos.helper.TableFixtures.주문_테이블_만들기;
import static kitchenpos.helper.TableFixtures.테이블_만들기;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.Arrays;
import kitchenpos.order.consts.OrderStatus;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.Orders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 테이블 관련 Domain 단위 테스트")
class OrderTableTest {

    @DisplayName("빈 테이블 여부 변경이 가능한지 확인한다.")
    @Test
    void checkPossibleChangeEmpty() {

        //given
        OrderTable orderTable1 = 주문_테이블_만들기();
        OrderLineItems orderLineItems = new OrderLineItems(Arrays.asList(주문_항목_만들기(메뉴_만들기(1L, "후라이드치킨", 16000), 3)));
        Orders orders1 = new Orders(Arrays.asList(주문_만들기(OrderStatus.MEAL, orderTable1, orderLineItems),
                주문_만들기(OrderStatus.COMPLETION, orderTable1, orderLineItems)));

        OrderTable orderTable2 = 테이블_만들기(2, false);
        Orders orders2 = new Orders(Arrays.asList(주문_만들기(OrderStatus.COOKING, orderTable2, orderLineItems),
                주문_만들기(OrderStatus.COMPLETION, orderTable2, orderLineItems)));

        OrderTable orderTable3 = 테이블_만들기(2, false);
        Orders orders3 = new Orders(Arrays.asList(주문_만들기(OrderStatus.COMPLETION, orderTable2, orderLineItems),
                주문_만들기(OrderStatus.COMPLETION, orderTable3, orderLineItems)));

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderTable1.changeEmpty(new TableEmpty(true), orders1))
                .withMessageContaining("식사 상태인 주문이 있어 빈 테이블 여부 업데이트 할 수 없습니다.");
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderTable2.changeEmpty(new TableEmpty(true), orders2))
                .withMessageContaining("조리 상태인 주문이 있어 빈 테이블 여부 업데이트 할 수 없습니다.");
        assertThatNoException()
                .isThrownBy(() -> orderTable3.changeEmpty(new TableEmpty(true), orders3));

    }

    @DisplayName("방문 손님 수를 0명 미만으로 업데이트 할 수 없다.")
    @Test
    void updateNumberOfGuests_less_then_one() {

        //given
        OrderTable orderTable = 테이블_만들기(3, false);

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderTable.changeNumberOfGuests(new NumberOfGuest(-1)));
    }

    @DisplayName("빈 테이블인 경우 방문 손님 수 업데이트 할 수 없다. ")
    @Test
    void updateNumberOfGuests_not_empty_table() {

        //given
        OrderTable orderTable = 빈_테이블_만들기();

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderTable.changeNumberOfGuests(new NumberOfGuest(5)));
    }

}
