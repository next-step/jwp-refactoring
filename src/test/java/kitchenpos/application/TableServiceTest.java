package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@DisplayName("TableService 클래스 테스트")
public class TableServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @InjectMocks
    private TableService tableService;

    @Nested
    @DisplayName("create 메서드 테스트")
    public class CreateMethod {
        @Nested
        @DisplayName("주문 테이블 등록 성공")
        public class Success {
            @Test
            public void testCase() {
                // given
                final OrderTable orderTable = setup();

                // when
                final OrderTable createdTable = tableService.create(orderTable);

                // then
                assertAll(
                        () -> assertThat(createdTable.getId()).isPositive(),
                        () -> assertThat(createdTable.getTableGroupId()).isNull()
                );
            }

            private OrderTable setup() {
                final OrderTable orderTable = new OrderTable();
                orderTable.setId(1L);
                orderTable.setTableGroupId(null);
                Mockito.when(orderTableDao.save(Mockito.any())).thenReturn(orderTable);
                return orderTable;
            }
        }
    }

    @Nested
    @DisplayName("list 메서드 테스트")
    public class ListMethod {
        @Nested
        @DisplayName("주문 테이블 빈 목록 조회 성공")
        public class Success {
            @Test
            public void testCase() {
                // given
                final List<OrderTable> orderTables = setup();

                // when
                final List<OrderTable> foundOrderTables = tableService.list();

                // then
                assertThat(foundOrderTables).hasSize(orderTables.size());
            }

            private List<OrderTable> setup() {
                final List<OrderTable> orderTables = Arrays.asList(new OrderTable(), new OrderTable(), new OrderTable());
                Mockito.when(orderTableDao.findAll()).thenReturn(orderTables);
                return orderTables;
            }
        }

        @Nested
        @DisplayName("주문 테이블 빈 목록 조회 성공")
        public class SuccessEmpty {
            @Test
            public void testCase() {
                // given
                setup();

                // when
                final List<OrderTable> foundOrderTables = tableService.list();

                // then
                assertThat(foundOrderTables).hasSize(0);
            }

            private void setup() {
                final List<OrderTable> orderTables = new ArrayList<>();
                Mockito.when(orderTableDao.findAll()).thenReturn(orderTables);
            }
        }
    }

    @Nested
    @DisplayName("changeEmpty 메서드 테스트")
    public class ChangeEmptyMethod {
        @Nested
        @DisplayName("주문 테이블 빈 테이블로 지정 성공")
        public class Success {
            @Test
            public void testCase() {
                // given
                final OrderTable orderTable = setup();
                final OrderTable newOrderTable = new OrderTable();
                newOrderTable.setEmpty(false);

                // when
                final OrderTable changedOrderTable = tableService.changeEmpty(orderTable.getId(), newOrderTable);

                // then
                assertThat(changedOrderTable.isEmpty()).isEqualTo(newOrderTable.isEmpty());
            }

            private OrderTable setup() {
                final OrderTable orderTable = new OrderTable();
                orderTable.setId(1L);
                Mockito.when(orderTableDao.findById(Mockito.anyLong())).thenReturn(Optional.of(orderTable));
                orderTable.setTableGroupId(null);
                Mockito.when(orderDao.existsByOrderTableIdAndOrderStatusIn(Mockito.anyLong(), Mockito.anyList()))
                        .thenReturn(false);
                orderTable.setEmpty(false);
                Mockito.when(orderTableDao.save(Mockito.any())).thenReturn(orderTable);
                return orderTable;
            }
        }

        @Nested
        @DisplayName("주문 테이블 id 로 실제 주문 테이블을 찾을 수 없으면 주문 테이블 빈 테이블로 지정 실패")
        public class ErrorOrderTableNotFoundById {
            @Test
            public void testCase() {
                // given
                final OrderTable orderTable = setup();
                final OrderTable newOrderTable = new OrderTable();
                newOrderTable.setEmpty(false);

                // when - then
                assertThrows(IllegalArgumentException.class,
                        () -> tableService.changeEmpty(orderTable.getId(), newOrderTable));
            }

            private OrderTable setup() {
                final OrderTable orderTable = new OrderTable();
                orderTable.setId(1L);
                Mockito.when(orderTableDao.findById(Mockito.anyLong())).thenReturn(Optional.empty());
                return orderTable;
            }
        }

        @Nested
        @DisplayName("주문 테이블 id 로 검색해서 조리중이거나 식사중인 주문이 있을 경우 주문 테이블 빈 테이블로 지정 실패")
        public class ErrorOrderCookingOrEating {
            @Test
            public void testCase() {
                // given
                final OrderTable orderTable = setup();
                final OrderTable newOrderTable = new OrderTable();
                newOrderTable.setEmpty(false);

                // when - then
                assertThrows(IllegalArgumentException.class,
                        () -> tableService.changeEmpty(orderTable.getId(), newOrderTable));
            }

            private OrderTable setup() {
                final OrderTable orderTable = new OrderTable();
                orderTable.setId(1L);
                Mockito.when(orderTableDao.findById(Mockito.anyLong())).thenReturn(Optional.of(orderTable));
                orderTable.setTableGroupId(null);
                Mockito.when(orderDao.existsByOrderTableIdAndOrderStatusIn(Mockito.anyLong(), Mockito.anyList()))
                        .thenReturn(true);
                return orderTable;
            }
        }
    }

    @Nested
    @DisplayName("changeNumberOfGuests 메서드 테스트")
    public class ChangeNumberOfGuestsMethod {
        @Nested
        @DisplayName("주문 테이블에 앉아 있는 손님의 숫자 변경 성공")
        public class Success {
            private final int NUMBER_OF_GUESTS_AFTER = 5;

            @Test
            public void testCase() {
                // given
                final OrderTable orderTable = setup();
                final OrderTable newOrderTable = new OrderTable();
                newOrderTable.setNumberOfGuests(NUMBER_OF_GUESTS_AFTER);

                // when
                final OrderTable changedOrderTable = tableService.changeNumberOfGuests(orderTable.getId(),
                        newOrderTable);

                // then
                assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(newOrderTable.getNumberOfGuests());
            }

            private OrderTable setup() {
                final OrderTable orderTable = new OrderTable();
                orderTable.setId(1L);
                final int NUMBER_OF_GUESTS_BEFORE = 3;
                orderTable.setNumberOfGuests(NUMBER_OF_GUESTS_BEFORE);
                orderTable.setEmpty(false);
                Mockito.when(orderTableDao.findById(Mockito.anyLong())).thenReturn(Optional.of(orderTable));
                orderTable.setNumberOfGuests(NUMBER_OF_GUESTS_AFTER);
                Mockito.when(orderTableDao.save(Mockito.any())).thenReturn(orderTable);
                return orderTable;
            }
        }

        @Nested
        @DisplayName("새롭게 변경하려는 손님의 숫자가 음수이면 변경 실패")
        public class ErrorNumberOfGuestsMinus {
            @Test
            public void testCase() {
                // given
                final OrderTable orderTable = setup();
                final OrderTable newOrderTable = new OrderTable();
                newOrderTable.setNumberOfGuests(-1);

                // when - then
                assertThrows(IllegalArgumentException.class,
                        () -> tableService.changeNumberOfGuests(orderTable.getId(), newOrderTable));
            }

            private OrderTable setup() {
                final OrderTable orderTable = new OrderTable();
                orderTable.setId(1L);
                return orderTable;
            }
        }

        @Nested
        @DisplayName("주문 테이블 id 로 실제 주문 테이블을 찾을 수 없으면 주문 테이블에 앉아 있는 손님의 숫자 변경 성공")
        public class ErrorOrderTableNotFound {
            @Test
            public void testCase() {
                // given
                final OrderTable orderTable = setup();
                final OrderTable newOrderTable = new OrderTable();
                final int NUMBER_OF_GUESTS_AFTER = 5;
                newOrderTable.setNumberOfGuests(NUMBER_OF_GUESTS_AFTER);

                // when - then
                assertThrows(IllegalArgumentException.class,
                        () -> tableService.changeNumberOfGuests(orderTable.getId(), newOrderTable));
            }

            private OrderTable setup() {
                final OrderTable orderTable = new OrderTable();
                orderTable.setId(1L);
                final int NUMBER_OF_GUESTS_BEFORE = 3;
                orderTable.setNumberOfGuests(NUMBER_OF_GUESTS_BEFORE);
                Mockito.when(orderTableDao.findById(Mockito.anyLong())).thenReturn(Optional.empty());
                return orderTable;
            }
        }

        @Nested
        @DisplayName("주문 테이블이 빈 테이블이면 주문 테이블에 앉아 있는 손님의 숫자 변경 성공")
        public class ErrorOrderTableEmpty {
            @Test
            public void testCase() {
                // given
                final OrderTable orderTable = setup();
                final OrderTable newOrderTable = new OrderTable();
                final int NUMBER_OF_GUESTS_AFTER = 5;
                newOrderTable.setNumberOfGuests(NUMBER_OF_GUESTS_AFTER);

                // when - then
                assertThrows(IllegalArgumentException.class,
                        () -> tableService.changeNumberOfGuests(orderTable.getId(), newOrderTable));
            }

            private OrderTable setup() {
                final OrderTable orderTable = new OrderTable();
                orderTable.setId(1L);
                final int NUMBER_OF_GUESTS_BEFORE = 3;
                orderTable.setNumberOfGuests(NUMBER_OF_GUESTS_BEFORE);
                orderTable.setEmpty(true);
                Mockito.when(orderTableDao.findById(Mockito.anyLong())).thenReturn(Optional.empty());
                return orderTable;
            }
        }
    }
}
