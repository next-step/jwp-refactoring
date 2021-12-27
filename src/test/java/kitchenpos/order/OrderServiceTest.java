package kitchenpos.order;

import kitchenpos.AcceptanceTest;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderStatusChangeRequest;
import kitchenpos.table.application.TableService;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.exception.TableNotAvailableException;
import kitchenpos.global.exception.EntityNotFoundException;
import kitchenpos.table.dto.TableCreateRequest;
import kitchenpos.table.dto.TableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 관련 기능")
class OrderServiceTest extends AcceptanceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private TableService tableService;

    @Test
    @DisplayName("주문한 메뉴가 존재하지 않으면 예외가 발생한다.")
    void createOrderFailBecauseOfIsNotExistMenu() {
        // given
        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(1L, Arrays.asList(new OrderCreateRequest.OrderLineItem(1L, 1L)));

        // when
        assertThatThrownBy(() -> {
            orderService.create(orderCreateRequest);
        }).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("테이블이 존재하지 않으면 예외가 발생한다.")
    void createOrderFailBecauseOfIsNotExistTable() {
        // given
        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(1L, Arrays.asList(new OrderCreateRequest.OrderLineItem(1L, 1L)));

        // when
        assertThatThrownBy(() -> {
            orderService.create(orderCreateRequest);
        }).isInstanceOf(EntityNotFoundException.class);
    }


    @Test
    @DisplayName("주문 상태 변경 시 주문이 존재하지 않으면 예외가 발생한다.")
    void changeOrderStatusFailBecauseOfIsNotExistOrder() {
        // when
        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(1L, new OrderStatusChangeRequest(OrderStatus.COMPLETION));
        }).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("테이블의 상태가 사용불가라면 예외가 발생한다.")
    void createOrderFailBecauseOfIsEmptyTableStatus() {
        // given
        final TableResponse tableResponse = tableService.create(new TableCreateRequest(0, true));
        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(tableResponse.getId(), Arrays.asList(new OrderCreateRequest.OrderLineItem(1L, 1L)));

        // when
        assertThatThrownBy(() -> {
            orderService.create(orderCreateRequest);
        }).isInstanceOf(TableNotAvailableException.class);
    }
}
