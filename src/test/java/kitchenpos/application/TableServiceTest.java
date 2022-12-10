package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
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
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void createTable() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 4, true);
        when(orderTableDao.save(orderTable)).thenReturn(orderTable);

        // when
        OrderTable result = tableService.create(orderTable);

        // then
        assertAll(
            () -> assertThat(result.getTableGroupId()).isNull(),
            () -> assertThat(result.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests())
        );
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void findAllTables() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 4, true);
        when(orderTableDao.findAll()).thenReturn(Arrays.asList(orderTable));

        // when
        List<OrderTable> result = tableService.list();

        // then
        assertThat(result).hasSize(1)
            .containsExactly(orderTable);
    }

    @DisplayName("주문 테이블의 비어있는지 상태를 변경한다.")
    @Test
    void updateOrderTableEmpty() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 4, true);
        OrderTable updateOrderTable = new OrderTable(orderTable.getId(), null, orderTable.getNumberOfGuests(), false);
        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(),
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(false);
        when(orderTableDao.save(orderTable)).thenReturn(orderTable);

        // when
        OrderTable result = tableService.changeEmpty(orderTable.getId(), updateOrderTable);

        // then
        assertThat(result.isEmpty()).isEqualTo(false);
    }

    @DisplayName("등록되지 않은 주문 테이블의 비어있는 상태를 변경하면 예외가 발생한다.")
    @Test
    void updateOrderTableNotExistException() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 4, true);
        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정된 주문 테이블의 비어있는 상태를 변경하면 예외가 발생한다.")
    @Test
    void updateOrderTableAssignGroupException() {
        // given
        OrderTable orderTable = new OrderTable(1L, 1L, 4, true);
        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 상태가 조리또는 식사 중이면 비어있는 상태 변경시 예외가 발생한다.")
    @Test
    void updateOrderTableStatusException() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 4, true);
        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(),
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 방문한 손님 수를 변경한다.")
    @ParameterizedTest
    @ValueSource(ints = { 6, 8, 12 })
    void updateOrderTableNumberOfGuest(int numberOfGuest) {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 4, false);
        OrderTable updateOrderTable = new OrderTable(orderTable.getId(), null, numberOfGuest, orderTable.isEmpty());
        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));
        when(orderTableDao.save(orderTable)).thenReturn(orderTable);

        // when
        OrderTable result = tableService.changeNumberOfGuests(orderTable.getId(), updateOrderTable);

        // then
        assertThat(result.getNumberOfGuests()).isEqualTo(numberOfGuest);
    }

    @DisplayName("주문 테이블의 방문한 손님 수를 변경 시 0명보다 작으면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = { -1, -4, -6 })
    void updateOrderTableUnderZeroNumberOfGuestException(int numberOfGuest) {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 4, false);
        OrderTable updateOrderTable = new OrderTable(orderTable.getId(), null, numberOfGuest, orderTable.isEmpty());

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), updateOrderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되지 않은 주문 테이블의 손님 수를 변경하면 예외가 발생한다.")
    @Test
    void updateOrderTableNotExistNumberOfGuestException() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 4, false);
        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 주문 테이블의 손님 수를 변경하면 예외가 발생한다.")
    @Test
    void updateOrderTableEmptyNumberOfGuestException() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 4, true);
        OrderTable updateOrderTable = new OrderTable(orderTable.getId(), null, 6, orderTable.isEmpty());
        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), updateOrderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
