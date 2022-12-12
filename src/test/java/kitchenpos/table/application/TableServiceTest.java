package kitchenpos.table.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.persistence.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.persistence.OrderTableRepository;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    @InjectMocks
    private TableService tableService;
    @Mock
    private OrderRepository orderDao;
    @Mock
    private OrderTableRepository orderTableDao;

    @DisplayName("주문테이블을 생성할 경우 주문테이블을 반환")
    @Test
    public void returnOderTable() {
        OrderTable orderTable = OrderTable.builder()
                .tableGroup(TableGroup.builder().id(443l).build())
                .build();
        doReturn(orderTable).when(orderTableDao).save(any(OrderTable.class));

        assertThat(tableService.create(new OrderTableRequest()).getTableGroupId()).isEqualTo(443l);
    }

    @DisplayName("주문테이블목록을 조회할경우 주문테이블목록 반환")
    @Test
    public void returnOderTables() {
        List<OrderTable> orderTables = getOrderTables(OrderTable
                .builder()
                .id(13l)
                .tableGroup(TableGroup.builder().id(443l).build())
                .empty(true)
                .build(), 100);
        doReturn(orderTables).when(orderTableDao).findAll();

        List<OrderTableResponse> returnedTables = tableService.list();
        assertAll(() -> assertThat(returnedTables).hasSize(100), () -> assertThat(returnedTables.stream().map(OrderTableResponse::getId)).allMatch(id -> id == 13l), () -> assertThat(returnedTables.stream().map(OrderTableResponse::isEmpty)).allMatch(empty -> empty));
    }

    @DisplayName("주문테이블의 공석여부를 수정할 경우 주문테이블이 등록안되있으면 예외발생")
    @Test
    public void throwsExceptionWhenGroupIdIsNull() {
        OrderTable orderTable = OrderTable.builder().build();
        doReturn(Optional.empty()).when(orderTableDao).findById(orderTable.getId());

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), OrderTable.builder().build())).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블의 공석여부를 수정할 경우 테이블그룹이 존재하면 예외발생")
    @Test
    public void throwsExceptionWhenExistsTableGroup() {
        List<Order> orders = getOrders(Order.builder().orderStatus(OrderStatus.COOKING.name()).build(), 5);
        OrderTable orderTable = OrderTable.builder()
                .tableGroup(TableGroup.builder().id(13l).build())
                .build();
        doReturn(Optional.ofNullable(orderTable))
                .when(orderTableDao)
                .findById(orderTable.getId());
        doReturn(orders).when(orderDao)
                .findAllByOrderTable(orderTable);
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), OrderTable.builder().build())).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블의 공석여부를 수정할 경우 테이블이 조리중이나 식사중이면 예외발생")
    @Test
    public void throwsExceptionWhenExistsTableGroupAndMillOrCook() {
        List<Order> orders = getOrders(Order.builder().orderStatus(OrderStatus.COOKING.name()).build(), 5);
        OrderTable orderTable = OrderTable.builder().build();
        doReturn(Optional.ofNullable(orderTable)).when(orderTableDao).findById(orderTable.getId());
        doReturn(orders).when(orderDao)
                .findAllByOrderTable(orderTable);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), OrderTable.builder().build())).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블의 공석여부를 수정하면 수정된 테이블정보을 반환")
    @Test
    public void returnOrderTableWithEmpty() {
        OrderTable orderTable = OrderTable.builder().build();
        OrderTable savedTable = OrderTable.builder().empty(true).build();

        doReturn(Optional.ofNullable(savedTable)).when(orderTableDao).findById(orderTable.getId());
        doReturn(savedTable).when(orderTableDao).save(savedTable);
        assertThat(tableService.changeEmpty(orderTable.getId(), orderTable).isEmpty()).isFalse();
    }

    @DisplayName("주문테이블의 손님수를 수정할 경우 손님수가 0보다 작으면 예외발생")
    @Test
    public void throwsExceptionWhenGuestNumberIsNegative() {
        OrderTable orderTable = OrderTable.builder()
                .numberOfGuests(Arbitraries.integers().lessOrEqual(-1).sample())
                .build();

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블의 손님수를 수정할 경우 테이블이 등록안되있으면 예외발생")
    @Test
    public void throwsExceptionWhenNoneExistsTable() {
        OrderTable orderTable = OrderTable.builder()
                .numberOfGuests(Arbitraries.integers().greaterOrEqual(0).sample())
                .build();
        doReturn(Optional.empty()).when(orderTableDao).findById(orderTable.getId());

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블의 손님수를 수정할 경우 테이블이 공석이면 예외발생")
    @Test
    public void throwsExceptionWhenEmptyTable() {
        OrderTable orderTable = OrderTable.builder()
                .numberOfGuests(Arbitraries.integers().greaterOrEqual(0).sample())
                .empty(true)
                .build();
        doReturn(Optional.ofNullable(orderTable)).when(orderTableDao).findById(orderTable.getId());

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블의 손님수를 수정할 경우 수정된 테이블을 반환")
    @Test
    public void returnOrderTableWithGuest() {
        OrderTable orderTable = OrderTable.builder()
                .numberOfGuests(15)
                .build();
        OrderTable findTable = OrderTable.builder()
                .numberOfGuests(5)
                .build();
        doReturn(Optional.ofNullable(findTable)).when(orderTableDao).findById(orderTable.getId());
        doReturn(findTable).when(orderTableDao).save(findTable);

        assertThat(tableService.changeNumberOfGuests(orderTable.getId(), orderTable).getNumberOfGuests()).isEqualTo(15);

    }

    private List<Order> getOrders(Order order, int size) {
        return IntStream.rangeClosed(1, size)
                .mapToObj(value -> Order.builder()
                        .id(order.getId())
                        .orderTable(order.getOrderTable())
                        .orderStatus(order.getOrderStatus())
                        .orderLineItems(order.getOrderLineItems())
                        .build())
                .collect(Collectors.toList());
    }

    private List<OrderTable> getOrderTables(OrderTable orderTable, int size) {
        return IntStream.rangeClosed(1, size)
                .mapToObj(value -> orderTable)
                .collect(Collectors.toList());
    }
}
