package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.UpdateOrderStatusRequest;
import kitchenpos.table.validator.OrderValidatorImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    MenuRepository menuRepository;
    @Mock
    OrderRepository orderRepository;
    @Mock
    OrderValidatorImpl orderValidator;
    @InjectMocks
    OrderService orderService;

    @Test
    @DisplayName("주문 추가")
    void create() {
        // given
        Menu 햄버거_메뉴 = new Menu(1L, "햄버거세트", BigDecimal.valueOf(2000), MenuGroup.of("햄버거메뉴").getId());
        doNothing().when(orderValidator).validate(any());

        given(menuRepository.findAllById(any()))
                .willReturn(Arrays.asList(햄버거_메뉴));

        given(orderRepository.save(any()))
                .willReturn(new Order());
        // when
        final OrderResponse order = orderService.create(new OrderRequest(1L, Arrays.asList(new OrderLineItemRequest(1L, 5))));
        // then
        assertThat(order).isInstanceOf(OrderResponse.class);
    }

    @Test
    @DisplayName("메뉴 조회")
    void list() {
        given(orderRepository.findAll())
                .willReturn(Arrays.asList(new Order()));
        // when
        final List<OrderResponse> list = orderService.list();
        // then
        assertThat(list).hasSize(1);
    }

    @Test
    @DisplayName("메뉴 주문 상태 변경")
    void changeOrderStatus() {
        // given
        given(orderRepository.findById(any()))
                .willReturn(Optional.of(new Order(1L, 1L, OrderStatus.MEAL)));
        // when
        final OrderResponse orderResponse = orderService.changeOrderStatus(1L, new UpdateOrderStatusRequest(OrderStatus.COMPLETION));
        // then
        assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }
}
