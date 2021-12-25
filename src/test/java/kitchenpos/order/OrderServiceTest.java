package kitchenpos.order;

import kitchenpos.AcceptanceTest;
import kitchenpos.application.OrderService;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderStatusChangeRequest;
import kitchenpos.dto.TableCreateRequest;
import kitchenpos.dto.TableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

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
        assertThatIllegalArgumentException().isThrownBy(() -> {
            orderService.create(orderCreateRequest);
        });
    }

    @Test
    @DisplayName("테이블이 존재하지 않으면 예외가 발생한다.")
    void createOrderFailBecauseOfIsNotExistTable() {
        // given
        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(1L, Arrays.asList(new OrderCreateRequest.OrderLineItem(1L, 1L)));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            orderService.create(orderCreateRequest);
        });
    }


    @Test
    @DisplayName("주문 상태 변경 시 주문이 존재하지 않으면 예외가 발생한다.")
    void changeOrderStatusFailBecauseOfIsNotExistOrder() {
        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            orderService.changeOrderStatus(1L, new OrderStatusChangeRequest(OrderStatus.COMPLETION));
        });
    }

    @Test
    @DisplayName("테이블의 상태가 사용불가라면 예외가 발생한다.")
    void createOrderFailBecauseOfIsEmptyTableStatus() {
        // given
        final TableResponse tableResponse = tableService.create(new TableCreateRequest(0, true));
        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(tableResponse.getId(), Arrays.asList(new OrderCreateRequest.OrderLineItem(1L, 1L)));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            orderService.create(orderCreateRequest);
        });
    }
}
