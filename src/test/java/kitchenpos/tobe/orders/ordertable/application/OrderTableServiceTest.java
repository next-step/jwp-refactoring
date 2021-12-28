package kitchenpos.tobe.orders.ordertable.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.tobe.common.domain.Validator;
import kitchenpos.tobe.fixture.OrderTableFixture;
import kitchenpos.tobe.orders.ordertable.domain.OrderTable;
import kitchenpos.tobe.orders.ordertable.domain.OrderTableRepository;
import kitchenpos.tobe.orders.ordertable.dto.OrderTableChangeEmptyRequest;
import kitchenpos.tobe.orders.ordertable.dto.OrderTableResponse;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 테이블 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderTableServiceTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private Validator<OrderTable> validator;

    @InjectMocks
    private OrderTableService orderTableService;

    @DisplayName("주문 테이블을 생성할 수 있다.")
    @Test
    void create() {
        // given
        final OrderTable expected = OrderTableFixture.of(1L);
        given(orderTableRepository.save(any(OrderTable.class))).willReturn(expected);

        // when
        final OrderTableResponse response = orderTableService.create();

        // then
        assertAll(
            () -> assertThat(response.getId()).isEqualTo(expected.getId()),
            () -> assertThat(response.getTableGroupId()).isEqualTo(expected.getTableGroupId()),
            () -> assertThat(response.getNumberOfGuests()).isEqualTo(expected.getNumberOfGuests()),
            () -> assertThat(response.isEmpty()).isEqualTo(expected.isEmpty())
        );
    }

    @DisplayName("주문 테이블 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        final List<OrderTable> expected = Arrays.asList(
            OrderTableFixture.of(1L),
            OrderTableFixture.of(2L)
        );
        given(orderTableRepository.findAll()).willReturn(expected);

        // when
        final List<OrderTableResponse> response = orderTableService.list();

        // then
        final List<Long> expectedIds = expected.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
        final List<Long> actualIds = response.stream()
            .map(OrderTableResponse::getId)
            .collect(Collectors.toList());
        assertAll(
            () -> assertThat(response.size()).isEqualTo(2),
            () -> assertThat(actualIds).containsExactlyElementsOf(expectedIds)
        );
    }

    @DisplayName("주문 테이블 빈 여부를 변경할 수 있다.")
    @Test
    void changeEmpty() {
        // given
        final OrderTable expected = OrderTableFixture.of(1L);
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(expected));

        final OrderTableChangeEmptyRequest request = OrderTableFixture.ofChangeEmptyRequest(false);

        // when
        final OrderTableResponse response = orderTableService.changeEmpty(1L, request);

        // then
        assertAll(
            () -> assertThat(response.getNumberOfGuests()).isEqualTo(0),
            () -> assertThat(response.isEmpty()).isEqualTo(expected.isEmpty())
        );
    }

    @DisplayName("주문 테이블에 방문한 손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        // given
        final OrderTable expected = OrderTableFixture.of(1L);
        expected.serve();
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(expected));

        // when
        final OrderTableResponse response = orderTableService.changeNumberOfGuests(
            1L,
            OrderTableFixture.ofChangeNumberOfGuestsRequest(4)
        );

        // then
        assertAll(
            () -> assertThat(response.getNumberOfGuests()).isEqualTo(4),
            () -> assertThat(response.isEmpty()).isEqualTo(false)
        );
    }

    @DisplayName("빈 주문 테이블의 방문한 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsFailOrderTableEmpty() {
        // given
        final OrderTable expected = OrderTableFixture.of(1L);
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(expected));

        // when
        final ThrowableAssert.ThrowingCallable request = () -> orderTableService.changeNumberOfGuests(
            1L,
            OrderTableFixture.ofChangeNumberOfGuestsRequest(4)
        );

        // then
        assertThatThrownBy(request).isInstanceOf(IllegalStateException.class);
    }
}
