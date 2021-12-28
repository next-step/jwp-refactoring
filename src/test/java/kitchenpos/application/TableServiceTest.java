package kitchenpos.application;

import kitchenpos.dao.FakeOrderDao;
import kitchenpos.dao.FakeOrderTableDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 테이블 테스트")
class TableServiceTest {
    private final OrderDao orderDao = new FakeOrderDao();
    private final OrderTableDao orderTableDao = new FakeOrderTableDao();
    private final TableService tableService = new TableService(orderDao, orderTableDao);

    @DisplayName("주문 테이블 생성")
    @Test
    void create() {
        OrderTable orderTable = new OrderTable(10, true);
        OrderTable result = tableService.create(orderTable);
        assertAll(
                () -> assertThat(result.getTableGroupId()).isNull(),
                () -> assertThat(result.getNumberOfGuests()).isEqualTo(10)
        );
    }

    @DisplayName("모든 주문 테이블 조회")
    @Test
    void list() {
        OrderTable orderTable1 = new OrderTable(1, true);
        OrderTable orderTable2 = new OrderTable(2, true);
        OrderTable orderTable3 = new OrderTable(3, true);

        tableService.create(orderTable1);
        tableService.create(orderTable2);
        tableService.create(orderTable3);

        List<OrderTable> orderTableList = tableService.list();
        assertThat(orderTableList.size()).isEqualTo(3);
    }

    @DisplayName("주문 테이블이 없으면 테이블 공석 여부 변경을 할 수 없다.")
    @Test
    void notChangeEmptyTableNotExistsOrderTable() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(1L, new OrderTable(false)));
    }

    @DisplayName("주문 상태가 COOKING,MEAL 이면 테이블 공석 여부를 변경할 수 없다.")
    @Test
    void notChangeEmptyTableCookingOrMeal() {
        OrderTable savedOrderTable = orderTableDao.save(OrderTable.of(10, true));
        Order order = new Order(1L,
                savedOrderTable.getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                Arrays.asList(OrderLineItem.of(1L, 20))
        );
        orderDao.save(order);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), new OrderTable(false)));
    }

    @DisplayName("주문 테이블 공석 여부 변경")
    @Test
    void successChangeEmpty() {
        OrderTable savedOrderTable = orderTableDao.save(OrderTable.of(10, true));
        Order order = new Order(1L,
                savedOrderTable.getId(),
                OrderStatus.COMPLETION.name(),
                LocalDateTime.now(),
                Arrays.asList(OrderLineItem.of(1L, 20))
        );
        orderDao.save(order);

        OrderTable result = tableService.changeEmpty(savedOrderTable.getId(), new OrderTable(false));
        assertAll(
                () -> assertThat(result.getNumberOfGuests()).isEqualTo(10),
                () -> assertThat(result.isEmpty()).isFalse()
        );
    }

    @DisplayName("주문 테이블 손님 수가 0보다 작게 바꿀 수 없다")
    @Test
    void notChangeNumberOfGuestLessThanZero() {
        orderTableDao.save(OrderTable.of(-1, true));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(1L, new OrderTable(-1)));
    }

    @DisplayName("주문 테이블이 없으면 주문 테이블 손님 수를 변경할 수 없다.")
    @Test
    void notChangeNumberOfGuestNotExistsOrderTable() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(1L, new OrderTable(10, false)));
    }

    @DisplayName("주문 테이블이 공석인 상태면 손님 수를 변경할 수 없다.")
    @Test
    void notChangeNumberOfGuestOrderTableIsEmpty() {
        OrderTable savedOrderTable = orderTableDao.save(OrderTable.of(10, true));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), new OrderTable(15)));
    }

    @DisplayName("주문 테이블 손님 수 변공 성공")
    @Test
    void successChangeNumberOfGuest() {
        OrderTable savedOrderTable = orderTableDao.save(OrderTable.of(10, false));
        OrderTable result = tableService.changeNumberOfGuests(savedOrderTable.getId(), new OrderTable(15));
        assertThat(result.getNumberOfGuests()).isEqualTo(15);
    }


}
