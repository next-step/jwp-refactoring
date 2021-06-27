package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("TableService 클래스")
class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    private TableService tableService;

    @BeforeEach
    void beforeEach() {
        tableService = new TableService(orderDao, orderTableDao);
    }

    @Nested
    @DisplayName("create 메서드는")
    class Describe_create {

        @Nested
        @DisplayName("주문 테이블이 주어지면")
        class Context_with_order_table {
            final OrderTable givenOrderTable = new OrderTable();

            @BeforeEach
            void setUp() {
                when(orderTableDao.save(any(OrderTable.class)))
                        .thenReturn(givenOrderTable);
            }

            @Test
            @DisplayName("주어진 주문 테이블을 저장하고, 저장된 객체를 리턴한다.")
            void it_returns_saved_order_table() {
                final OrderTable actual = tableService.create(givenOrderTable);

                assertThat(actual).isEqualTo(givenOrderTable);
            }
        }
    }

    @Nested
    @DisplayName("list 메서드는")
    class Describe_list {

        @Nested
        @DisplayName("저장된 주문 테이블 목록이 주어지면")
        class Context_with_order_tables {
            final OrderTable givenOrderTable1 = new OrderTable();
            final OrderTable givenOrderTable2 = new OrderTable();

            @BeforeEach
            void setUp() {
                when(orderTableDao.findAll())
                        .thenReturn(Arrays.asList(givenOrderTable1, givenOrderTable2));
            }

            @Test
            @DisplayName("주문 테이블 목록을 리턴한다.")
            void it_returns_order_tables() {
                List<OrderTable> actual = tableService.list();
                assertThat(actual).containsExactly(givenOrderTable1, givenOrderTable2);
            }
        }
    }

    @Nested
    @DisplayName("changeEmpty 메서드는")
    class Describe_change_empty {

        @Nested
        @DisplayName("저장된 주문 테이블과 주문 테이블의 비어있음 여부가 주어지면")
        class Context_with_not_empty_order_table {
            final Long givenOrderTableId = 1L;
            final OrderTable givenOrderTable = new OrderTable();

            @BeforeEach
            void setUp() {
                givenOrderTable.setEmpty(true);

                OrderTable savedOrderTable = new OrderTable();
                savedOrderTable.setEmpty(false);
                when(orderTableDao.findById(anyLong()))
                        .thenReturn(Optional.of(savedOrderTable));

                OrderTable updatedOrderTable = new OrderTable();
                updatedOrderTable.setEmpty(givenOrderTable.isEmpty());

                when(orderTableDao.save(any(OrderTable.class)))
                        .thenReturn(updatedOrderTable);
            }

            @Test
            @DisplayName("비어있는 상태의 주문 테이블을 리턴한다.")
            void it_returns_empty_order_tables() {
                final OrderTable actual = tableService.changeEmpty(givenOrderTableId, givenOrderTable);
                assertThat(actual.isEmpty()).isTrue();
            }
        }

        @Nested
        @DisplayName("저장된 주문테이블이 테이블 그룹에 포함된 상태로 주어지면")
        class Context_with_table_group_order_table {
            final Long givenOrderTableId = 1L;
            final OrderTable givenOrderTable = new OrderTable();

            @BeforeEach
            void setUp() {
                Long savedTableGroupId = 1L;
                OrderTable savedOrderTable = new OrderTable();
                savedOrderTable.setTableGroupId(savedTableGroupId);
                when(orderTableDao.findById(anyLong()))
                        .thenReturn(Optional.of(savedOrderTable));
            }

            @Test
            @DisplayName("예외를 던진다.")
            void it_throws_exception() {
                assertThatThrownBy(() -> tableService.changeEmpty(givenOrderTableId, givenOrderTable))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    @Nested
    @DisplayName("changeNumberOfGuests 메서드는")
    class Describe_changeNumberOfGuests {

        @Nested
        @DisplayName("변경할 방문 손님 수가 담긴 주문 테이블과 식별자가 주어지면")
        class Context_with_number_of_guest_and_order_table {
            Long givenOrderTableId = 1L;
            OrderTable givenOrderTable = new OrderTable();
            int updateNumberOfGuest = 5;

            @BeforeEach
            void setUp() {
                givenOrderTable.setNumberOfGuests(updateNumberOfGuest);
                OrderTable savedOrderTable = new OrderTable();
                when(orderTableDao.findById(anyLong()))
                        .thenReturn(Optional.of(savedOrderTable));

                OrderTable updatedTable = new OrderTable();
                updatedTable.setNumberOfGuests(updateNumberOfGuest);
                when(orderTableDao.save(savedOrderTable))
                        .thenReturn(updatedTable);
            }

            @Test
            @DisplayName("손님 수가 갱신된 테이블 목록을 리턴한다.")
            void it_returns_updated_number_of_guests_order_table() {
                OrderTable actual = tableService.changeNumberOfGuests(givenOrderTableId, givenOrderTable);
                assertThat(actual).extracting("numberOfGuests").isEqualTo(updateNumberOfGuest);
            }
        }

        @Nested
        @DisplayName("방문 손님 수가 없는 주문 테이블과 식별자가 주어지면")
        class Context_with_empty_guest_order_table {
            Long givenOrderTableId = 1L;
            OrderTable givenOrderTable = new OrderTable();
            int updateNumberOfGuest = 0;

            @BeforeEach
            void setUp() {
                givenOrderTable.setNumberOfGuests(updateNumberOfGuest);
            }

            @Test
            @DisplayName("예외를 던진다")
            void it_throws_exception() {
                assertThatThrownBy(() -> tableService.changeNumberOfGuests(givenOrderTableId, givenOrderTable))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("비어있는 저장된 테이블이 주어지면")
        class Context_with_empty_saved_order_table {
            Long givenOrderTableId = 1L;
            OrderTable givenOrderTable = new OrderTable();
            int updateNumberOfGuest = 5;

            @BeforeEach
            void setUp() {
                givenOrderTable.setNumberOfGuests(updateNumberOfGuest);
                OrderTable savedOrderTable = new OrderTable();
                savedOrderTable.setEmpty(true);
                when(orderTableDao.findById(anyLong()))
                        .thenReturn(Optional.of(savedOrderTable));
            }

            @Test
            @DisplayName("예외를 던진다")
            void it_throws_exception() {
                assertThatThrownBy(() -> tableService.changeNumberOfGuests(givenOrderTableId, givenOrderTable))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }
    }
}
