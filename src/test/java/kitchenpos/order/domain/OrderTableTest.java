package kitchenpos.order.domain;

import kitchenpos.order.exception.OrderExceptionCode;
import kitchenpos.order.exception.OrderTableExceptionCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("주문 테이블 클래스 테스트")
class OrderTableTest {

    private TableGroup 단체_테이블;
    private OrderTable 단체_주문_테이블1;
    private OrderTable 단체_주문_테이블2;

    @BeforeEach
    void setUp() {
        단체_테이블 = new TableGroup();
        단체_주문_테이블1 = new OrderTable(0, true);
        단체_주문_테이블2 = new OrderTable(0, true);

        단체_테이블.group(Arrays.asList(단체_주문_테이블1, 단체_주문_테이블2));
    }

    @Test
    void 주문_테이블이_비어있지_않으면_테이블_그룹을_생성할_수_없음() {
        단체_주문_테이블1.ungroup();

        assertThatThrownBy(() -> {
            단체_주문_테이블1.checkOrderTableForTableGrouping();
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderTableExceptionCode.NON_EMPTY_ORDER_TABLE_CANNOT_BE_INCLUDED_IN_TABLE_GROUP.getMessage());
    }

    @Test
    void 테이블_그룹을_해제() {
        단체_주문_테이블1.ungroup();

        assertThat(단체_주문_테이블1.getTableGroup()).isNull();
    }

    @Test
    void 다른_테이블_그룹에_포함되어_있으면_빈_테이블로_변경할_수_없음() {
        assertThatThrownBy(() -> {
            단체_주문_테이블1.changeEmpty(true, Collections.emptyList());
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderTableExceptionCode.ALREADY_INCLUDED_IN_ANOTHER_TABLE_GROUP.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = { "COOKING", "MEAL" })
    void 요리중이거나_식사중인_주문이_있다면_빈_테이블로_변경할_수_없음(OrderStatus orderStatus) {
        OrderTable 주문_테이블 = new OrderTable(5, false);
        Order order = new Order(주문_테이블, orderStatus);

        assertThatThrownBy(() -> {
            주문_테이블.changeEmpty(true, Arrays.asList(order));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderExceptionCode.CANNOT_BE_CHANGED.getMessage());
    }

    @Test
    void 빈_테이블로_변경() {
        OrderTable 주문_테이블 = new OrderTable(5, false);
        Order order = new Order(주문_테이블, OrderStatus.COMPLETION);

        주문_테이블.changeEmpty(true, Arrays.asList(order));

        assertTrue(주문_테이블.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(ints = { -1, -2, -5, -10 })
    void 입력된_주문_테이블_방문자_수가_음수이면_테이블_방문자_수를_변경할_수_없음(int numberOfGuests) {
        assertThatThrownBy(() -> {
            단체_주문_테이블1.changeNumberOfGuests(numberOfGuests);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderTableExceptionCode.INVALID_NUMBER_OF_GUESTS.getMessage());
    }

    @Test
    void 주문_테이블이_비어있으면_테이블_방문자_수를_변경할_수_없음() {
        OrderTable 주문_테이블 = new OrderTable(0, true);

        assertThatThrownBy(() -> {
            주문_테이블.changeNumberOfGuests(5);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderTableExceptionCode.NUMBER_OF_GUESTS_CANNOT_BE_CHANGED.getMessage());
    }

    @Test
    void 테이블_방문자_수_변경() {
        단체_주문_테이블1.changeNumberOfGuests(5);

        assertEquals(5, 단체_주문_테이블1.getNumberOfGuests());
    }
}
