package kitchenpos.application;

import kitchenpos.dao.FakeOrderRepository;
import kitchenpos.dao.FakeOrderTableRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableChangeEmptyRequest;
import kitchenpos.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.dto.OrderTableCreateRequest;
import kitchenpos.dto.OrderTableResponse;
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
    private final OrderRepository orderRepository = new FakeOrderRepository();
    private final OrderTableRepository orderTableRepository = new FakeOrderTableRepository();
    private final TableService tableService = new TableService(orderRepository, orderTableRepository);

    @DisplayName("주문 테이블 생성")
    @Test
    void create() {
        OrderTableCreateRequest orderTable = OrderTableCreateRequest.of(10, true);
        OrderTableResponse result = tableService.create(orderTable);
        assertAll(
                () -> assertThat(result.getTableGroupId()).isNull(),
                () -> assertThat(result.getNumberOfGuests()).isEqualTo(10)
        );
    }

    @DisplayName("모든 주문 테이블 조회")
    @Test
    void list() {
        OrderTableCreateRequest orderTable1 = OrderTableCreateRequest.of(1, true);
        OrderTableCreateRequest orderTable2 = OrderTableCreateRequest.of(2, true);
        OrderTableCreateRequest orderTable3 = OrderTableCreateRequest.of(3, true);

        tableService.create(orderTable1);
        tableService.create(orderTable2);
        tableService.create(orderTable3);

        List<OrderTableResponse> orderTableList = tableService.list();
        assertThat(orderTableList.size()).isEqualTo(3);
    }

    @DisplayName("주문 테이블이 없으면 테이블 공석 여부 변경을 할 수 없다.")
    @Test
    void notChangeEmptyTableNotExistsOrderTable() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(1L, OrderTableChangeEmptyRequest.of(false)));
    }

    @DisplayName("주문 상태가 COOKING,MEAL 이면 테이블 공석 여부를 변경할 수 없다.")
    @Test
    void notChangeEmptyTableCookingOrMeal() {
        OrderTable savedOrderTable = orderTableRepository.save(OrderTable.of(10, true));
        Order order = new Order(1L,
                savedOrderTable,
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                Arrays.asList(OrderLineItem.of(null, 20))
        );
        orderRepository.save(order);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), OrderTableChangeEmptyRequest.of(false)));
    }

    @DisplayName("주문 테이블 공석 여부 변경")
    @Test
    void successChangeEmpty() {
        OrderTable savedOrderTable = orderTableRepository.save(OrderTable.of(10, true));
        Order order = new Order(1L,
                savedOrderTable,
                OrderStatus.COMPLETION.name(),
                LocalDateTime.now(),
                Arrays.asList(OrderLineItem.of(null, 20))
        );
        orderRepository.save(order);

        OrderTableResponse result = tableService.changeEmpty(savedOrderTable.getId(), OrderTableChangeEmptyRequest.of(false));
        assertAll(
                () -> assertThat(result.getNumberOfGuests()).isEqualTo(10),
                () -> assertThat(result.isEmpty()).isFalse()
        );
    }

    @DisplayName("주문 테이블 손님 수가 0보다 작게 바꿀 수 없다")
    @Test
    void notChangeNumberOfGuestLessThanZero() {
        orderTableRepository.save(OrderTable.of(-1, true));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(1L, OrderTableChangeNumberOfGuestsRequest.of(-1)));
    }

    @DisplayName("주문 테이블이 공석인 상태면 손님 수를 변경할 수 없다.")
    @Test
    void notChangeNumberOfGuestOrderTableIsEmpty() {
        OrderTable savedOrderTable = orderTableRepository.save(OrderTable.of(10, true));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), OrderTableChangeNumberOfGuestsRequest.of(15)));
    }

    @DisplayName("주문 테이블 손님 수 변공 성공")
    @Test
    void successChangeNumberOfGuest() {
        OrderTable savedOrderTable = orderTableRepository.save(OrderTable.of(10, false));
        OrderTableResponse result = tableService.changeNumberOfGuests(savedOrderTable.getId(), OrderTableChangeNumberOfGuestsRequest.of(15));
        assertThat(result.getNumberOfGuests()).isEqualTo(15);
    }


}
