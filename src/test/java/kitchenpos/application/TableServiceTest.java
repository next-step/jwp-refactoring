package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    OrderDao orderDao;
    @Mock
    OrderTableDao orderTableDao;
    @InjectMocks
    TableService tableService;

    @DisplayName("테이블을 생성한다.")
    @Test
    void create() {
        // Given
        OrderTable orderTableRequest = new OrderTable(0, true);
        given(orderTableDao.save(orderTableRequest)).willReturn(new OrderTable(1L, null, 0, true));

        // When
        OrderTable tableResponse = tableService.create(orderTableRequest);

        // Then
        assertAll(
                () -> assertThat(tableResponse.getId()).isNotNull(),
                () -> assertThat(tableResponse.getNumberOfGuests()).isEqualTo(orderTableRequest.getNumberOfGuests()),
                () -> assertThat(tableResponse.isEmpty()).isEqualTo(orderTableRequest.isEmpty())
        );
    }

    @DisplayName("테이블 리스트 조회")
    @Test
    void findAll() {
        // Given
        OrderTable savedOrderTable = new OrderTable(0, true);
        OrderTable savedNotEmptyOrderTable = new OrderTable(5, false);
        given(orderTableDao.findAll()).willReturn(Arrays.asList(savedOrderTable, savedNotEmptyOrderTable));

        // When
        List<OrderTable> orderTablesResponse = tableService.list();

        // Then
        assertAll(
                () -> assertThat(orderTablesResponse.size()).isEqualTo(2),
                () -> assertThat(orderTablesResponse).containsExactly(savedOrderTable, savedNotEmptyOrderTable)
        );
    }

    @DisplayName("테이블 인원수 수정")
    @Test
    void updateNumberOfGuest() {
        // Given
        OrderTable savedOrderTable = new OrderTable(1L, null, 5, false);
        OrderTable updateOrderTableRequest = new OrderTable(3);
        given(orderTableDao.findById(savedOrderTable.getId())).willReturn(Optional.of(savedOrderTable));
        given(orderTableDao.save(any(OrderTable.class))).willReturn(new OrderTable(1L, null, 3, false));

        // When
        OrderTable orderTableResponse = tableService.changeNumberOfGuests(savedOrderTable.getId(), updateOrderTableRequest);

        // Then
        assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(3);
    }

    @DisplayName("테이블 인원수 수정 에러처리")
    @Test
    void updateNumberOfGuestException() {
        // Given
        OrderTable savedOrderTable = new OrderTable(1L, null, 5, true);
        OrderTable updateMinusNumberOrderTableRequest = new OrderTable(-1);
        OrderTable updateOrderTableRequest = new OrderTable(3);

        // When, Then
        assertAll(
                () -> assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), updateMinusNumberOrderTableRequest)).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), updateOrderTableRequest)).isInstanceOf(IllegalArgumentException.class)
        );
    }

    @DisplayName("테이블 상태 변경")
    @Test
    void changeEmpty() {
        // Given
        OrderTable savedOrderTable = new OrderTable(1L, null, 5, false);
        OrderTable updateOrderTableRequest = new OrderTable(true);
        given(orderTableDao.findById(savedOrderTable.getId())).willReturn(Optional.of(savedOrderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(savedOrderTable.getId(),Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);
        given(orderTableDao.save(any(OrderTable.class))).willReturn(new OrderTable(1L, 1L, 5, true));

        // When
        OrderTable orderTableResponse = tableService.changeEmpty(savedOrderTable.getId(), updateOrderTableRequest);

        assertThat(orderTableResponse.isEmpty()).isEqualTo(true);
    }
}