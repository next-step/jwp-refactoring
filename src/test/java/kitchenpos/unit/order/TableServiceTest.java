package kitchenpos.unit.order;

import kitchenpos.order.application.TableService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.type.OrderStatus;
import kitchenpos.order.validator.OrderTableValidator;
import kitchenpos.order.dto.ChaneNumberOfGuestRequest;
import kitchenpos.order.dto.ChangeEmptyRequest;
import kitchenpos.order.dto.TableRequest;
import kitchenpos.order.dto.TableResponse;
import kitchenpos.order.port.OrderTablePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.common.constants.ErrorCodeType.COOKING_MEAL_NOT_UNGROUP;
import static kitchenpos.common.constants.ErrorCodeType.GUEST_NOT_NULL_AND_ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderTablePort orderTablePort;
    @Mock
    private OrderTableValidator orderTableValidator;

    @InjectMocks
    private TableService tableService;

    @Test
    @DisplayName("주문 테이블 등록 할 수 있다.")
    void createTable() {
        OrderTable 주문테이블 = new OrderTable(1, true);

        given(orderTablePort.save(any())).willReturn(주문테이블);

        TableResponse result = tableService.create(new TableRequest(3, false));

        assertThat(result.isEmpty()).isTrue();
        assertThat(result.getNumberOfGuests()).isEqualTo(1);
    }

    @Test
    @DisplayName("주문 테이블 리스트를 받을 수 있다.")
    void getTableList() {
        OrderTable orderTable = new OrderTable(1L, null, 7, true);
        given(orderTablePort.findAll()).willReturn(Arrays.asList(orderTable));

        List<TableResponse> result = tableService.list();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNumberOfGuests()).isEqualTo(7);
    }

    @Test
    @DisplayName("주문테이블의 상태를 변경할 수 있다.")
    void changeTableStatusEmpty() {
        OrderTable 주문테이블 = new OrderTable(1L, null, 4, false);

        given(orderTablePort.findById(any())).willReturn(주문테이블);
        doNothing().when(orderTableValidator).validChangeEmpty(주문테이블);

        TableResponse result = tableService.changeEmpty(1L, new ChangeEmptyRequest(true));

        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("주문 상태가 조리, 식사중 이면 테이블 이용여부 변경이 불가능하다.")
    void changeFailIfStatusCookingAndMeal() {
        OrderTable 주문테이블 = new OrderTable(1L, null, 4, false);
        Order 주문 = new Order(주문테이블.getId(), OrderStatus.COOKING, null);

        given(orderTablePort.findById(1L)).willReturn(주문테이블);
        doThrow(new IllegalArgumentException(COOKING_MEAL_NOT_UNGROUP.getMessage()))
                .when(orderTableValidator).validChangeEmpty(주문테이블);

        assertThatThrownBy(() ->
                tableService.changeEmpty(1L, new ChangeEmptyRequest(true)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블의 방문한 손님의 수를 바꿀 수 있다.")
    void notChangeGuestNumber() {
        OrderTable 주문테이블 = new OrderTable(1L, null, 4, false);

        given(orderTablePort.findById(any())).willReturn(주문테이블);

        TableResponse result = tableService.changeNumberOfGuests(주문테이블.getId(), new ChaneNumberOfGuestRequest(3));

        assertThat(result.getNumberOfGuests()).isEqualTo(3);
    }

    @Test
    @DisplayName("방문한 손님의 수는 0명 이상이여야한다.")
    void notChangeGuestIsMinZero() {
        OrderTable 주문테이블 = new OrderTable(1L, null, 4, false);

        given(orderTablePort.findById(any())).willReturn(주문테이블);
        doThrow(new IllegalArgumentException(GUEST_NOT_NULL_AND_ZERO.getMessage()))
                .when(orderTableValidator).validChangeNumberOfGuest(-1);

        assertThatThrownBy(() ->
                tableService.changeNumberOfGuests(1L, new ChaneNumberOfGuestRequest(-1))
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
