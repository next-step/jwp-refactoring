package kitchenpos.application;

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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@DisplayName("주문 테이블 관련 비즈니스 기능 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @DisplayName("주문테이블 생성 테스트")
    @Test
    void createTableTest() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 1, true);
        when(orderTableDao.save(orderTable)).thenReturn(orderTable);

        // when
        OrderTable result = tableService.create(orderTable);

        // then
        checkForCreateTable(result, orderTable);
    }

    private void checkForCreateTable(OrderTable createTable, OrderTable sourceTable) {
        assertAll(
                () -> assertThat(createTable.getTableGroupId()).isNull(),
                () -> assertThat(createTable.getNumberOfGuests()).isEqualTo(sourceTable.getNumberOfGuests())
        );
    }

    @DisplayName("주문테이블 목록 조회 테스트")
    @Test
    void findAllTablesTest() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 4, true);
        when(orderTableDao.findAll()).thenReturn(Arrays.asList(orderTable));

        // when
        List<OrderTable> result = tableService.list();

        // then
        assertThat(result).hasSize(1)
                .containsExactly(orderTable);
    }

    @DisplayName("주문 테이블 상태 변경 테스트")
    @Test
    void updateOrderTableEmpty() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 4, true);
        OrderTable updateOrderTable = new OrderTable(orderTable.getId(), null, orderTable.getNumberOfGuests(), false);
        settingMockInfoForUpdateTable(orderTable);

        // when
        OrderTable result = tableService.changeEmpty(orderTable.getId(), updateOrderTable);

        // then
        assertThat(result.isEmpty()).isEqualTo(false);
    }

    private void settingMockInfoForUpdateTable(OrderTable orderTable) {
        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(false);
        when(orderTableDao.save(orderTable)).thenReturn(orderTable);
    }

    @DisplayName("주문 테이블 상태 변경 테스트 - 등록되지 않은 주문 테이블일 경우")
    @Test
    void updateOrderTableEmpty2() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 4, true);
        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 상태 변경 테스트 - 단체지정되있는 테이블일 경우")
    @Test
    void updateOrderTableEmpty3() {
        // given
        OrderTable orderTable = new OrderTable(1L, 1L, 4, true);
        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 상태 변경 테스트 - 주문 테이블의 상태가 조리또는 식사 중이면 비어있는 상태일 경우")
    @Test
    void updateOrderTableEmpty4() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 4, true);
        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 손님수 변경 테스트")
    @Test
    void updateOrderTableNumberOfGuest() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 4, false);
        OrderTable updateOrderTable = new OrderTable(orderTable.getId(), null, 6, orderTable.isEmpty());
        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));
        when(orderTableDao.save(orderTable)).thenReturn(orderTable);

        // when
        OrderTable result = tableService.changeNumberOfGuests(orderTable.getId(), updateOrderTable);

        // then
        assertThat(result.getNumberOfGuests()).isEqualTo(6);
    }

    @DisplayName("주문 테이블 손님수 변경 테스트 - 0명보다 작은 경우")
    @ParameterizedTest
    @ValueSource(ints = { -1, -3, -7 })
    void updateOrderTableNumberOfGuest2(int numberOfGuest) {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 4, false);
        OrderTable updateOrderTable = new OrderTable(orderTable.getId(), null, numberOfGuest, orderTable.isEmpty());

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), updateOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 손님수 변경 테스트 - 등록되지 않은 주문 테이블의 경우")
    @Test
    void updateOrderTableNumberOfGuest3() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 4, false);
        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 손님수 변경 테스트 - 빈 주문 테이블의 경우")
    @Test
    void updateOrderTableNumberOfGuest4() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 4, true);
        OrderTable updateOrderTable = new OrderTable(orderTable.getId(), null, 6, orderTable.isEmpty());
        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), updateOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}