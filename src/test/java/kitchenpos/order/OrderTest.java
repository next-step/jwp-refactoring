package kitchenpos.order;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.exception.CannotChangeOrderStatusException;
import kitchenpos.table.application.TableService;
import kitchenpos.table.repository.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@DisplayName("주문")
class OrderTest {

    @Test
    @DisplayName("주문 상태 변경 시 주문이 완료 상태면 예외가 발생한다.")
    void changeOrderStatusFailBecauseOfOrderStatusCompletion() {
        // given
        final MenuRepository menuRepository = mock(MenuRepository.class);
        final OrderTableRepository orderTableRepository = mock(OrderTableRepository.class);
        final OrderValidator orderValidator = new OrderValidator(menuRepository, orderTableRepository);


        final Order order = new Order(OrderStatus.COMPLETION);

        // when
        assertThatThrownBy(() -> {
            order.changeOrderStatus(OrderStatus.COOKING, orderValidator);
        }).isInstanceOf(CannotChangeOrderStatusException.class);
    }
}
