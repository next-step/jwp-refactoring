package kitchenpos.table.application;

import kitchenpos.order.dao.OrderDao;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

  @Mock
  private OrderDao orderDao;

  @Mock
  private OrderTableDao orderTableDao;

  @DisplayName("손님수, 주문을 등록할 수 있는 테이블 여부를 입력받아 저장할 수 있다.")
  @Test
  void createTest() {
    //given
    OrderTable toSave = new OrderTable(4, true);
    when(orderTableDao.save(any())).thenReturn(new OrderTable(1L, null, 4, true));
    TableService tableService = new TableService(orderDao, orderTableDao);

    //when
    OrderTable saved = tableService.create(toSave);

    //then
    assertAll(
        () -> assertThat(saved.getId()).isNotNull(),
        () -> assertThat(saved.getTableGroupId()).isNull(),
        () -> assertThat(saved.getNumberOfGuests()).isEqualTo(toSave.getNumberOfGuests()),
        () -> assertThat(saved.isEmpty()).isEqualTo(toSave.isEmpty())
    );
    verify(orderTableDao, VerificationModeFactory.times(1)).save(any());
  }

  @DisplayName("전체 테이블 목록을 조회할 수 있다.")
  @Test
  void findAllTest() {
    //given
    OrderTable orderTable1 = new OrderTable(1L, null, 4, true);
    OrderTable orderTable2 = new OrderTable(2L, null, 2, true);
    when(orderTableDao.findAll()).thenReturn(Arrays.asList(orderTable1, orderTable2));
    TableService tableService = new TableService(orderDao, orderTableDao);

    //when
    List<OrderTable> orderTableList = tableService.list();

    //then
    assertThat(orderTableList).containsExactly(orderTable1, orderTable2);
  }

  @DisplayName("테이블 ID와 주문을 등록할 수 있는 테이블 여부를 입력받아 테이블의 주문을 등록할 수 있는 테이블 여부를 변경할 수 있다.")
  @Test
  void changeEmptyTest() {
    //given
    OrderTable changeParameter = new OrderTable();
    changeParameter.setEmpty(false);
    long savedOrderTableId = 1L;
    OrderTable savedOrderTable = new OrderTable(savedOrderTableId, null, 4, true);
    when(orderTableDao.findById(savedOrderTableId)).thenReturn(Optional.of(savedOrderTable));
    when(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).thenReturn(false);
    OrderTable orderAfterChangeEmpty = new OrderTable(savedOrderTableId, null, 4, false);
    when(orderTableDao.save(any())).thenReturn(orderAfterChangeEmpty);
    TableService tableService = new TableService(orderDao, orderTableDao);

    //when
    OrderTable changedOrderTable = tableService.changeEmpty(savedOrderTableId, changeParameter);

    //then
    assertThat(changedOrderTable).isEqualTo(orderAfterChangeEmpty);
  }

  @DisplayName("존재하는 테이블만 상태를 변경할 수 있다.")
  @Test
  void changeEmptyFailCauseNotExistTableTest() {
    //given
    long notExistTableId = 1L;
    when(orderTableDao.findById(notExistTableId)).thenReturn(Optional.empty());
    TableService tableService = new TableService(orderDao, orderTableDao);

    //when & then
    assertThatThrownBy(() -> tableService.changeEmpty(notExistTableId, new OrderTable())).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("연결된 테이블 그룹이 존재하지않아야 한다.")
  @Test
  void changeEmptyFailCauseConnectedTableGroupTest() {
    //given
    long savedOrderTableId = 1L;
    OrderTable savedOrderTable = new OrderTable(savedOrderTableId, 1L, 4, true);
    when(orderTableDao.findById(savedOrderTableId)).thenReturn(Optional.of(savedOrderTable));
    TableService tableService = new TableService(orderDao, orderTableDao);

    //when & then
    assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTableId, new OrderTable())).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("주문이 상태가 진행 중 (조리 or 식사)이지 않아야 한다.")
  @Test
  void changeEmptyFailCauseOrderStatusTest() {
    //given
    long savedOrderTableId = 1L;
    OrderTable savedOrderTable = new OrderTable(savedOrderTableId, null, 4, true);
    when(orderTableDao.findById(savedOrderTableId)).thenReturn(Optional.of(savedOrderTable));
    when(orderDao.existsByOrderTableIdAndOrderStatusIn(savedOrderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);
    TableService tableService = new TableService(orderDao, orderTableDao);

    //when & then
    assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTableId, new OrderTable())).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("테이블 ID와 손님수를 입력받아 테이블의 손님수를 변경할 수 있다.")
  @Test
  void changeNumberOfGuestsTest() {
    //given
    OrderTable changeParameter = new OrderTable();
    changeParameter.setNumberOfGuests(10);
    long savedOrderTableId = 1L;
    OrderTable savedOrderTable = new OrderTable(savedOrderTableId, null, 4, false);
    when(orderTableDao.findById(savedOrderTableId)).thenReturn(Optional.of(savedOrderTable));
    OrderTable orderAfterChangeEmpty = new OrderTable(savedOrderTableId, null, 10, false);
    when(orderTableDao.save(any())).thenReturn(orderAfterChangeEmpty);
    TableService tableService = new TableService(orderDao, orderTableDao);

    //when
    OrderTable changedOrderTable = tableService.changeNumberOfGuests(savedOrderTableId, changeParameter);

    //then
    assertThat(changedOrderTable).isEqualTo(orderAfterChangeEmpty);
  }

  @DisplayName("손님 수는 0 이상이어야 한다.")
  @Test
  void changeNumberOfGuestsCauseNegativeNumberTest() {
    //given
    OrderTable parameter = new OrderTable();
    parameter.setNumberOfGuests(-1);
    TableService tableService = new TableService(orderDao, orderTableDao);

    //when & then
    assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, parameter)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("존재하는 테이블만 손님수를 변경할 수 있다.")
  @Test
  void changeNumberOfGuestsCauseNotExistTableTest() {
    //given
    long notExistTableId = 1L;
    when(orderTableDao.findById(notExistTableId)).thenReturn(Optional.empty());
    TableService tableService = new TableService(orderDao, orderTableDao);

    //when & then
    assertThatThrownBy(() -> tableService.changeNumberOfGuests(notExistTableId, new OrderTable())).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("주문을 등록할 수 있는 테이블만 손님수를 변경할 수 있다.")
  @Test
  void changeNumberOfGuestsCauseEmptyTableTest() {
    //given
    long savedOrderTableId = 1L;
    OrderTable savedOrderTable = new OrderTable(savedOrderTableId, null, 4, true);
    when(orderTableDao.findById(savedOrderTableId)).thenReturn(Optional.of(savedOrderTable));
    TableService tableService = new TableService(orderDao, orderTableDao);

    //when & then
    assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTableId, new OrderTable())).isInstanceOf(IllegalArgumentException.class);
  }

}
