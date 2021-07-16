package kitchenpos.order.application;

import kitchenpos.advice.exception.OrderTableException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.application.TableService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@SpringBootTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @MockBean
    private OrderRepository orderRepository;

    @DisplayName("테이블을 생성한다")
    @Test
    void create() {
        OrderTable orderTable = 테이블을_생성한다(0, true);

        assertAll(
            () -> assertEquals(orderTable.getNumberOfGuests(), 0),
            () -> assertEquals(orderTable.isEmpty(), true)
        );
    }

    @DisplayName("테이블들을 조회한다")
    @Test
    void list() {
        List<OrderTable> list = tableService.list();

        assertThat(list.size()).isGreaterThanOrEqualTo(1);
    }

    @DisplayName("테이블의 상태를 변경할 수 있다")
    @Test
    void changeEmpty() {
        OrderTable orderTable = 테이블을_생성한다(0, true);
        orderTable.updateEmpty(false);
        when(orderRepository.existsByOrderTableAndOrderStatusIn(any(), anyList())).thenReturn(false);
        OrderTable changeOrderTable = tableService.changeEmpty(orderTable.getId(), OrderTableRequest.of(orderTable));

        assertThat(changeOrderTable.isEmpty()).isFalse();
    }

    @DisplayName("테이블의 상태를 변경할 수 없다 : OrderStatus가 COOKING, MEAL인 경우")
    @Test
    void changeEmptyException() {
        OrderTable orderTable = 테이블을_생성한다(0, true);
        orderTable.updateEmpty(false);
        when(orderRepository.existsByOrderTableAndOrderStatusIn(any(), anyList())).thenReturn(true);
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), OrderTableRequest.of(orderTable)))
            .isInstanceOf(OrderTableException.class);
    }

    @DisplayName("테이블의 고객수를 변경할 수 있다")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = 테이블을_생성한다(0, false);
        orderTable.updateNumberOfGuests(10);
        OrderTable changeOrderTable = tableService.changeNumberOfGuests(orderTable.getId(), OrderTableRequest.of(orderTable));

        assertThat(changeOrderTable.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
    }

    @DisplayName("테이블의 고객수를 변경할 수 없다 : 게스트수가 0미만인 경우")
    @Test
    void changeNumberOfGuestsNumberException() {
        OrderTable orderTable = 테이블을_생성한다(0, false);
        orderTable.updateNumberOfGuests(-1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), OrderTableRequest.of(orderTable)))
            .isInstanceOf(OrderTableException.class);
    }

    @DisplayName("테이블의 고객수를 변경할 수 없다 : 테이블 상태가 비어있는 경우")
    @Test
    void changeNumberOfGuestsStatusException() {
        OrderTable orderTable = 테이블을_생성한다(0, true);
        orderTable.updateNumberOfGuests(10);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), OrderTableRequest.of(orderTable)))
            .isInstanceOf(OrderTableException.class);
    }

    private OrderTable 테이블을_생성한다(int numberOfGuest, boolean empty) {
        return tableService.create(new OrderTableRequest(numberOfGuest, empty));
    }
}
