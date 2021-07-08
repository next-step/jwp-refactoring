package kitchenpos.tablegroup.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.TableGroupService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.utils.domain.OrderTableObjects;
import kitchenpos.utils.domain.TableGroupObjects;

@DisplayName("단체 지정 서비스")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private TableGroupDao tableGroupDao;
    @InjectMocks
    private TableGroupService tableGroupService;
    private TableGroup createTableGroup;
    private TableGroup ungroupTableGroup;
    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        TableGroupObjects tableGroupObjects = new TableGroupObjects();
        OrderTableObjects orderTableObjects = new OrderTableObjects();
        createTableGroup = tableGroupObjects.getTableGroup1();
        ungroupTableGroup = tableGroupObjects.getTableGroup2();
        orderTable1 = orderTableObjects.getOrderTable1();
        orderTable2 = orderTableObjects.getOrderTable2();

    }

    @Test
    @DisplayName("단체지정 등록")
    void create_group() {
        // given
        orderTable1.setTableGroupId(null);
        orderTable2.setTableGroupId(null);
        createTableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

        // mocking
        when(orderTableDao.findAllByIdIn(any(List.class))).thenReturn(Arrays.asList(orderTable1, orderTable2));
        when(tableGroupDao.save(any(TableGroup.class))).thenReturn(createTableGroup);
        when(orderTableDao.save(orderTable1)).thenReturn(orderTable1);
        when(orderTableDao.save(orderTable2)).thenReturn(orderTable2);

        // when
        TableGroup resultTableGroup = tableGroupService.create(createTableGroup);

        // then
        assertAll(
                () -> assertThat(resultTableGroup.getId()).isEqualTo(createTableGroup.getId()),
                () -> assertThat(resultTableGroup.getOrderTables()).extracting("id").contains(orderTable1.getId(), orderTable2.getId())
        );
    }

    @TestFactory
    @DisplayName("단체지정 등록 오류")
    List<DynamicTest> group_exception() {
        return Arrays.asList(
                dynamicTest("단체지정 테이블이 없는 경우 오류 발생.", () -> {
                    // given
                    createTableGroup.setOrderTables(null);

                    assertThatThrownBy(() -> tableGroupService.create(createTableGroup))
                            .isInstanceOf(IllegalArgumentException.class);
                }),
                dynamicTest("단체지정 테이블이 2개 이상이 아닐 경우 오류 발생.", () -> {
                    // given
                    createTableGroup.setOrderTables(Arrays.asList(orderTable1));

                    assertThatThrownBy(() -> tableGroupService.create(createTableGroup))
                            .isInstanceOf(IllegalArgumentException.class);
                }),
                dynamicTest("단체지정 테이블 중 등록되지 않은 테이블이 존재할 경우 오류 발생.", () -> {
                    // given
                    createTableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

                    // mocking
                    when(orderTableDao.findAllByIdIn(any(List.class))).thenReturn(Arrays.asList(orderTable1));

                    assertThatThrownBy(() -> tableGroupService.create(createTableGroup))
                            .isInstanceOf(IllegalArgumentException.class);
                }),
                dynamicTest("단체지정 테이블 중 비어있지 않은 테이블이 존재할 경우 오류 발생.", () -> {
                    // given
                    orderTable1.setEmpty(true);
                    orderTable2.setEmpty(false);
                    createTableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

                    // mocking
                    when(orderTableDao.findAllByIdIn(any(List.class))).thenReturn(Arrays.asList(orderTable1, orderTable2));

                    assertThatThrownBy(() -> tableGroupService.create(createTableGroup))
                            .isInstanceOf(IllegalArgumentException.class);
                }),
                dynamicTest("단체지정 테이블 중 테이블 그룹이 지정되어 있는 테이블이 존재할 경우 오류 발생.", () -> {
                    // given
                    orderTable1.setEmpty(true);
                    orderTable1.setTableGroupId(null);
                    orderTable2.setEmpty(true);
                    orderTable2.setTableGroupId(createTableGroup.getId());
                    createTableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

                    // mocking
                    when(orderTableDao.findAllByIdIn(any(List.class))).thenReturn(Arrays.asList(orderTable1, orderTable2));

                    assertThatThrownBy(() -> tableGroupService.create(createTableGroup))
                            .isInstanceOf(IllegalArgumentException.class);
                })
        );
    }

    @Test
    @DisplayName("단체지정 취소")
    void ungroup() {
        // given
        orderTable1.setTableGroupId(ungroupTableGroup.getId());
        orderTable2.setTableGroupId(ungroupTableGroup.getId());

        // mocking
        when(orderTableDao.findAllByTableGroupId(anyLong())).thenReturn(Arrays.asList(orderTable1, orderTable2));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(List.class), any(List.class))).thenReturn(false);

        // when
        tableGroupService.ungroup(ungroupTableGroup.getId());

        // then
        assertAll(
                () -> assertThat(orderTable1.getTableGroupId()).isNull(),
                () -> assertThat(orderTable2.getTableGroupId()).isNull()
        );
    }

    @TestFactory
    @DisplayName("단체지정 취소 오류")
    List<DynamicTest> ungroup_exception() {
        return Arrays.asList(
                dynamicTest("테이블들의 주문 상태가 COOKING이거나 MEAL인 상태가 존재하는 경우 오류 발생.", () -> {
                    when(orderTableDao.findAllByTableGroupId(anyLong())).thenReturn(Arrays.asList(orderTable1, orderTable2));
                    when(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(List.class), any(List.class))).thenReturn(true);

                    assertThatThrownBy(() -> tableGroupService.ungroup(createTableGroup.getId()))
                            .isInstanceOf(IllegalArgumentException.class);
                })
        );
    }
}
