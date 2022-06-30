package kitchenpos.application;

import static kitchenpos.utils.generator.OrderTableFixtureGenerator.generateOrderTable;
import static kitchenpos.utils.generator.OrderTableFixtureGenerator.generateOrderTables;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service:Table")
class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @Test
    @DisplayName("주문 테이블을 생성한다.")
    public void createOrderTable() {
        // Given
        OrderTable givenOrderTable = generateOrderTable();
        given(orderTableDao.save(any(OrderTable.class))).will(AdditionalAnswers.returnsFirstArg());

        // When
        OrderTable actualOrderTable = tableService.create(givenOrderTable);

        // Then
        verify(orderTableDao).save(any(OrderTable.class));
        assertThat(actualOrderTable)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(givenOrderTable);
    }

    @Test
    @DisplayName("주문 목록을 조회 한다.")
    public void getAllOrderTables() {
        // Given
        final int generateOrderTableCount = 5;
        List<OrderTable> givenOrderTables = generateOrderTables(generateOrderTableCount);
        given(orderTableDao.findAll()).willReturn(givenOrderTables);

        // When
        List<OrderTable> actualOrderTables = tableService.list();

        // Then
        assertThat(actualOrderTables).hasSize(generateOrderTableCount);
    }

    @Test
    @DisplayName("테이블의 사용 가능 상태를 변경한다.")
    public void changeEmpty() {
        // Given
        OrderTable givenOrderTable = generateOrderTable();
        given(orderTableDao.findById(any())).willReturn(Optional.of(givenOrderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), anyList())).willReturn(false);
        given(orderTableDao.save(any())).will(AdditionalAnswers.returnsFirstArg());

        // When
        OrderTable updateEmptyRequest = generateOrderTable();
        updateEmptyRequest.setEmpty(true);
        OrderTable actualOrderTable = tableService.changeEmpty(givenOrderTable.getId(), updateEmptyRequest);

        // Then
        verify(orderTableDao).findById(any());
        verify(orderDao).existsByOrderTableIdAndOrderStatusIn(any(), anyList());
        verify(orderTableDao).save(any());
        assertThat(actualOrderTable)
            .usingRecursiveComparison()
            .ignoringFields("empty")
            .isEqualTo(givenOrderTable);
        assertThat(actualOrderTable.isEmpty()).isEqualTo(updateEmptyRequest.isEmpty());
    }

    @Test
    @DisplayName("주문 테이블이 존재하지 않는 경우 사용 가능 상태는 변경할 수 없다.")
    public void throwException_WhenTargetOrderTableIsNotExist() {
        // Given
        OrderTable givenOrderTable = generateOrderTable();
        given(orderTableDao.findById(any())).willThrow(IllegalArgumentException.class);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> tableService.changeEmpty(any(), givenOrderTable));
        verify(orderTableDao).findById(any());
    }

    @Test
    @DisplayName("그룹핑된 테이블의 사용 가능 상태는 변경할 수 없다.")
    public void throwException_WhenTargetOrderTableIsBindingToTableGroup() {
        // Given
        OrderTable givenOrderTable = generateOrderTable();
        givenOrderTable.setTableGroupId(1L);
        given(orderTableDao.findById(any())).willReturn(Optional.of(givenOrderTable));

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> tableService.changeEmpty(any(), givenOrderTable));

        verify(orderTableDao).findById(any());
    }

    @Test
    @DisplayName("주문 테이블의 주문 상태가 조리중이거나 식사중인 경우 테이블의 사용 가능 상태를 변경할 수 없다.")
    public void throwException_WhenTargetOrderTableIsMealOrCooking() {
        // Given
        OrderTable givenOrderTable = generateOrderTable();
        given(orderTableDao.findById(any())).willReturn(Optional.of(givenOrderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), anyList())).willReturn(true);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> tableService.changeEmpty(any(), givenOrderTable));

        verify(orderTableDao).findById(any());
        verify(orderDao).existsByOrderTableIdAndOrderStatusIn(any(), anyList());
    }

    @Test
    @DisplayName("주문 테이블이 존재하지 않는 경우 객수를 변경할 수 없다.")
    public void throwException_WhenOrderTableIsNotExist() {
        // Given
        OrderTable orderTable = generateOrderTable();
        given(orderTableDao.findById(any())).willThrow(IllegalArgumentException.class);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> tableService.changeNumberOfGuests(any(), orderTable));

        verify(orderTableDao).findById(any());
    }

    @Test
    @DisplayName("주문 테이블의 객수를 변경한다.")
    public void changeNumberOfGuests() {
        // Given
        OrderTable givenOrderTable = generateOrderTable();
        given(orderTableDao.findById(any())).willReturn(Optional.of(givenOrderTable));
        given(orderTableDao.save(any(OrderTable.class))).will(AdditionalAnswers.returnsFirstArg());

        final int newNumberOfGuests = 4;
        OrderTable updateNumberOfGuestsRequest = generateOrderTable();
        updateNumberOfGuestsRequest.setNumberOfGuests(4);

        // When
        OrderTable actualOrderTable = tableService.changeNumberOfGuests(any(), updateNumberOfGuestsRequest);

        // Then
        verify(orderTableDao).findById(any());
        verify(orderTableDao).save(any());
        assertThat(actualOrderTable)
            .usingRecursiveComparison()
            .ignoringFields("numberOfGuests")
            .isEqualTo(givenOrderTable);
        assertThat(actualOrderTable.getNumberOfGuests()).isEqualTo(newNumberOfGuests);
    }
}
