package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTableEntity;
import kitchenpos.table.domain.TableRepository;
import kitchenpos.table.dto.TableRequest;
import org.junit.jupiter.api.BeforeEach;
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
class TableService2Test {

  @Mock
  private OrderRepository orderRepository;

  @Mock
  private TableRepository tableRepository;

  private TableService2 tableService;

  @BeforeEach
  void setUp() {
    tableService = new TableService2(orderRepository, tableRepository);
  }

  @DisplayName("손님수, 주문을 등록할 수 있는 테이블 여부를 입력받아 저장할 수 있다.")
  @Test
  void createTest() {
    //given
    TableRequest tableRequest = new TableRequest(4, true);
    when(tableRepository.save(any())).thenReturn(OrderTableEntity.initWithId(1L, 4, true));
    //when
    TableResponse saved = tableService.create(tableRequest);

    //then
    assertAll(
        () -> assertThat(saved.getId()).isNotNull(),
        () -> assertThat(saved.getTableGroupId()).isNull(),
        () -> assertThat(saved.getNumberOfGuests()).isEqualTo(tableRequest.getNumberOfGuests()),
        () -> assertThat(saved.isEmpty()).isEqualTo(tableRequest.isEmpty())
    );
    verify(tableRepository, VerificationModeFactory.times(1)).save(any());
  }

  @DisplayName("전체 테이블 목록을 조회할 수 있다.")
  @Test
  void findAllTest() {
    //given
    OrderTableEntity orderTableEntity1 = OrderTableEntity.initWithId(1L, 4, true);
    OrderTableEntity orderTableEntity2 = OrderTableEntity.initWithId(2L, 2, true);
    when(tableRepository.findAll()).thenReturn(Arrays.asList(orderTableEntity1, orderTableEntity2));

    //when
    List<TableResponse> orderTableList = tableService.findAllTables();

    //then
    assertThat(orderTableList).contains(TableResponse.from(orderTableEntity1), TableResponse.from(orderTableEntity2));
  }

  @DisplayName("테이블 ID와 주문을 등록할 수 있는 테이블 여부를 입력받아 테이블의 주문을 등록할 수 있는 테이블 여부를 변경할 수 있다.")
  @Test
  void changeEmptyTest() {
    //given
    TableRequest tableRequest = new TableRequest(null, false);
    long savedOrderTableId = 1L;
    OrderTableEntity savedOrderTable = OrderTableEntity.initWithId(savedOrderTableId, 4, true);
    when(tableRepository.findById(savedOrderTableId)).thenReturn(Optional.of(savedOrderTable));
    when(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), any())).thenReturn(false);
    OrderTableEntity orderAfterChangeEmpty = OrderTableEntity.initWithId(savedOrderTableId, 4, false);
    when(orderTableDao.save(any())).thenReturn(orderAfterChangeEmpty);
    //when
    TableResponse changedOrderTable = tableService.changeEmpty(savedOrderTableId, tableRequest);

    //then
    assertThat(changedOrderTable).isEqualTo(TableResponse.from(orderAfterChangeEmpty));
  }

  @DisplayName("존재하는 테이블만 상태를 변경할 수 있다.")
  @Test
  void changeEmptyFailCauseNotExistTableTest() {
    //given
    TableRequest tableRequest = new TableRequest(null, false);
    long notExistTableId = 1L;
    when(tableRepository.findById(notExistTableId)).thenReturn(Optional.empty());

    //when & then
    assertThatThrownBy(() -> tableService.changeEmpty(notExistTableId, tableRequest)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("연결된 테이블 그룹이 존재하지않아야 한다.")
  @Test
  void changeEmptyFailCauseConnectedTableGroupTest() {
    //given
    long savedOrderTableId = 1L;
    TableRequest tableRequest = new TableRequest(null, false);
    OrderTableEntity savedOrderTable = OrderTableEntity.initWithAll(savedOrderTableId, 1L, 4, true);
    when(tableRepository.findById(savedOrderTableId)).thenReturn(Optional.of(savedOrderTable));

    //when & then
    assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTableId, tableRequest)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("주문이 상태가 진행 중 (조리 or 식사)이지 않아야 한다.")
  @Test
  void changeEmptyFailCauseOrderStatusTest() {
    //given
    long savedOrderTableId = 1L;
    TableRequest tableRequest = new TableRequest(null, false);
    OrderTableEntity savedOrderTable = OrderTableEntity.initWithId(savedOrderTableId, 4, true);
    when(tableRepository.findById(savedOrderTableId)).thenReturn(Optional.of(savedOrderTable));
    when(orderRepository.existsByOrderTableIdAndOrderStatusIn(savedOrderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))).thenReturn(true);
    //when & then
    assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTableId, tableRequest)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("테이블 ID와 손님수를 입력받아 테이블의 손님수를 변경할 수 있다.")
  @Test
  void changeNumberOfGuestsTest() {
    //given
    TableRequest tableRequest = new TableRequest(10, null);
    long savedOrderTableId = 1L;
    OrderTableEntity savedOrderTable = OrderTableEntity.initWithId(savedOrderTableId, 4, false);
    when(tableRepository.findById(savedOrderTableId)).thenReturn(Optional.of(savedOrderTable));
    OrderTableEntity orderAfterChangeEmpty = OrderTableEntity.initWithId(savedOrderTableId, 10, false);

    //when
    TableResponse changedOrderTable = tableService.changeNumberOfGuests(savedOrderTableId, tableRequest);

    //then
    assertThat(changedOrderTable).isEqualTo(TableResponse.from(orderAfterChangeEmpty));
  }

  @DisplayName("손님 수는 0 이상이어야 한다.")
  @Test
  void changeNumberOfGuestsCauseNegativeNumberTest() {
    //given
    TableRequest tableRequest = new TableRequest(-1, null);

    //when & then
    assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, tableRequest)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("존재하는 테이블만 손님수를 변경할 수 있다.")
  @Test
  void changeNumberOfGuestsCauseNotExistTableTest() {
    //given
    long notExistTableId = 1L;
    TableRequest tableRequest = new TableRequest(10, null);
    when(tableRepository.findById(notExistTableId)).thenReturn(Optional.empty());

    //when & then
    assertThatThrownBy(() -> tableService.changeNumberOfGuests(notExistTableId, tableRequest)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("주문을 등록할 수 있는 테이블만 손님수를 변경할 수 있다.")
  @Test
  void changeNumberOfGuestsCauseEmptyTableTest() {
    //given
    long savedOrderTableId = 1L;
    TableRequest tableRequest = new TableRequest(10, null);
    OrderTableEntity savedOrderTable = OrderTableEntity.initWithId(savedOrderTableId, 4, true);
    when(tableRepository.findById(savedOrderTableId)).thenReturn(Optional.of(savedOrderTable));

    //when & then
    assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTableId, tableRequest)).isInstanceOf(IllegalArgumentException.class);
  }

}
