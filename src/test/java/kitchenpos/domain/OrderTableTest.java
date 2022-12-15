package kitchenpos.domain;

import static kitchenpos.domain.OrderStatus.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.exception.CannotChangeByOrderStatusException;
import kitchenpos.exception.ChangeEmptyGroupException;
import kitchenpos.exception.EmptyTableException;
import kitchenpos.exception.GroupTableException;
import kitchenpos.exception.InvalidNumberOfGuestsException;

class OrderTableTest {

    @DisplayName("[빈 테이블 상태변경] 단체지정된 주문테이블은 할 수 없다")
    @Test
    void test1() {
        OrderTable table = new OrderTable(10, true);
        table.group(new TableGroup());
        assertThatThrownBy(() -> table.changeEmpty(true))
            .isInstanceOf(ChangeEmptyGroupException.class);
    }

    @DisplayName("[빈 테이블 상태변경] 주문상태가 요리중, 식사중인 경우 할 수 없다")
    @Test
    void test2() {
        Order order = new Order();
        order.changeStatus(COOKING);
        OrderTable table = new OrderTable(order);

        assertThatThrownBy(() -> table.changeEmpty(true))
            .isInstanceOf(CannotChangeByOrderStatusException.class);
    }

    @DisplayName("[방문손님수 변경] 1명 이상만 가능")
    @Test
    void test3() {
        OrderTable table = new OrderTable(10, false);
        assertThatThrownBy(() -> table.changeNumberOfGuests(0))
            .isInstanceOf(InvalidNumberOfGuestsException.class);
    }

    @DisplayName("[방문손님수 변경] 빈 테이블인 경우 할 수 없다")
    @Test
    void test4() {
        OrderTable table = new OrderTable(10, true);
        assertThatThrownBy(() -> table.changeNumberOfGuests(3))
            .isInstanceOf(EmptyTableException.class);
    }

    @DisplayName("[단체지정] 빈 테이블인 경우만 할 수 있다")
    @Test
    void test5() {
        OrderTable table = new OrderTable(10, false);
        assertThatThrownBy(() -> table.group(new TableGroup()))
            .isInstanceOf(GroupTableException.class);
    }

    @DisplayName("[단체지정] 이미 단체지정 됐으면 할 수 없다")
    @Test
    void test6() {
        OrderTable table = new OrderTable(10, true);
        table.group(new TableGroup());
        assertThatThrownBy(() -> table.group(new TableGroup()))
            .isInstanceOf(GroupTableException.class);
    }

    @DisplayName("[단체지정 해제] 주문 상태가 변경불가일 때 해제할 수 없다")
    @Test
    void test7() {
        Order order = new Order();
        order.changeStatus(COOKING);
        OrderTable table = new OrderTable(order);

        assertThatThrownBy(table::ungroup)
            .isInstanceOf(CannotChangeByOrderStatusException.class);
    }

}
