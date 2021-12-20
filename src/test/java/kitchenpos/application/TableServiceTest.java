package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 테이블 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @DisplayName("주문 테이블을 생성할 수 있다.")
    @Test
    void create() {
        // given
        final OrderTable request = OrderTableFixture.ofCreateRequest(4);
        final OrderTable expected = OrderTableFixture.of(1L, null, 4, true);
        given(orderTableDao.save(any(OrderTable.class))).willReturn(expected);

        // when
        final OrderTable actual = tableService.create(request);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("주문 테이블 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        final List<OrderTable> expected = Arrays.asList(
            OrderTableFixture.of(1L, null, 4, true),
            OrderTableFixture.of(2L, null, 4, true)
        );
        given(orderTableDao.findAll()).willReturn(expected);

        // when
        final List<OrderTable> actual = tableService.list();

        // then
        assertThat(actual).containsExactlyElementsOf(expected);
    }

    @DisplayName("주문 테이블을 비울 수 있다.")
    @Test
    void changeEmpty() {
        // given
        final OrderTable request = OrderTableFixture.ofChangeEmptyRequest(true);
        final OrderTable before = OrderTableFixture.of(1L, null, 4, false);
        final OrderTable after = OrderTableFixture.of(
            before.getId(),
            before.getTableGroupId(),
            before.getNumberOfGuests(),
            request.isEmpty()
        );
        given(orderTableDao.findById(anyLong()))
            .willReturn(Optional.of(before));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList()))
            .willReturn(false);
        given(orderTableDao.save(any(OrderTable.class)))
            .willReturn(after);

        // when
        final OrderTable actual = tableService.changeEmpty(before.getId(), request);

        // then
        assertThat(actual).isEqualTo(after);
    }

    @DisplayName("주문 테이블이 등록되어 있지 않은 경우, 주문 테이블을 비울 수 없다.")
    @Test
    void changeEmpty_fail_noSuchOrderTable() {
        // given
        final OrderTable request = OrderTableFixture.ofChangeEmptyRequest(true);
        final OrderTable table = OrderTableFixture.of(1L, null, 4, false);

        // when
        ThrowableAssert.ThrowingCallable actual = () ->
            tableService.changeEmpty(table.getId(), request);

        // then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 단체 지정되어 있는 경우, 주문 테이블을 비울 수 없다.")
    @Test
    void changeEmpty_fail_orderTableInGroup() {
        // given
        final OrderTable request = OrderTableFixture.ofChangeEmptyRequest(true);
        final OrderTable table = OrderTableFixture.of(1L, 1L, 4, false);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(table));

        // when
        ThrowableAssert.ThrowingCallable actual = () ->
            tableService.changeEmpty(table.getId(), request);

        // then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블에 준비중인 주문이 있는 경우, 주문 테이블을 비울 수 없다.")
    @Test
    void changeEmpty_fail_hasCookingOrder() {
        // given
        final OrderTable request = OrderTableFixture.ofChangeEmptyRequest(true);
        final OrderTable table = OrderTableFixture.of(1L, null, 4, false);
        given(orderTableDao.findById(anyLong()))
            .willReturn(Optional.of(table));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList()))
            .willReturn(true);

        // when
        ThrowableAssert.ThrowingCallable actual = () ->
            tableService.changeEmpty(table.getId(), request);

        // then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 방문한 손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        // given
        final OrderTable request = OrderTableFixture.ofChangeNumberOfGuestsRequest(2);
        final OrderTable before = OrderTableFixture.of(1L, null, 4, false);
        final OrderTable after = OrderTableFixture.of(
            before.getId(),
            before.getTableGroupId(),
            request.getNumberOfGuests(),
            before.isEmpty()
        );
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(before));
        given(orderTableDao.save(any(OrderTable.class))).willReturn(after);

        // when
        final OrderTable actual = tableService.changeNumberOfGuests(before.getId(), request);

        // then
        assertThat(actual).isEqualTo(after);
    }

    @DisplayName("손님 수가 0보다 작은 경우, 주문 테이블의 방문한 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_fail_invalidNumberOfGuests() {
        // given
        final OrderTable request = OrderTableFixture.ofChangeNumberOfGuestsRequest(-1);
        final OrderTable table = OrderTableFixture.of(1L, null, 4, false);

        // when
        ThrowableAssert.ThrowingCallable actual = () ->
            tableService.changeNumberOfGuests(table.getId(), request);

        // then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 등록되어 있지 않은 경우, 주문 테이블의 방문한 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_fail_noSuchOrderTable() {
        // given
        final OrderTable request = OrderTableFixture.ofChangeNumberOfGuestsRequest(2);
        final OrderTable table = OrderTableFixture.of(1L, null, 4, false);

        // when
        ThrowableAssert.ThrowingCallable actual = () ->
            tableService.changeNumberOfGuests(table.getId(), request);

        // then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어 있는 경우, 주문 테이블의 방문한 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_fail_emptyOrderTable() {
        // given
        final OrderTable request = OrderTableFixture.ofChangeNumberOfGuestsRequest(2);
        final OrderTable table = OrderTableFixture.of(1L, null, 4, true);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(table));

        // when
        ThrowableAssert.ThrowingCallable actual = () ->
            tableService.changeNumberOfGuests(table.getId(), request);

        // then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }
}
