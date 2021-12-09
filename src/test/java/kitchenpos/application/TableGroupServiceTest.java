package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static kitchenpos.application.TableServiceTest.orderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("주문 테이블 그룹 관리")
class TableGroupServiceTest {

    private OrderDao orderDao;
    private OrderTableDao orderTableDao;
    private TableGroupDao tableGroupDao;
    private TableGroupService tableGroupService;

    public static TableGroup tableGroup(Long id) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(id);
        tableGroup.setCreatedDate(LocalDateTime.now());
        return tableGroup;
    }

    public static TableGroup setOrderTables(TableGroup tableGroup, int size) {
        List<OrderTable> orderTables = new ArrayList<>();
        IntStream.rangeClosed(1, size)
                .forEach(it ->
                        orderTables.add(orderTable(Long.valueOf(it), null, it + 1, true))
                );
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }

    private void setMockCreateData(TableGroup tableGroup) {
        when(orderTableDao.findAllByIdIn(anyList())).thenReturn(tableGroup.getOrderTables());
        when(tableGroupDao.save(any(TableGroup.class))).thenReturn(tableGroup);
        tableGroup.getOrderTables().stream().forEach(it -> when(orderTableDao.save(any(OrderTable.class))).thenReturn(it));
    }

    private void setMockUnGroupData(TableGroup tableGroup, boolean orderStatus) {
        when(orderTableDao.findAllByTableGroupId(anyLong())).thenReturn(tableGroup.getOrderTables());
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).thenReturn(orderStatus);
        tableGroup.getOrderTables().stream().forEach(it -> when(orderTableDao.save(any(OrderTable.class))).thenReturn(it));
    }

    @BeforeEach
    void setUp() {
        orderDao = mock(OrderDao.class);
        orderTableDao = mock(OrderTableDao.class);
        tableGroupDao = mock(TableGroupDao.class);
        tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
    }

    @Nested
    @DisplayName("주문 테이블 그룹 생성")
    class CreateTableGroup {
        @Test
        @DisplayName("성공")
        void createTableGroupSuccess() {
            // given
            final TableGroup tableGroup = tableGroup(1L);
            setOrderTables(tableGroup, 3);
            setMockCreateData(tableGroup);

            // when
            final TableGroup actual = tableGroupService.create(tableGroup);

            // then
            assertAll(
                    () -> assertThat(actual).isEqualTo(tableGroup),
                    () -> assertThat(actual.getOrderTables()).hasSize(3),
                    () -> assertThat(actual.getOrderTables()).containsAll(tableGroup.getOrderTables()),
                    () -> assertThat(actual.getOrderTables()).filteredOnNull("tableGroupId").isNotNull()
            );
        }

        @Test
        @DisplayName("실패 - 그룹 조건 최소 테이블 수 미달")
        void createFailOrderTableSize() {
            // given
            final TableGroup tableGroup = tableGroup(1L);
            setOrderTables(tableGroup, 1);
            setMockCreateData(tableGroup);

            // when
            assertThatThrownBy(() -> {
                TableGroup actual = tableGroupService.create(tableGroup);
            }).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("실패 - 요청 주문 테이블이 비어있지 않음")
        void createFailOrderTableNotEmpty() {
            // given
            final TableGroup tableGroup = tableGroup(1L);
            setOrderTables(tableGroup, 3);
            tableGroup.getOrderTables().get(1).setEmpty(false);
            setMockCreateData(tableGroup);

            // when
            assertThatThrownBy(() -> {
                TableGroup actual = tableGroupService.create(tableGroup);
            }).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("실패 - 요청 주문 테이블 중 그룹이 존재")
        void createFailOrderTableExistsTableGroupId() {
            // given
            final TableGroup tableGroup = tableGroup(1L);
            setOrderTables(tableGroup, 3);
            tableGroup.getOrderTables().get(1).setTableGroupId(10L);
            setMockCreateData(tableGroup);

            // when
            assertThatThrownBy(() -> {
                TableGroup actual = tableGroupService.create(tableGroup);
            }).isInstanceOf(IllegalArgumentException.class);
        }
    }


    @Nested
    @DisplayName("주문 테이블 그룹 해제")
    class UnTableGroup {
        @Test
        @DisplayName("성공")
        void tableUnGroupSuccess() {
            // given
            final TableGroup tableGroup = tableGroup(1L);
            setOrderTables(tableGroup, 3);
            boolean 조리중 = false;
            setMockUnGroupData(tableGroup, 조리중);

            // when
            tableGroupService.ungroup(tableGroup.getId());
        }

        @Test
        @DisplayName("실패 - 조리 중인 테이블이 존재")
        void tableUnGroupFailExistsCooking() {
            // given
            final TableGroup tableGroup = tableGroup(1L);
            setOrderTables(tableGroup, 3);
            boolean 조리중 = true;
            setMockUnGroupData(tableGroup, 조리중);

            // when
            assertThatThrownBy(() -> {
                tableGroupService.ungroup(tableGroup.getId());
            }).isInstanceOf(IllegalArgumentException.class);
        }
    }

}
