package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("TableGroupService 클래스")
class TableGroupServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    private TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
    }

    @Nested
    @DisplayName("create 메서드는")
    class Describe_create {

        @Nested
        @DisplayName("테이블 그룹이 주어지면")
        class Context_with_table_group {
            TableGroup givenTableGroup = new TableGroup();

            @BeforeEach
            void setUp() {
                OrderTable orderTable1 = new OrderTable();
                orderTable1.setEmpty(true);
                OrderTable orderTable2 = new OrderTable();
                orderTable2.setEmpty(true);
                List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
                givenTableGroup.setOrderTables(orderTables);
                TableGroup savedOrderTable = new TableGroup();
                savedOrderTable.setId(1L);

                when(orderTableDao.findAllByIdIn(anyList()))
                        .thenReturn(orderTables);
                when(tableGroupDao.save(any(TableGroup.class)))
                        .thenReturn(givenTableGroup);
            }

            @Test
            @DisplayName("주어진 테이블 그룹을 저장하고, 저장된 객체를 리턴한다.")
            void it_returns_saved_table_group() {
                TableGroup actual = tableGroupService.create(givenTableGroup);

                assertThat(actual).isEqualTo(givenTableGroup);
            }
        }

        @Nested
        @DisplayName("테이블 그룹이 주문 테이블 없이 주어지면")
        class Context_with_table_group_and_no_order_table {
            TableGroup givenTableGroup = new TableGroup();

            @Test
            @DisplayName("예외를 던진다.")
            void it_throws_exception() {
                assertThatThrownBy(() -> tableGroupService.create(givenTableGroup))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("테이블 그룹이 주문 테이블 1개만 주어지면")
        class Context_with_table_group_and_one_order_table {
            TableGroup givenTableGroup = new TableGroup();

            @BeforeEach
            void setUp() {
                OrderTable orderTable1 = new OrderTable();
                orderTable1.setEmpty(true);
                List<OrderTable> orderTables = Collections.singletonList(orderTable1);
                givenTableGroup.setOrderTables(orderTables);
            }

            @Test
            @DisplayName("예외를 던진다.")
            void it_throws_exception() {
                assertThatThrownBy(() -> tableGroupService.create(givenTableGroup))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("저장된 주문 테이블 갯수와 그룹 지정할 주문 테이블 갯수가 다르게 주어지면")
        class Context_with_different_order_table_count {
            TableGroup givenTableGroup = new TableGroup();

            @BeforeEach
            void setUp() {
                OrderTable orderTable1 = new OrderTable();
                OrderTable orderTable2 = new OrderTable();
                List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
                givenTableGroup.setOrderTables(orderTables);

                when(orderTableDao.findAllByIdIn(anyList()))
                        .thenReturn(Arrays.asList(orderTable1));
            }

            @Test
            @DisplayName("예외를 던진다.")
            void it_throws_exception() {
                assertThatThrownBy(() -> tableGroupService.create(givenTableGroup))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("테이블 그룹이 주문 테이블이 비어있지 않게 주어지면")
        class Context_with_not_empty_order_table {
            TableGroup givenTableGroup = new TableGroup();

            @BeforeEach
            void setUp() {
                OrderTable orderTable1 = new OrderTable();
                orderTable1.setEmpty(false);
                OrderTable orderTable2 = new OrderTable();
                orderTable1.setEmpty(false);
                List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
                givenTableGroup.setOrderTables(orderTables);

                when(orderTableDao.findAllByIdIn(anyList()))
                        .thenReturn(orderTables);
            }

            @Test
            @DisplayName("예외를 던진다.")
            void it_throws_exception() {
                assertThatThrownBy(() -> tableGroupService.create(givenTableGroup))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("주문 테이블이 테이블 그룹에 포함되어있으면")
        class Context_with_order_table_has_group_id {
            TableGroup givenTableGroup = new TableGroup();

            @BeforeEach
            void setUp() {
                OrderTable orderTable1 = new OrderTable();
                orderTable1.setEmpty(true);
                orderTable1.setTableGroupId(1L);
                OrderTable orderTable2 = new OrderTable();
                orderTable1.setEmpty(true);
                orderTable1.setTableGroupId(1L);
                List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
                givenTableGroup.setOrderTables(orderTables);

                when(orderTableDao.findAllByIdIn(anyList()))
                        .thenReturn(orderTables);
            }

            @Test
            @DisplayName("예외를 던진다.")
            void it_throws_exception() {
                assertThatThrownBy(() -> tableGroupService.create(givenTableGroup))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    @Nested
    @DisplayName("ungroup 메서드는")
    class Describe_ungroup {

        @Nested
        @DisplayName("테이블 그룹을 해제할 식별자가 주어지면")
        class Context_with_table_group {
            Long givenTableGroupId = 1L;

            @BeforeEach
            void setUp() {
                OrderTable orderTable1 = new OrderTable();
                orderTable1.setEmpty(true);
                OrderTable orderTable2 = new OrderTable();
                orderTable2.setEmpty(true);
                List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);

                when(orderTableDao.findAllByTableGroupId(1L))
                        .thenReturn(orderTables);
            }

            @Test
            @DisplayName("변경사항을 저장한다.")
            void it_saves_table_ungroup() {
                tableGroupService.ungroup(givenTableGroupId);

                verify(orderTableDao, times(2))
                        .save(any(OrderTable.class));
            }
        }

        @Nested
        @DisplayName("저장된 테이블 그룹을 해제할 주문테이블이 완료가 아닌채 주어지면")
        class Context_with_table_status_is_not_complete {
            Long givenTableGroupId = 1L;

            @BeforeEach
            void setUp() {
                OrderTable orderTable1 = new OrderTable();
                orderTable1.setEmpty(true);
                OrderTable orderTable2 = new OrderTable();
                orderTable2.setEmpty(true);
                List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);

                when(orderTableDao.findAllByTableGroupId(1L))
                        .thenReturn(orderTables);
                when(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                        anyList(), eq(Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                ).thenReturn(true);
            }

            @Test
            @DisplayName("예외를 던진다.")
            void it_throws_exception() {
                assertThatThrownBy(() -> tableGroupService.ungroup(givenTableGroupId))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }
    }
}
