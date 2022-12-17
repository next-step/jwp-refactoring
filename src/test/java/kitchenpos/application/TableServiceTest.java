package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.type.OrderStatus;
import kitchenpos.dto.ChaneNumberOfGuestRequest;
import kitchenpos.dto.ChangeEmptyRequest;
import kitchenpos.dto.TableRequest;
import kitchenpos.dto.TableResponse;
import kitchenpos.port.OrderPort;
import kitchenpos.port.OrderTablePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderPort orderPort;

    @Mock
    private OrderTablePort orderTablePort;

    @InjectMocks
    private TableService tableService;

    @Test
    @DisplayName("주문 테이블 등록 할 수 있다.")
    void createTable() {
        OrderTable 주문테이블 = new OrderTable(1L, null, 4, true);
        when(tableService.create(any())).thenReturn(TableResponse.from(주문테이블));

        TableResponse result = tableService.create(new TableRequest(3, false));

        assertThat(result.getId()).isNotNull();
        assertThat(result.isEmpty()).isTrue();
        assertThat(result.getNumberOfGuests()).isEqualTo(4);

    }

    @Test
    @DisplayName("주문 테이블 리스트를 받을 수 있다.")
    void getTableList() {
        OrderTable 주문테이블_일번 = new OrderTable(1L, null, 4, true);
        OrderTable 주문테이블_이번 = new OrderTable(2L, null, 2, true);

        when(orderTablePort.findAll()).thenReturn(Arrays.asList(주문테이블_일번, 주문테이블_이번));

        List<TableResponse> result = tableService.list();

        assertThat(result).hasSize(2);
        assertThat(result).contains(TableResponse.from(주문테이블_일번), TableResponse.from(주문테이블_이번));
    }

    @Test
    @DisplayName("주문테이블의 상태를 변경할 수 있다.")
    void changeTableStatusEmpty() {
        OrderTable 주문테이블 = new OrderTable(1L, null, 4, false);

        when(orderTablePort.findById(any())).thenReturn(주문테이블);
        when(orderTablePort.save(any())).thenReturn(주문테이블);

        TableResponse result = tableService.changeEmpty(1L, new ChangeEmptyRequest(true));

        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("등록된 주문 테이블이여야 주문 테아블 상태를 변경 할 수 있다.")
    void isRegisterTable() {
        when(orderTablePort.findById(any())).thenReturn(null);

        assertThatThrownBy(() ->
                tableService.changeEmpty(1L, new ChangeEmptyRequest(true))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 상태가 조리, 식사중 이면 테이블 이용여부 변경이 불가능하다.")
    void changeFailIfStatusCookingAndMeal() {
        OrderTable 주문테이블 = new OrderTable(1L, null, 4, false);
        Order 주문 = new Order(주문테이블, OrderStatus.COOKING, null);

        when(orderTablePort.findById(1L)).thenReturn(주문테이블);
        when(orderPort.findAllByOrderTableIdIn(any())).thenReturn(Arrays.asList(주문));

        assertThatThrownBy(() ->
                tableService.changeEmpty(1L, new ChangeEmptyRequest(true)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블의 방문한 손님의 수를 바꿀 수 있다.")
    void notChangeGuestNumber() {
        OrderTable 주문테이블 = new OrderTable(1L, null, 4, false);

        when(orderTablePort.findById(any())).thenReturn(주문테이블);

        TableResponse result = tableService.changeNumberOfGuests(주문테이블.getId(), new ChaneNumberOfGuestRequest(3));

        assertThat(result.getNumberOfGuests()).isEqualTo(3);
    }

    @Test
    @DisplayName("방문한 손님의 수는 0명 이상이여야한다.")
    void notChangeGuestIsMinZero() {
        assertThatThrownBy(() ->
                tableService.changeNumberOfGuests(1L, new ChaneNumberOfGuestRequest(-1))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("등록된 주문 테이블이여아 한다.")
    void alreadyRegisterTable() {
        when(orderTablePort.findById(1L)).thenReturn(null);

        assertThatThrownBy(() ->
                tableService.changeNumberOfGuests(1L, new ChaneNumberOfGuestRequest(3))
        ).isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    @DisplayName("등록된 주문 테이블이여아 주문 테이블의 방문한 손님의 수를 바꿀 수 있다.")
    void tableIsNotEmpty() {
        OrderTable 주문테이블 = new OrderTable(1L, null, 6, true);

        when(orderTablePort.findById(any())).thenReturn(주문테이블);

        assertThatThrownBy(() ->
                tableService.changeNumberOfGuests(1L, new ChaneNumberOfGuestRequest(3))
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
