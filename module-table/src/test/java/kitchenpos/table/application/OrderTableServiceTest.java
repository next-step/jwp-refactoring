package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.CannotUpdateException;
import kitchenpos.common.exception.ExceptionType;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.request.OrderTableRequest;
import kitchenpos.table.dto.response.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("테이블 서비스에 대한 테스트")
@ExtendWith(MockitoExtension.class)
class OrderTableServiceTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderTableService orderTableService;

    private OrderTableRequest 주문_테이블_request;
    private OrderTableRequest 주문_테이블_2_request;

    private OrderTable 주문_테이블;

    @BeforeEach
    void setUp() {
        주문_테이블_request = new OrderTableRequest(null, null, 3, true);
        주문_테이블_2_request = new OrderTableRequest(null, null, 5, true);

        주문_테이블 = OrderTable.of(1L, null, 3, true);
    }

    @DisplayName("주문 테이블을 등록하면 정상적으로 등록된다")
    @Test
    void create_test() {
        // given
        when(orderTableRepository.save(any()))
            .thenReturn(주문_테이블);

        // when
        OrderTableResponse result = orderTableService.create(주문_테이블_request);

        // then
        assertThat(result).isNotNull();
    }

    @DisplayName("주문 테이블 목록을 조회한다")
    @Test
    void findAll_test() {
        // given
        when(orderTableRepository.findAll())
            .thenReturn(Arrays.asList(주문_테이블, 주문_테이블));

        // when
        List<OrderTableResponse> result = orderTableService.list();

        // then
        assertThat(result).hasSize(2);
    }

    @DisplayName("주문 테이블을 빈 테이블로 변경한다")
    @Test
    void change_empty_test() {
        // given
        주문_테이블.fillTheTable();
        when(orderTableRepository.findById(주문_테이블.getId()))
            .thenReturn(Optional.of(주문_테이블));
        when(orderTableRepository.save(주문_테이블))
            .thenReturn(주문_테이블);

        // when
        OrderTableResponse result = orderTableService.changeEmpty(주문_테이블.getId());

        // then
        assertTrue(result.isEmpty());
    }

    @DisplayName("주문 테이블을 빈 테이블로 변경시 변경할 테이블이 존재하지 않으면 예외가 발생한다")
    @Test
    void change_empty_exception_test() {
        // given
        when(orderTableRepository.findById(주문_테이블.getId()))
            .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> {
            orderTableService.changeEmpty(주문_테이블.getId());
        }).isInstanceOf(NotFoundException.class)
            .hasMessageContaining(ExceptionType.NOT_EXIST_ORDER_TABLE.getMessage());
    }

    @DisplayName("주문 테이블을 빈 테이블로 변경시 요리중, 식사중인 테이블이 있다면 예외가 발생한다")
    @Test
    void change_empty_exception_test2() {
        // given
        OrderTable 주문_테이블 = mock(OrderTable.class);
        when(orderTableRepository.findById(주문_테이블.getId()))
            .thenReturn(Optional.of(주문_테이블));
        doThrow(new CannotUpdateException(ExceptionType.CAN_NOT_UPDATE_TABLE_IN_COOKING_AND_MEAL_STATUS))
            .when(주문_테이블).emptyTheTable();

        // then
        assertThatThrownBy(() -> {
            orderTableService.changeEmpty(주문_테이블.getId());
        }).isInstanceOf(CannotUpdateException.class)
            .hasMessageContaining(ExceptionType.CAN_NOT_UPDATE_TABLE_IN_COOKING_AND_MEAL_STATUS.getMessage());
    }

    @DisplayName("주문 테이블의 손님 수를 변경하면 정상적으로 변경된다")
    @Test
    void change_number_of_guests_test() {
        // given
        주문_테이블.fillTheTable();
        when(orderTableRepository.findById(주문_테이블.getId()))
            .thenReturn(Optional.of(주문_테이블));
        when(orderTableRepository.save(주문_테이블))
            .thenReturn(주문_테이블);

        // when
        OrderTableResponse result = orderTableService.changeNumberOfGuests(주문_테이블.getId(), 주문_테이블_2_request);

        // then
        assertThat(result.getNumberOfGuests()).isEqualTo(주문_테이블_2_request.getNumberOfGuests());
    }

    @DisplayName("주문 테이블의 손님 수 변경시 변경할 손님 수가 0미만이면 예외가 발생한다")
    @Test
    void change_number_of_guests_exception_test() {
        // given
        주문_테이블_request = new OrderTableRequest(null, null, -3, true);

        // then
        assertThatThrownBy(() -> {
            orderTableService.changeNumberOfGuests(주문_테이블.getId(), 주문_테이블_request);
        }).isInstanceOf(BadRequestException.class)
            .hasMessageContaining(ExceptionType.CAN_NOT_LESS_THAN_ZERO_GUESTS.getMessage());
    }

    @DisplayName("주문 테이블의 손님 수 변경시 변경할 주문 테이블이 존재하지 않으면 예외가 발생한다")
    @Test
    void change_number_of_guests_exception_test2() {
        // given
        when(orderTableRepository.findById(주문_테이블.getId()))
            .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> {
            orderTableService.changeNumberOfGuests(주문_테이블.getId(), 주문_테이블_request);
        }).isInstanceOf(NotFoundException.class)
            .hasMessageContaining(ExceptionType.NOT_EXIST_ORDER_TABLE.getMessage());
    }

    @DisplayName("주문 테이블의 손님 수 변경시 변경할 주문 테이블이 비어있다면 예외가 발생한다")
    @Test
    void change_number_of_guests_exception_test3() {
        // given
        when(orderTableRepository.findById(주문_테이블.getId()))
            .thenReturn(Optional.of(주문_테이블));

        // then
        assertThatThrownBy(() -> {
            orderTableService.changeNumberOfGuests(주문_테이블.getId(), 주문_테이블_request);
        }).isInstanceOf(BadRequestException.class)
            .hasMessageContaining(ExceptionType.EMPTY_TABLE.getMessage());
    }
}
