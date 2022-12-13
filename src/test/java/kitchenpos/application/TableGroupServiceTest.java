package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TableGroupService 클래스 테스트")
public class TableGroupServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private TableGroupDao tableGroupDao;
    @InjectMocks
    private TableGroupService tableGroupService;

    @Nested
    @DisplayName("create 메서드 테스트")
    public class CreateMethod {
        @Nested
        @DisplayName("테이블 그룹 생성 성공")
        public class Success {
            @Test
            public void testCase() {
                // given
                final TableGroup tableGroup = setup();

                // when
                final TableGroup createdTableGroup = tableGroupService.create(tableGroup);

                // then
                assertAll(
                        () -> assertThat(createdTableGroup.getId()).isPositive(),
                        () -> assertThat(createdTableGroup.getOrderTables()).isEqualTo(tableGroup.getOrderTables())
                );
            }

            private TableGroup setup() {
                final TableGroup tableGroup = new TableGroup();
                tableGroup.setId(1L);
                final List<Long> orderTableIds = Stream.of(1, 2).map(Long::new).collect(Collectors.toList());
                final List<OrderTable> orderTables = orderTableIds
                        .stream()
                        .map(id -> {
                            final OrderTable orderTable = new OrderTable();
                            orderTable.setId(id);
                            orderTable.setEmpty(true);
                            return orderTable;
                        })
                        .collect(Collectors.toList());
                tableGroup.setOrderTables(orderTables);
                Mockito.when(orderTableDao.findAllByIdIn(Mockito.anyList())).thenReturn(orderTables);
                Mockito.when(tableGroupDao.save(Mockito.any())).thenReturn(tableGroup);
                Mockito.when(orderTableDao.save(Mockito.any())).thenReturn(orderTables.get(0), orderTables.get(1));
                return tableGroup;
            }
        }

        @Nested
        @DisplayName("주문 테이블이 하나도 없으면 테이블 그룹 생성 실패")
        public class ErrorOrderTableIsEmpty {
            @Test
            public void testCase() {
                // given
                final TableGroup tableGroup = setup();

                // when - then
                assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(tableGroup));
            }

            private TableGroup setup() {
                final TableGroup tableGroup = new TableGroup();
                tableGroup.setOrderTables(new ArrayList<>());
                return tableGroup;
            }
        }

        @Nested
        @DisplayName("주문 테이블이 한 개 뿐이면 테이블 그룹 생성 실패")
        public class ErrorOnlyOneInOrderTables {
            @Test
            public void testCase() {
                // given
                final TableGroup tableGroup = setup();

                // when - then
                assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(tableGroup));
            }

            private TableGroup setup() {
                final TableGroup tableGroup = new TableGroup();
                final List<OrderTable> orderTables = Arrays.asList(new OrderTable());
                tableGroup.setOrderTables(orderTables);
                return tableGroup;
            }
        }

        @Nested
        @DisplayName("주문 테이블 id 로 실제 주문 테이블을 하나라도 찾을 수 없으면 테이블 그룹 생성 실패")
        public class ErrorsOrderTablesMissingWhenTryToFindById {
            @Test
            public void testCase() {
                // given
                final TableGroup tableGroup = setup();

                // when - then
                assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(tableGroup));
            }

            private TableGroup setup() {
                final TableGroup tableGroup = new TableGroup();
                tableGroup.setId(1L);
                final List<Long> orderTableIds = Stream.of(1, 2).map(Long::new).collect(Collectors.toList());
                final List<OrderTable> orderTables = orderTableIds
                        .stream()
                        .map(id -> {
                            final OrderTable orderTable = new OrderTable();
                            orderTable.setId(id);
                            orderTable.setEmpty(true);
                            return orderTable;
                        })
                        .collect(Collectors.toList());
                tableGroup.setOrderTables(orderTables);
                Mockito.when(orderTableDao.findAllByIdIn(Mockito.anyList())).thenReturn(orderTables.subList(0, 1));
                return tableGroup;
            }
        }
    }

    @Nested
    @DisplayName("ungroup 메서드 테스트")
    public class UngroupMethod {
        @Nested
        @DisplayName("그룹 해제 성공")
        public class Success {
            @Test
            public void testCase() {
                // given
                final TableGroup tableGroup = setup();
                final TableGroup createdTableGroup = tableGroupService.create(tableGroup);

                // when - then
                assertDoesNotThrow(() -> tableGroupService.ungroup(createdTableGroup.getId()));
            }

            private TableGroup setup() {
                final TableGroup tableGroup = new TableGroup();
                tableGroup.setId(1L);
                final List<Long> orderTableIds = Stream.of(1, 2).map(Long::new).collect(Collectors.toList());
                final List<OrderTable> orderTables = orderTableIds
                        .stream()
                        .map(id -> {
                            final OrderTable orderTable = new OrderTable();
                            orderTable.setId(id);
                            orderTable.setEmpty(true);
                            return orderTable;
                        })
                        .collect(Collectors.toList());
                tableGroup.setOrderTables(orderTables);
                Mockito.when(orderTableDao.findAllByIdIn(Mockito.anyList())).thenReturn(orderTables);
                Mockito.when(tableGroupDao.save(Mockito.any())).thenReturn(tableGroup);
                Mockito.when(orderTableDao.save(Mockito.any())).thenReturn(orderTables.get(0), orderTables.get(1));
                Mockito.when(orderDao.existsByOrderTableIdInAndOrderStatusIn(Mockito.anyList(), Mockito.anyList()))
                        .thenReturn(false);
                return tableGroup;
            }
        }

        @Nested
        @DisplayName("그룹 해제 성공")
        public class ErrorOrderStillCookingOrMeal {
            @Test
            public void testCase() {
                // given
                final TableGroup tableGroup = setup();
                final TableGroup createdTableGroup = tableGroupService.create(tableGroup);

                // when - then
                assertThrows(IllegalArgumentException.class,
                        () -> tableGroupService.ungroup(createdTableGroup.getId()));
            }

            private TableGroup setup() {
                final TableGroup tableGroup = new TableGroup();
                tableGroup.setId(1L);
                final List<Long> orderTableIds = Stream.of(1, 2).map(Long::new).collect(Collectors.toList());
                final List<OrderTable> orderTables = orderTableIds
                        .stream()
                        .map(id -> {
                            final OrderTable orderTable = new OrderTable();
                            orderTable.setId(id);
                            orderTable.setEmpty(true);
                            return orderTable;
                        })
                        .collect(Collectors.toList());
                tableGroup.setOrderTables(orderTables);
                Mockito.when(orderTableDao.findAllByIdIn(Mockito.anyList())).thenReturn(orderTables);
                Mockito.when(tableGroupDao.save(Mockito.any())).thenReturn(tableGroup);
                Mockito.when(orderTableDao.save(Mockito.any())).thenReturn(orderTables.get(0), orderTables.get(1));
                Mockito.when(orderDao.existsByOrderTableIdInAndOrderStatusIn(Mockito.anyList(), Mockito.anyList()))
                        .thenReturn(true);
                return tableGroup;
            }
        }
    }
}
