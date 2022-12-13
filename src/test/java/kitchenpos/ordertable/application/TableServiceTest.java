package kitchenpos.ordertable.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 테이블 관련 비즈니스 테스트")
@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void createTable() {
        // given
        OrderTableRequest orderTableRequest = OrderTableRequest.of(4, true);
        when(orderTableRepository.save(orderTableRequest.toOrderTable())).thenReturn(orderTableRequest.toOrderTable());

        // when
        OrderTableResponse result = tableService.create(orderTableRequest);

        // then
        assertAll(
            () -> assertThat(result.getTableGroupId()).isNull(),
            () -> assertThat(result.getNumberOfGuests()).isEqualTo(orderTableRequest.getNumberOfGuests())
        );
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void findAllTables() {
        // given
        OrderTableRequest orderTableRequest = OrderTableRequest.of(4, true);
        when(orderTableRepository.findAll()).thenReturn(Arrays.asList(orderTableRequest.toOrderTable()));

        // when
        List<OrderTableResponse> result = tableService.list();

        assertAll(
            () -> assertThat(result).hasSize(1),
            () -> assertThat(result.stream().map(OrderTableResponse::getId)).containsExactly(orderTableRequest.toOrderTable().getId())
        );
    }

    @DisplayName("주문 테이블의 비어있는지 상태를 변경한다.")
    @Test
    void updateOrderTableEmpty() {
        // given
        OrderTableRequest orderTableRequest = OrderTableRequest.of(4, true);
        OrderTableRequest updateOrderTableRequest = OrderTableRequest.of(4, false);
        when(orderTableRepository.findById(orderTableRequest.toOrderTable().getId())).thenReturn(Optional.of(orderTableRequest.toOrderTable()));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableRequest.toOrderTable().getId(),
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(false);

        // when
        OrderTableResponse resultOrderTable = tableService.changeEmpty(orderTableRequest.toOrderTable().getId(), updateOrderTableRequest);

        // then
        assertThat(resultOrderTable.isEmpty()).isEqualTo(false);
    }

    @DisplayName("등록되지 않은 주문 테이블의 비어있는 상태를 변경하면 예외가 발생한다.")
    @Test
    void updateOrderTableNotExistException() {
        // given
        OrderTableRequest orderTableRequest = OrderTableRequest.of(4, true);
        when(orderTableRepository.findById(orderTableRequest.toOrderTable().getId())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableRequest.toOrderTable().getId(), orderTableRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정된 주문 테이블의 비어있는 상태를 변경하면 예외가 발생한다.")
    @Test
    void updateOrderTableAssignGroupException() {
        // given
        OrderTable orderTable1 = new OrderTable( 4, true);
        OrderTable orderTable2 = new OrderTable( 4, true);
        TableGroup tableGroup = new TableGroup(
            OrderTables.from(Arrays.asList(orderTable1, orderTable2)));
        OrderTable orderTable = new OrderTable(5L, tableGroup, 4, false);
        OrderTableRequest orderTableRequest = OrderTableRequest.of(4, true);
        when(orderTableRepository.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTableRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 상태가 조리또는 식사 중이면 비어있는 상태 변경시 예외가 발생한다.")
    @Test
    void updateOrderTableStatusException() {
        // given
        OrderTableRequest orderTableRequest = OrderTableRequest.of(4, true);
        when(orderTableRepository.findById(orderTableRequest.toOrderTable().getId())).thenReturn(Optional.of(orderTableRequest.toOrderTable()));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableRequest.toOrderTable().getId(),
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableRequest.toOrderTable().getId(), orderTableRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 방문한 손님 수를 변경한다.")
    @ParameterizedTest
    @ValueSource(ints = { 6, 8, 12 })
    void updateOrderTableNumberOfGuest(int numberOfGuest) {
        // given
        OrderTableRequest orderTableRequest = OrderTableRequest.of(4, false);
        OrderTableRequest updateOrderTableRequest = OrderTableRequest.of(numberOfGuest, false);
        when(orderTableRepository.findById(orderTableRequest.toOrderTable().getId())).thenReturn(Optional.of(orderTableRequest.toOrderTable()));

        // when
        OrderTableResponse result = tableService.changeNumberOfGuests(orderTableRequest.toOrderTable().getId(), updateOrderTableRequest);

        // then
        assertThat(result.getNumberOfGuests()).isEqualTo(numberOfGuest);
    }

    @DisplayName("주문 테이블의 방문한 손님 수를 변경 시 0명보다 작으면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = { -1, -4, -6 })
    void updateOrderTableUnderZeroNumberOfGuestException(int numberOfGuest) {
        // given
        OrderTableRequest orderTableRequest = OrderTableRequest.of(4, false);
        OrderTableRequest updateOrderTableRequest = OrderTableRequest.of(numberOfGuest, false);
        when(orderTableRepository.findById(orderTableRequest.toOrderTable().getId())).thenReturn(Optional.of(orderTableRequest.toOrderTable()));

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableRequest.toOrderTable().getId(), updateOrderTableRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되지 않은 주문 테이블의 손님 수를 변경하면 예외가 발생한다.")
    @Test
    void updateOrderTableNotExistNumberOfGuestException() {
        // given
        OrderTableRequest orderTableRequest = OrderTableRequest.of(4, false);
        when(orderTableRepository.findById(orderTableRequest.toOrderTable().getId())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableRequest.toOrderTable().getId(), orderTableRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 주문 테이블의 손님 수를 변경하면 예외가 발생한다.")
    @Test
    void updateOrderTableEmptyNumberOfGuestException() {
        // given
        OrderTableRequest orderTableRequest = OrderTableRequest.of(4, true);
        OrderTableRequest updateOrderTableRequest = OrderTableRequest.of(6, true);
        when(orderTableRepository.findById(orderTableRequest.toOrderTable().getId())).thenReturn(Optional.of(orderTableRequest.toOrderTable()));

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableRequest.toOrderTable().getId(), updateOrderTableRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
