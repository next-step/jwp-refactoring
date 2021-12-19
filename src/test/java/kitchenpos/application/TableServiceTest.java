package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@DisplayName("주문 테이블 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private OrderDao orderDao;
    @InjectMocks
    private TableService tableService;


    @DisplayName("방문한 손님 수 와 빈 테이블 유무 주문 테이블을 생성 할 수 있다.")
    @Test
    void create() {
        // given
        final OrderTable orderTable = getOrderTable(1L, false, 13);
        given(orderTableDao.save(any())).willReturn(orderTable);
        // when
        final OrderTable actual = tableService.create(orderTable);
        // then
        assertAll(
                () -> assertThat(actual).isEqualTo(orderTable),
                () -> assertThat(actual.getTableGroupId()).isNull()
        );
    }

    @DisplayName("주문 테이블 목록을 조회 할 수 있다.")
    @Test
    void list() {
        // given
        final List<OrderTable> expected = Arrays.asList(
                getOrderTable(1L, false, 3),
                getOrderTable(2L, false, 4)
        );

        given(orderTableDao.findAll()).willReturn(expected);
        // when
        final List<OrderTable> actual = tableService.list();
        // then
        assertThat(actual).containsExactlyElementsOf(expected);

    }


    @DisplayName("주문 테이블 아이디와 주문 테이블을 통해 해당 주문 테이블을 빈 테이블로 변경할 수 있다.")
    @Test
    void changeEmpty() {
        // given
        final OrderTable request = getChangeEmptyRequest(false, 13);
        final OrderTable expected = getOrderTable(1L, true, 13);

        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(expected));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), any())).willReturn(false);
        given(orderTableDao.save(any(OrderTable.class))).willReturn(expected);
        // when
        OrderTable orderTable = tableService.changeEmpty(expected.getId(), request);
        // then
        assertThat(orderTable).isEqualTo(expected);
    }


    @DisplayName("빈 테이블 유무를 변경하지 못하는 경우")
    @Nested
    class changeEmptyFail {
        @DisplayName("주문 테이블 아이디를 따른 주문 테이블이 존재하지 않는 경우")
        @Test
        void changeEmptyByEmptyByNotExistOrderTable() {
            // given
            final OrderTable changeEmptyRequest = getChangeEmptyRequest(false, 13);
            given(orderTableDao.findById(anyLong())).willReturn(Optional.empty());
            // when
            ThrowableAssert.ThrowingCallable callable = () -> tableService.changeEmpty(1L, changeEmptyRequest);
            // then
            assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("단체 지정의 아이디가 빈값이 아닌 경우")
        @Test
        void changeEmptyByEmptyByNotNullTargetGroupId() {
            // given
            final OrderTable changeEmptyRequest = getChangeEmptyRequest(false, 13);
            final OrderTable expected = getOrderTable(1L, true, 13, 1L);

            given(orderTableDao.findById(anyLong())).willReturn(Optional.of(expected));
            // when
            ThrowableAssert.ThrowingCallable callable = () -> tableService.changeEmpty(expected.getId(), changeEmptyRequest);
            // then
            assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블의 상태가 조리나 식사 상태인 경우")
        @Test
        void changeEmptyByEmptyOrderLines() {
            // given
            final OrderTable changeEmptyRequest = getChangeEmptyRequest(false, 13);
            final OrderTable expected = getOrderTable(1L, true, 13);

            given(orderTableDao.findById(anyLong())).willReturn(Optional.of(expected));
            given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), any())).willReturn(true);

            // when
            ThrowableAssert.ThrowingCallable callable = () -> tableService.changeEmpty(expected.getId(), changeEmptyRequest);
            // then
            assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
        }
    }


    @DisplayName("주문 테이블 아이디와 주문 테이블을 통해 해당 주문 테이블을 방문한 손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        // given
        final OrderTable changeNumberOfGuestsRequest = getChangeNumberOfGuestsRequest(23);
        final OrderTable expected = getOrderTable(1L, false, 23);
        given(orderTableDao.findById(any())).willReturn(Optional.of(expected));
        given(orderTableDao.save(any())).willReturn(expected);
        // when
        final OrderTable actual = tableService.changeNumberOfGuests(expected.getId(), changeNumberOfGuestsRequest);
        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(23);
    }


    @DisplayName("손님의 수를 변경하지 못하는 경우")
    @Nested
    class changeNumberOfGuestsFail {

        @DisplayName("주문 테이블 아이디를 따른 주문 테이블이 존재하지 않는 경우")
        @Test
        void changeNumberOfGuestsByNotExistOrder() {
            // given
            final OrderTable changeEmptyRequest = getChangeNumberOfGuestsRequest(4);
            given(orderTableDao.findById(any())).willReturn(Optional.empty());
            // when
            ThrowableAssert.ThrowingCallable callable = () -> tableService.changeNumberOfGuests(1L, changeEmptyRequest);
            // then
            assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("방문한 손님 수가 1 미만 인 경우")
        @Test
        void changeNumberOfIllegalNumberOfGuests() {
            // given
            final OrderTable changeEmptyRequest = getChangeNumberOfGuestsRequest(-1);
            // when
            ThrowableAssert.ThrowingCallable callable = () -> tableService.changeNumberOfGuests(1L, changeEmptyRequest);
            // then
            assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("빈 테이블인 경우")
        @Test
        void changeNumberOfEmptyTable() {
            // given
            final OrderTable changeEmptyRequest = getChangeNumberOfGuestsRequest(4);
            final OrderTable expected = getOrderTable(1L, true, 4);
            given(orderTableDao.findById(any())).willReturn(Optional.of(expected));
            // when
            ThrowableAssert.ThrowingCallable callable = () -> tableService.changeNumberOfGuests(1L, changeEmptyRequest);
            // then
            assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
        }
    }


    private OrderTable getChangeNumberOfGuestsRequest(int numberOfGuests) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        return orderTable;
    }

    private OrderTable getChangeEmptyRequest(boolean empty, int numberOfGuests) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);
        orderTable.setNumberOfGuests(numberOfGuests);
        return orderTable;
    }

    public static OrderTable getOrderTable(Long id, boolean empty, int numberOfGuests) {
        return getOrderTable(id, empty, numberOfGuests, null);
    }

    public static OrderTable getOrderTable(Long id, boolean empty, int numberOfGuests, Long tableGroupId) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setEmpty(empty);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setTableGroupId(tableGroupId);
        return orderTable;
    }
}
