//package kitchenpos.old.application;
//
//import kitchenpos.old.dao.OrderDao;
//import kitchenpos.table.OrderTableDao;
//import kitchenpos.old.dao.TableGroupDao;
//import kitchenpos.table.dto.OrderTable;
//import kitchenpos.old.domain.TableGroup;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyList;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class TableGroupServiceTest {
//
//    @Mock
//    private OrderDao orderDao;
//    @Mock
//    private OrderTableDao orderTableDao;
//    @Mock
//    private TableGroupDao tableGroupDao;
//
//    @InjectMocks
//    private TableGroupService tableGroupService;
//
//    @DisplayName("테이블 단체 지정")
//    @Test
//    public void createTest() {
//        OrderTable orderTable1 = new OrderTable();
//        orderTable1.setId(1l);
//        orderTable1.setEmpty(true);
//        OrderTable orderTable2 = new OrderTable();
//        orderTable2.setId(2l);
//        orderTable2.setEmpty(true);
//
//        List<OrderTable> orderTables = new ArrayList<>();
//        orderTables.add(orderTable1);
//        orderTables.add(orderTable2);
//
//        TableGroup tableGroup = new TableGroup();
//        tableGroup.setId(1l);
//        tableGroup.setOrderTables(orderTables);
//
//        given(orderTableDao.findAllByIdIn(any()))
//                .willReturn(orderTables);
//        given(tableGroupDao.save(any()))
//                .willReturn(tableGroup);
//
//        tableGroupService.create(tableGroup);
//
//        assertThat(tableGroup.getCreatedDate()).isNotNull();
//        assertThat(tableGroup.getOrderTables().size()).isEqualTo(2);
//        verify(orderTableDao, times(2))
//                .save(any());
//    }
//
//    @DisplayName("테이블 단체 지정 불가 케이스 1 - 1개 이하의 테이블을 단체 지정할 경우")
//    @Test
//    public void invalidCreateCase1() {
//        OrderTable orderTable1 = new OrderTable();
//        orderTable1.setId(1l);
//        orderTable1.setEmpty(true);
//
//        List<OrderTable> orderTables = new ArrayList<>();
//        orderTables.add(orderTable1);
//
//        TableGroup tableGroup = new TableGroup();
//        tableGroup.setOrderTables(orderTables);
//        assertThatThrownBy(() -> {
//            tableGroupService.create(tableGroup);
//        }).isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @DisplayName("테이블 단체 지정 불가 케이스 2 - 중복된 테이블을 단체 지정할 경우")
//    @Test
//    public void invalidCreateCase2() {
//        OrderTable orderTable1 = new OrderTable();
//        orderTable1.setId(1l);
//        orderTable1.setEmpty(true);
//        OrderTable orderTable2 = new OrderTable();
//        orderTable2.setId(1l);
//        orderTable2.setEmpty(true);
//
//        List<OrderTable> orderTables = new ArrayList<>();
//        orderTables.add(orderTable1);
//        orderTables.add(orderTable2);
//
//        TableGroup tableGroup = new TableGroup();
//        tableGroup.setId(1l);
//        tableGroup.setOrderTables(orderTables);
//
//        given(orderTableDao.findAllByIdIn(any()))
//                .willReturn(Collections.emptyList());
//
//        assertThatThrownBy(() -> {
//            tableGroupService.create(tableGroup);
//        }).isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @DisplayName("테이블 단체 지정 불가 케이스 3 - 이미 단체 지정이 된 테이블이 포함된 경우")
//    @Test
//    public void invalidCreateCase3() {
//        OrderTable orderTable1 = new OrderTable();
//        orderTable1.setId(1l);
//        orderTable1.setEmpty(true);
//        orderTable1.setTableGroupId(1l);
//        OrderTable orderTable2 = new OrderTable();
//        orderTable2.setId(2l);
//        orderTable2.setEmpty(true);
//
//        List<OrderTable> orderTables = new ArrayList<>();
//        orderTables.add(orderTable1);
//        orderTables.add(orderTable2);
//
//        TableGroup tableGroup = new TableGroup();
//        tableGroup.setId(1l);
//        tableGroup.setOrderTables(orderTables);
//
//        given(orderTableDao.findAllByIdIn(any()))
//                .willReturn(orderTables);
//
//        assertThatThrownBy(() -> {
//            tableGroupService.create(tableGroup);
//        }).isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @DisplayName("테이블 단체 지정 불가 케이스 4 - 비어있는 테이블이 포함된 경우")
//    @Test
//    public void invalidCreateCase4() {
//        OrderTable orderTable1 = new OrderTable();
//        orderTable1.setId(1l);
//        orderTable1.setEmpty(false);
//        OrderTable orderTable2 = new OrderTable();
//        orderTable2.setId(2l);
//        orderTable2.setEmpty(true);
//
//        List<OrderTable> orderTables = new ArrayList<>();
//        orderTables.add(orderTable1);
//        orderTables.add(orderTable2);
//
//        TableGroup tableGroup = new TableGroup();
//        tableGroup.setId(1l);
//        tableGroup.setOrderTables(orderTables);
//
//        given(orderTableDao.findAllByIdIn(any()))
//                .willReturn(orderTables);
//
//        assertThatThrownBy(() -> {
//            tableGroupService.create(tableGroup);
//        }).isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @DisplayName("테이블 그룹해제 불가 테이스 - 상태가 적절하지 않을때")
//    @Test
//    public void invalidUngroupCase() {
//        given(orderTableDao.findAllByTableGroupId(any()))
//                .willReturn(Collections.emptyList());
//        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), anyList()))
//                .willReturn(true);
//        assertThatThrownBy(() -> {
//            tableGroupService.ungroup(1l);
//        }).isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @DisplayName("테이블 그룹 해제")
//    @Test
//    public void upgroup() {
//        OrderTable orderTable1 = mock(OrderTable.class);
//        OrderTable orderTable2 = mock(OrderTable.class);
//        List<OrderTable> orderTables = new ArrayList<OrderTable>() {{
//            add(orderTable1);
//            add(orderTable2);
//        }};
//
//        given(orderTableDao.findAllByTableGroupId(anyLong()))
//                .willReturn(orderTables);
//        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), anyList()))
//                .willReturn(false);
//
//        tableGroupService.ungroup(1l);
//
//        verify(orderTable1, times(1))
//                .setTableGroupId(null);
//        verify(orderTable2, times(1))
//                .setTableGroupId(null);
//        verify(orderTableDao, times(2))
//                .save(any());
//    }
//
//}
