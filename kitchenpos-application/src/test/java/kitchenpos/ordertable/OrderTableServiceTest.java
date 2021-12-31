package kitchenpos.ordertable;

import kitchenpos.order.validator.OrderOrderTableChangeEmptyValidator;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
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

import static kitchenpos.OrderTableFixture.getOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@DisplayName("주문 테이블 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public
class OrderTableServiceTest {
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private OrderOrderTableChangeEmptyValidator changeEmptyOrderTableValidator;
    @InjectMocks
    private OrderTableService tableService;


    @DisplayName("방문한 손님 수 와 빈 테이블 유무 주문 테이블을 생성 할 수 있다.")
    @Test
    void create() {
        // given
        final OrderTableRequest createRequest = getCreateRequest(false, 13);
        given(orderTableRepository.save(any())).willReturn(createRequest.toEntity());
        // when
        final OrderTableResponse actual = tableService.create(createRequest);
        // then
        assertThat(actual).isEqualTo(OrderTableResponse.of(createRequest.toEntity()));
    }

    @DisplayName("주문 테이블 목록을 조회 할 수 있다.")
    @Test
    void list() {
        // given
        final OrderTable orderTable1 = getOrderTable(1L, false, 3);
        final OrderTable orderTable2 = getOrderTable(2L, false, 4);
        final List<OrderTable> expected = Arrays.asList(
                orderTable1,
                orderTable2
        );

        given(orderTableRepository.findAll()).willReturn(expected);
        // when
        final List<OrderTableResponse> actual = tableService.list();
        // then
        assertThat(actual).containsExactlyElementsOf(
                Arrays.asList(
                        OrderTableResponse.of(orderTable1),
                        OrderTableResponse.of(orderTable2))
        );

    }


    @DisplayName("주문 테이블 아이디와 주문 테이블을 통해 해당 주문 테이블을 빈 테이블로 변경할 수 있다.")
    @Test
    void changeEmpty() {
        // given
        final OrderTableRequest request = getChangeEmptyRequest(false);
        final OrderTable expected = getOrderTable(1L, true, 13);

        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(expected));
        doNothing().when(changeEmptyOrderTableValidator).validate(anyLong());
        given(orderTableRepository.save(any(OrderTable.class))).willReturn(expected);
        // when
        OrderTableResponse orderTable = tableService.changeEmpty(expected.getId(), request);
        // then
        assertThat(orderTable).isEqualTo(OrderTableResponse.of(expected));
    }


    @DisplayName("빈 테이블 유무를 변경하지 못하는 경우")
    @Nested
    class ChangeEmptyFail {
        @DisplayName("주문 테이블 아이디를 따른 주문 테이블이 존재하지 않는 경우")
        @Test
        void changeEmptyByEmptyByNotExistOrderTable() {
            // given
            final OrderTableRequest changeEmptyRequest = getChangeEmptyRequest(false);
            given(orderTableRepository.findById(anyLong())).willReturn(Optional.empty());
            // when
            ThrowableAssert.ThrowingCallable callable = () -> tableService.changeEmpty(1L, changeEmptyRequest);
            // then
            assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블의 상태가 조리나 식사 상태인 경우")
        @Test
        void changeEmptyByEmptyOrderLines() {
            // given
            final OrderTableRequest changeEmptyRequest = getChangeEmptyRequest(false);
            final OrderTable expected = getOrderTable(1L, true, 13);

            given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(expected));
            doThrow(new IllegalArgumentException()).when(changeEmptyOrderTableValidator).validate(anyLong());

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
        final OrderTableRequest changeNumberOfGuestsRequest = getChangeNumberOfGuestsRequest(23);
        final OrderTable expected = getOrderTable(1L, false, 23);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(expected));
        // when
        final OrderTableResponse actual = tableService.changeNumberOfGuests(expected.getId(), changeNumberOfGuestsRequest);
        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(23);
    }


    @DisplayName("손님의 수를 변경하지 못하는 경우")
    @Nested
    class ChangeNumberOfGuestsFail {

        @DisplayName("주문 테이블 아이디를 따른 주문 테이블이 존재하지 않는 경우")
        @Test
        void changeNumberOfGuestsByNotExistOrder() {
            // given
            final OrderTableRequest changeEmptyRequest = getChangeNumberOfGuestsRequest(4);
            given(orderTableRepository.findById(any())).willReturn(Optional.empty());
            // when
            ThrowableAssert.ThrowingCallable callable = () -> tableService.changeNumberOfGuests(1L, changeEmptyRequest);
            // then
            assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("방문한 손님 수가 1 미만 인 경우")
        @Test
        void changeNumberOfIllegalNumberOfGuests() {
            // given
            final OrderTableRequest changeEmptyRequest = getChangeNumberOfGuestsRequest(-1);
            // when
            ThrowableAssert.ThrowingCallable callable = () -> tableService.changeNumberOfGuests(1L, changeEmptyRequest);
            // then
            assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("빈 테이블인 경우")
        @Test
        void changeNumberOfEmptyTable() {
            // given
            final OrderTableRequest changeEmptyRequest = getChangeNumberOfGuestsRequest(4);
            final OrderTable expected = getOrderTable(1L, true, 4);
            given(orderTableRepository.findById(any())).willReturn(Optional.of(expected));
            // when
            ThrowableAssert.ThrowingCallable callable = () -> tableService.changeNumberOfGuests(1L, changeEmptyRequest);
            // then
            assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
        }
    }

    private OrderTableRequest getChangeNumberOfGuestsRequest(int numberOfGuests) {
        return OrderTableRequest.makeChangeNumberOfGuestsRequest(numberOfGuests);
    }

    private OrderTableRequest getChangeEmptyRequest(boolean empty) {
        return OrderTableRequest.makeChangeEmptyRequest(empty);
    }

    private OrderTableRequest getCreateRequest(boolean empty, int numberOfGuests) {
        return OrderTableRequest.of(numberOfGuests, empty);
    }
}
