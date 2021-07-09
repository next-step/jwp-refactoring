package kitchenpos.tablegroup.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTableEntity;
import kitchenpos.table.domain.TableRepository;
import kitchenpos.table.dto.TableResponse;
import kitchenpos.tablegroup.domain.TableGroupEntity;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableGroupService2Test {

  @Mock
  private OrderRepository orderRepository;

  @Mock
  private TableRepository tableRepository;

  @Mock
  private TableGroupRepository tableGroupRepository;

  private TableGroupService2 tableGroupService;

  @BeforeEach
  void setUp() {
    tableGroupService = new TableGroupService2(orderRepository, tableRepository, tableGroupRepository);
  }

  @DisplayName("그룹으로 지정될 테이블 목록을 입력받아 테이블 그룹을 저장할 수 있다.")
  @Test
  void createTest() {
    //given
    TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(new TableGroupRequest.TableGroupId(1L), new TableGroupRequest.TableGroupId(2L)));
    OrderTableEntity savedOrderTable1 = OrderTableEntity.initWithId(1L, 4, true);
    OrderTableEntity savedOrderTable2 = OrderTableEntity.initWithId(2L, 2, true);
    when(tableRepository.findAllByIdIn(any())).thenReturn(Arrays.asList(savedOrderTable1, savedOrderTable2));
    TableGroupEntity savedTableGroup = new TableGroupEntity(1L, LocalDateTime.of(2021, 6, 30, 1, 30));
    when(tableGroupRepository.save(any())).thenReturn(savedTableGroup);

    //when
    TableGroupResponse createdTableGroup = tableGroupService.create(tableGroupRequest);

    //then
    assertAll(
        () -> assertThat(createdTableGroup.getId()).isNotNull(),
        () -> assertThat(isFilledTableGroupId(createdTableGroup.getOrderTables())).isTrue(),
        () -> assertThat(isNotEmptyTables(createdTableGroup.getOrderTables())).isTrue()
    );
  }

  private boolean isFilledTableGroupId(List<TableResponse> orderTables) {
    return orderTables.stream()
            .allMatch(orderTable -> orderTable.getTableGroupId() != null);
  }

  private boolean isNotEmptyTables(List<TableResponse> orderTables) {
    return orderTables.stream()
        .noneMatch(TableResponse::isEmpty);
  }

  @DisplayName("그룹으로 지정되는 테이블은 2개 이상이어야 한다.")
  @Test
  void createFailCauseTableCountTest() {
    //given
    TableGroupRequest singleTableRequest = new TableGroupRequest(Arrays.asList(new TableGroupRequest.TableGroupId(1L)));
    TableGroupRequest emptyTableRequest = new TableGroupRequest(Collections.emptyList());

    //when & then
    assertAll(
        () -> assertThatThrownBy(() -> tableGroupService.create(singleTableRequest)).isInstanceOf(IllegalArgumentException.class),
        () -> assertThatThrownBy(() -> tableGroupService.create(emptyTableRequest)).isInstanceOf(IllegalArgumentException.class)
    );
  }

  @DisplayName("그룹으로 지정되는 테이블은 서로 다른 테이블이어야 한다.")
  @Test
  void createFailCauseDuplicateTableTest() {
    //given
    OrderTableEntity savedOrderTable1 = OrderTableEntity.initWithId(1L, 4, true);
    TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(new TableGroupRequest.TableGroupId(savedOrderTable1.getId()),
                                                                              new TableGroupRequest.TableGroupId(savedOrderTable1.getId())));
    when(tableRepository.findAllByIdIn(any())).thenReturn(Collections.singletonList(savedOrderTable1));

    //when & then
    assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("주문을 등록할 수 없는 테이블이어야 한다.")
  @Test
  void createFailCauseNotEmptyTableTest() {
    //given
    OrderTableEntity notEmptyTable = OrderTableEntity.initWithId(1L, 4, false);
    OrderTableEntity savedOrderTable2 = OrderTableEntity.initWithId(2L, 2, true);
    TableGroupEntity savedTableGroup = new TableGroupEntity(1L, LocalDateTime.of(2021, 6, 30, 1, 30));
    TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(new TableGroupRequest.TableGroupId(notEmptyTable.getId()),
                                                                            new TableGroupRequest.TableGroupId(savedOrderTable2.getId())));
    when(tableRepository.findAllByIdIn(any())).thenReturn(Arrays.asList(notEmptyTable, savedOrderTable2));
    when(tableGroupRepository.save(any())).thenReturn(savedTableGroup);

    //when & then
    assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("이미 다른 테이블 그룹에 묶여있지 않아야 한다.")
  @Test
  void createFailCauseAlreadyConnectedOtherGroupTest() {
    //given
    OrderTableEntity otherGroupConnectedTable = OrderTableEntity.initWithAll(1L, 1L, 4, true);
    OrderTableEntity savedOrderTable2 = OrderTableEntity.initWithId(2L, 2, true);
    TableGroupEntity savedTableGroup = new TableGroupEntity(1L, LocalDateTime.of(2021, 6, 30, 1, 30));
    TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(new TableGroupRequest.TableGroupId(otherGroupConnectedTable.getId()),
                                                                new TableGroupRequest.TableGroupId(savedOrderTable2.getId())));
    when(tableRepository.findAllByIdIn(any())).thenReturn(Arrays.asList(otherGroupConnectedTable, savedOrderTable2));
    when(tableGroupRepository.save(any())).thenReturn(savedTableGroup);

    //when & then
    assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("테이블 그룹 아이디를 입력받아 테이블 그룹을 제거할 수 있다.")
  @Test
  void ungroupTest() {
    //given
    long savedGroupId = 1L;
    OrderTableEntity savedOrderTable1 = OrderTableEntity.initWithAll(1L, savedGroupId, 4, false);
    OrderTableEntity savedOrderTable2 = OrderTableEntity.initWithAll(2L, savedGroupId, 2, false);
    List<OrderTableEntity> tablesByGroupId = Arrays.asList(savedOrderTable1, savedOrderTable2);
    when(tableRepository.findAllByTableGroupId(savedGroupId)).thenReturn(tablesByGroupId);
    when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(false);

    //when
    tableGroupService.ungroup(savedGroupId);

    //then
    assertThat(isAllEmptyTableGroupId(tablesByGroupId)).isTrue();
  }

  private boolean isAllEmptyTableGroupId(List<OrderTableEntity> orderTables) {
    return orderTables.stream()
        .allMatch(orderTable -> orderTable.getTableGroupId() == null);
  }

  @DisplayName("주문의 상태가 진행 중 (조리 or 식사)이지 않아야 한다.")
  @Test
  void ungroupFailCauseOrderStatusTest() {
    //given
    long savedGroupId = 1L;
    OrderTableEntity savedOrderTable1 = OrderTableEntity.initWithAll(1L, savedGroupId, 4, false);
    OrderTableEntity savedOrderTable2 = OrderTableEntity.initWithAll(2L, savedGroupId, 2, false);
    List<OrderTableEntity> tablesByGroupId = Arrays.asList(savedOrderTable1, savedOrderTable2);
    when(tableRepository.findAllByTableGroupId(savedGroupId)).thenReturn(tablesByGroupId);
    when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(true);

    //when & then
    assertThatThrownBy(() -> tableGroupService.ungroup(savedGroupId)).isInstanceOf(IllegalArgumentException.class);
  }

}
