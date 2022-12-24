package kitchenpos.order;

import static kitchenpos.menu.MenuFixture.더블강정치킨;
import static kitchenpos.order.OrderFixture.createOrderRequest;
import static kitchenpos.order.OrderFixture.주문;
import static kitchenpos.order.OrderFixture.주문항목;
import static kitchenpos.table.TableFixture.일번테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.dao.MenuRepository;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.dao.OrderRepository;
import kitchenpos.order.dao.OrderTableRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("주문 생성")
    void createOrder() {
        //given
        when(orderTableRepository.findById(any()))
            .thenReturn(Optional.of(일번테이블));
        when(menuRepository.findById(any()))
            .thenReturn(Optional.of(더블강정치킨));
        when(orderRepository.save(any())).then(returnsFirstArg());

        //when
        OrderResponse orderResponse = orderService.create(createOrderRequest(주문));

        //then
        assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(orderResponse.getOrderedTime()).isNotNull();
    }

    @Test
    @DisplayName("주문시 테이블이 없으면 에러 발생")
    void noTableException() {
        //given
        when(orderTableRepository.findById(any()))
            .thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> orderService.create(createOrderRequest(주문)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("주문 테이블 정보가 없습니다.");
    }

    @Test
    @DisplayName("주문시 메뉴 정보가 없으면 에러 발생")
    void noMenuException() {
        //given
        when(orderTableRepository.findById(any()))
            .thenReturn(Optional.of(일번테이블));
        when(menuRepository.findById(any()))
            .thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> orderService.create(createOrderRequest(주문)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("메뉴 정보가 없습니다.");
    }

    @Test
    @DisplayName("주문 목록 조회")
    void getMenuList() {
        //given
        when(orderRepository.findAll()).thenReturn(Collections.singletonList(주문));

        //when
        List<OrderResponse> list = orderService.list();

        //then
        assertThat(list)
            .hasSize(1)
            .containsExactly(OrderResponse.from(주문));
    }

    @Test
    @DisplayName("주문 상태 변경")
    void changeMenuStatus() {
        //given
        Order order = new Order(1L, 일번테이블, null, null,
            Collections.singletonList(주문항목));
        when(orderRepository.findById(any())).thenReturn(Optional.of(order));
        when(orderRepository.save(any())).then(returnsFirstArg());

        //when
        OrderResponse orderResponse = orderService
            .changeOrderStatus(1L, OrderStatus.COOKING.name());

        //then
        assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @Test
    @DisplayName("주문 상태 변경할 주문이 없으면 에러 발생")
    void noOrderException() {
        //given
        when(orderRepository.findById(any())).thenReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> orderService
            .changeOrderStatus(1L, OrderStatus.COOKING.name()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("주문 정보가 없습니다");
    }
}
