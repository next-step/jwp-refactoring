package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.exception.OrderTableException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    private TableService tableService;

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;

    private OrderTable orderTable;

    @BeforeEach
    public void init() {
        tableService = new TableService(orderRepository, orderTableRepository);
        orderTable = new OrderTable(0, true);
    }


    @Test
    @DisplayName("TableGroup에 속해있지않고, 주문중에 요리중이거나, 먹고있는중이 아닌 테이블은 Empty설정이 가능하다")
    void changeEmpty() {
        //given
        OrderTableRequest orderTableRequest = new OrderTableRequest();
        orderTableRequest.setEmpty(true);
        orderTableRequest.setNumberOfGuests(0);

        when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable));
        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(
            1L, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))).thenReturn(
            false);
        when(orderTableRepository.save(any())).thenReturn(orderTable);
        //when & then
        assertDoesNotThrow(() -> tableService.changeEmpty(1L, orderTableRequest));
    }

    @Test
    @DisplayName("TableGroup에 속해있는 테이블은 empty설정이 불가능하다.")
    void changeEmptyAssignedWithTableGroupThrowError() {
        //given
        OrderTableRequest orderTableRequest = new OrderTableRequest();
        orderTableRequest.setEmpty(true);
        orderTableRequest.setNumberOfGuests(0);

        orderTable.mapToTableGroup(1L);
        when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable));

        //when & then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTableRequest))
            .isInstanceOf(OrderTableException.class);
    }

    @Test
    @DisplayName("저장되지않은 테이블은 empty설정이불가능하다.")
    void changeEmptyWithNotSavedTableThrowError() {
        //given
        OrderTableRequest orderTableRequest = new OrderTableRequest();
        orderTableRequest.setEmpty(true);
        orderTableRequest.setNumberOfGuests(0);

        //when & then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTableRequest))
            .isInstanceOf(OrderTableException.class)
            .hasMessage("상태 변경할 테이블은 저장되어있어야 합니다");
    }

    @Test
    @DisplayName("테이블이 요청한 주문중에 먹고있거나 요리중이면 empty설정 불가")
    void changeEmptyDuringCookingOrMealThrowError() {
        //given
        OrderTableRequest orderTableRequest = new OrderTableRequest();
        orderTableRequest.setEmpty(true);
        orderTableRequest.setNumberOfGuests(0);

        when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable));
        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(
            1L, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))).thenReturn(
            true);

        //when & then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTableRequest))
            .isInstanceOf(OrderTableException.class);
    }

    @Test
    @DisplayName("손님 수 변경 정상로직")
    void changeNumberHappyCase() {
        //given
        when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable));
        orderTable.useTable();
        when(orderTableRepository.save(any())).thenReturn(orderTable);
        OrderTableRequest ordertableRequest = new OrderTableRequest();
        ordertableRequest.setNumberOfGuests(4);

        //when
        assertDoesNotThrow(() -> tableService.changeNumberOfGuests(1L, ordertableRequest));
    }

    @Test
    @DisplayName("음수의 손님 수 등록시 에러발생")
    void changeNumberOfGuestsWithMinusNumberThrowError() {
        //given
        OrderTableRequest orderTableRequest = new OrderTableRequest();
        orderTableRequest.setNumberOfGuests(-1);

        //when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTableRequest))
            .isInstanceOf(OrderTableException.class).
            hasMessage("인원수 설정할 테이블은 저장되어있어야 합니다");
    }

    @Test
    @DisplayName("저장되지 않은 테이블 수정시 에러발생")
    void changeNumberOfGuestsWithNotSavedTableThrowError() {
        //given
        OrderTableRequest orderTableRequest = new OrderTableRequest();
        orderTableRequest.setNumberOfGuests(3);

        //when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTableRequest))
            .isInstanceOf(OrderTableException.class)
            .hasMessage("인원수 설정할 테이블은 저장되어있어야 합니다");
    }

    @Test
    @DisplayName("비어있는 테이블에 손님 수 설정시 에러발생")
    void changeNumberOfGuestsToEmptyTableThrowError() {
        //given
        when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable));
        OrderTableRequest orderTableRequest = new OrderTableRequest();
        orderTableRequest.setNumberOfGuests(3);

        //when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTableRequest))
            .isInstanceOf(OrderTableException.class)
            .hasMessage("비어있는 테이블은 인원수 설정을 할수 없습니다");
    }
}