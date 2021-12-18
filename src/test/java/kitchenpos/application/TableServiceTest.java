package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @InjectMocks
    private TableService tableService;

    @DisplayName("주문 테이블을 생성한다")
    @Test
    void testCreate() {
        // given
        OrderTableRequest requestOrderTable = new OrderTableRequest(4, false);
        OrderTable expectedOrderTable = new OrderTable(1L, null, 4, false);

        given(orderTableDao.save(any(OrderTable.class))).willReturn(expectedOrderTable);

        // when
        OrderTableResponse orderTable = tableService.create(requestOrderTable);

        // then
        assertThat(orderTable).isEqualTo(OrderTableResponse.of(expectedOrderTable));
    }

    @DisplayName("모든 주문 테이블을 조회한다")
    @Test
    void testList() {
        // given
        List<OrderTable> expectedOrderTables = Arrays.asList(new OrderTable(1L, null, 4, false));
        given(orderTableDao.findAll()).willReturn(expectedOrderTables);

        // when
        List<OrderTableResponse> orderTables = tableService.list();

        // then
        assertThat(orderTables).isEqualTo(OrderTableResponse.ofList(expectedOrderTables));
    }

    @DisplayName("주문 테이블 비우기")
    @Nested
    class ChangeEmptyTableTest {
        @DisplayName("주문 테이블을 비운다")
        @Test
        void testEmpty() {
            // given
            OrderTableRequest requestOrderTable = new OrderTableRequest(4, true);
            OrderTable expectedOrderTable = new OrderTable(1L, null, 4, true);

            given(orderTableDao.findById(anyLong())).willReturn(Optional.of(expectedOrderTable));
            given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), any(List.class))).willReturn(false);
            given(orderTableDao.save(any(OrderTable.class))).willReturn(expectedOrderTable);

            // when
            OrderTableResponse orderTable = tableService.changeEmpty(expectedOrderTable.getId(), requestOrderTable);

            // then
            assertThat(orderTable).isEqualTo(OrderTableResponse.of(expectedOrderTable));
        }

        @DisplayName("존재 하는 주문 테이블을 요청해야한다")
        @Test
        void hasSavedOrderTable() {
            // given
            OrderTableRequest requestOrderTable = new OrderTableRequest( 4, true);
            given(orderTableDao.findById(anyLong())).willReturn(Optional.empty());

            // when
            ThrowableAssert.ThrowingCallable callable = () -> tableService.changeEmpty(anyLong(), requestOrderTable);

            // then
            assertThatThrownBy(callable)
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("단체 지정이 되어있지 않아야 한다")
        @Test
        void notGrouping() {
            // given
            OrderTableRequest requestOrderTable = new OrderTableRequest( 4, true);
            OrderTable expectedOrderTable = new OrderTable(1L, 2L, 4, true);

            given(orderTableDao.findById(anyLong())).willReturn(Optional.of(expectedOrderTable));

            // when
            ThrowableAssert.ThrowingCallable callable = () -> tableService.changeEmpty(expectedOrderTable.getId(), requestOrderTable);

            // then
            assertThatThrownBy(callable)
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("테이블에 조리, 식사 중인 주문이 없어야 한다")
        @Test
        void validateOrderState() {
            // given
            OrderTableRequest requestOrderTable = new OrderTableRequest(4, true);
            OrderTable orderTable = new OrderTable(1L, null, 4, true);

            given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
            given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), any(List.class))).willReturn(true);

            // when
            ThrowableAssert.ThrowingCallable callable = () -> tableService.changeEmpty(orderTable.getId(), requestOrderTable);

            // then
            assertThatThrownBy(callable)
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("방문한 손님 수 변경")
    @Nested
    class ChangeCountOfGuestsTest {
        @DisplayName("주문 테이블의 방문한 손님 수를 변경한다")
        @Test
        void testChangeCountOfGuests() {
            // given
            OrderTableRequest requestOrderTable = new OrderTableRequest(4, false);
            OrderTable expectedOrderTable = new OrderTable(1L, null, 4, false);

            given(orderTableDao.findById(anyLong())).willReturn(Optional.of(expectedOrderTable));
            given(orderTableDao.save(any(OrderTable.class))).willReturn(expectedOrderTable);

            // when
            OrderTableResponse orderTable = tableService.changeNumberOfGuests(expectedOrderTable.getId(), requestOrderTable);

            // then
            assertThat(orderTable.getNumberOfGuests()).isEqualTo(expectedOrderTable.getNumberOfGuests());
        }

        @DisplayName("손님의 수는 0명 이상이어야 한다")
        @Test
        void countOverZero() {
            // given
            OrderTableRequest orderTable = new OrderTableRequest(0, false);

            // when
            ThrowableAssert.ThrowingCallable callable = () -> tableService.changeNumberOfGuests(anyLong(), orderTable);

            // then
            assertThatThrownBy(callable)
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재 하는 주문 테이블을 요청해야한다")
        @Test
        void hasSavedOrderTable() {
            // given
            OrderTableRequest orderTable = new OrderTableRequest(4, false);
            given(orderTableDao.findById(anyLong())).willReturn(Optional.empty());

            // when
            ThrowableAssert.ThrowingCallable callable = () -> tableService.changeNumberOfGuests(anyLong(), orderTable);

            // then
            assertThatThrownBy(callable)
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("빈 테이블이 아니어야 한다")
        @Test
        void notEmptyTable() {
            // given
            OrderTableRequest orderTableRequest = new OrderTableRequest(4, false);
            OrderTable orderTable = new OrderTable(1L, null, 4, true);
            given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));

            // when
            ThrowableAssert.ThrowingCallable callable = () -> tableService.changeNumberOfGuests(orderTable.getId(), orderTableRequest);

            // then
            assertThatThrownBy(callable)
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
