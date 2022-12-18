package kitchenpos.order;

import static kitchenpos.order.domain.OrderStatus.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.exception.NotExistIdException;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.Quantity;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderTableValidator;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.exception.MenuFindException;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableValidator orderTableValidator;
    @InjectMocks
    private OrderService orderService;

    @DisplayName("[주문 등록] 등록된 메뉴만 주문 등록할 수 있다")
    @Test
    void test2() {
        when(menuRepository.findAllById(any())).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> orderService.create(new OrderRequest(
            Collections.singletonList(new OrderLineItemRequest(1L, new Quantity(1L))))))
            .isInstanceOf(MenuFindException.class);
    }

    @DisplayName("[주문상태 변경] 등록된 주문만 상태변경할 수 있다")
    @Test
    void test3() {
        when(orderRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, COOKING))
            .isInstanceOf(NotExistIdException.class);
    }

}
