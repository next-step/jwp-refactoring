package kitchenpos.application;

import kitchenpos.order.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

  @Mock
  private OrderDao orderDao;

  @Mock
  private OrderTableDao orderTableDao;

  @Mock
  private TableGroupDao tableGroupDao;

  @DisplayName("그룹으로 지정될 테이블 목록을 입력받아 테이블 그룹을 저장할 수 있다.")
  @Test
  void createTest() {
    //given
    OrderTable savedOrderTable1 = new OrderTable(1L, null, 4, true);
    OrderTable savedOrderTable2 = new OrderTable(2L, null, 2, true);
    OrderTable tableParameter1 = new OrderTable();
    tableParameter1.setId(savedOrderTable1.getId());
    OrderTable tableParameter2 = new OrderTable();
    tableParameter2.setId(savedOrderTable2.getId());
    TableGroup tableGroupParameter = new TableGroup(Arrays.asList(tableParameter1, tableParameter2));
    when(orderTableDao.findAllByIdIn(any())).thenReturn(Arrays.asList(savedOrderTable1, savedOrderTable2));
    TableGroup savedTableGroup = new TableGroup(1L, LocalDateTime.of(2021, 6, 30, 1, 30), Arrays.asList(tableParameter1, tableParameter2));
    when(tableGroupDao.save(any())).thenReturn(savedTableGroup);
    TableGroupService tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

    //when
    TableGroup createdTableGroup = tableGroupService.create(tableGroupParameter);

    //then
    assertAll(
        () -> assertThat(createdTableGroup.getId()).isNotNull(),
        () -> assertThat(isFilledTableGroupId(createdTableGroup.getOrderTables())).isTrue(),
        () -> assertThat(isNotEmptyTables(createdTableGroup.getOrderTables())).isTrue()
    );
  }

  private boolean isFilledTableGroupId(List<OrderTable> orderTables) {
    return orderTables.stream()
            .allMatch(orderTable -> orderTable.getTableGroupId() != null);
  }

  private boolean isNotEmptyTables(List<OrderTable> orderTables) {
    return orderTables.stream()
        .noneMatch(OrderTable::isEmpty);
  }

  @DisplayName("그룹으로 지정되는 테이블은 2개 이상이어야 한다.")
  @Test
  void createFailCauseTableCountTest() {
    //given
    OrderTable tableParameter = new OrderTable();
    tableParameter.setId(1L);
    TableGroup singleTableParameter = new TableGroup(Arrays.asList(tableParameter));
    TableGroup emptyTableParameter = new TableGroup();
    TableGroupService tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

    //when & then
    assertAll(
        () -> assertThatThrownBy(() -> tableGroupService.create(singleTableParameter)).isInstanceOf(IllegalArgumentException.class),
        () -> assertThatThrownBy(() -> tableGroupService.create(emptyTableParameter)).isInstanceOf(IllegalArgumentException.class)
    );
  }

  @DisplayName("그룹으로 지정되는 테이블은 서로 다른 테이블이어야 한다.")
  @Test
  void createFailCauseDuplicateTableTest() {
    //given
    OrderTable savedOrderTable1 = new OrderTable(1L, null, 4, true);
    OrderTable tableParameter1 = new OrderTable();
    tableParameter1.setId(savedOrderTable1.getId());
    OrderTable tableParameter2 = new OrderTable();
    tableParameter2.setId(savedOrderTable1.getId());
    TableGroup tableGroupParameter = new TableGroup(Arrays.asList(tableParameter1, tableParameter2));
    when(orderTableDao.findAllByIdIn(any())).thenReturn(Arrays.asList(savedOrderTable1));
    TableGroupService tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

    //when & then
    assertThatThrownBy(() -> tableGroupService.create(tableGroupParameter)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("주문을 등록할 수 없는 테이블이어야 한다.")
  @Test
  void createFailCauseNotEmptyTableTest() {
    //given
    OrderTable notEmptyTable = new OrderTable(1L, null, 4, false);
    OrderTable savedOrderTable2 = new OrderTable(2L, null, 2, true);
    OrderTable tableParameter1 = new OrderTable();
    tableParameter1.setId(notEmptyTable.getId());
    OrderTable tableParameter2 = new OrderTable();
    tableParameter2.setId(savedOrderTable2.getId());
    TableGroup tableGroupParameter = new TableGroup(Arrays.asList(tableParameter1, tableParameter2));
    when(orderTableDao.findAllByIdIn(any())).thenReturn(Arrays.asList(notEmptyTable, savedOrderTable2));
    TableGroupService tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

    //when & then
    assertThatThrownBy(() -> tableGroupService.create(tableGroupParameter)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("이미 다른 테이블 그룹에 묶여있지 않아야 한다.")
  @Test
  void createFailCauseAlreadyConnectedOtherGroupTest() {
    //given
    OrderTable otherGroupConnectedTable = new OrderTable(1L, 1L, 4, true);
    OrderTable savedOrderTable2 = new OrderTable(2L, null, 2, true);
    OrderTable tableParameter1 = new OrderTable();
    tableParameter1.setId(otherGroupConnectedTable.getId());
    OrderTable tableParameter2 = new OrderTable();
    tableParameter2.setId(savedOrderTable2.getId());
    TableGroup tableGroupParameter = new TableGroup(Arrays.asList(tableParameter1, tableParameter2));
    when(orderTableDao.findAllByIdIn(any())).thenReturn(Arrays.asList(otherGroupConnectedTable, savedOrderTable2));
    TableGroupService tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

    //when & then
    assertThatThrownBy(() -> tableGroupService.create(tableGroupParameter)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("테이블 그룹 아이디를 입력받아 테이블 그룹을 제거할 수 있다.")
  @Test
  void ungroupTest() {
    //given
    long savedGroupId = 1L;
    OrderTable savedOrderTable1 = new OrderTable(1L, savedGroupId, 4, false);
    OrderTable savedOrderTable2 = new OrderTable(2L, savedGroupId, 2, false);
    List<OrderTable> tablesByGroupId = Arrays.asList(savedOrderTable1, savedOrderTable2);
    when(orderTableDao.findAllByTableGroupId(savedGroupId)).thenReturn(tablesByGroupId);
    when(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(false);
    TableGroupService tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

    //when
    tableGroupService.ungroup(savedGroupId);

    //then
    assertThat(isAllEmptyTableGroupId(tablesByGroupId)).isTrue();
  }

  private boolean isAllEmptyTableGroupId(List<OrderTable> orderTables) {
    return orderTables.stream()
        .allMatch(orderTable -> orderTable.getTableGroupId() == null);
  }

  @DisplayName("주문의 상태가 진행 중 (조리 or 식사)이지 않아야 한다.")
  @Test
  void ungroupFailCauseOrderStatusTest() {
    //given
    long savedGroupId = 1L;
    OrderTable savedOrderTable1 = new OrderTable(1L, savedGroupId, 4, false);
    OrderTable savedOrderTable2 = new OrderTable(2L, savedGroupId, 2, false);
    List<OrderTable> tablesByGroupId = Arrays.asList(savedOrderTable1, savedOrderTable2);
    when(orderTableDao.findAllByTableGroupId(savedGroupId)).thenReturn(tablesByGroupId);
    when(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(true);
    TableGroupService tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

    //when & then
    assertThatThrownBy(() -> tableGroupService.ungroup(savedGroupId)).isInstanceOf(IllegalArgumentException.class);
  }

}
