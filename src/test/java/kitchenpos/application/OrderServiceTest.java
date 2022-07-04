package kitchenpos.application;

import kitchenpos.fixture.TestMenuFactory;
import kitchenpos.fixture.TestMenuGroupFactory;
import kitchenpos.fixture.TestProductFactory;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.application.OrderMapper;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderMapper orderMapperMock;
    @Mock
    private OrderValidator orderValidator;

    @InjectMocks
    private OrderService orderService;

    private MenuGroup 분식류;
    private Product 진매;
    private Product 진순이;
    private Menu 메뉴;
    private OrderRequest 주문_요청;
    private Order 주문;
    private OrderLineItemRequest 주문_메뉴;

    @BeforeEach
    void setUp() {
        분식류 = TestMenuGroupFactory.create(1L, "분식류");

        진매 = TestProductFactory.create(1L, "진라면 매운맛", 5_000);
        진순이 = TestProductFactory.create(2L, "진라면 순한맛", 5_000);

        메뉴 = TestMenuFactory.create(10L, 4_000, 분식류.getId(), "라면메뉴", Arrays.asList(new MenuProduct(1L, 1), new MenuProduct(2L, 1)));

        주문_메뉴 = new OrderLineItemRequest(메뉴.getId(), 3);
        주문_요청 = new OrderRequest(1L, Collections.singletonList(주문_메뉴));
        주문 = new Order(1L);
        주문.addOrderLineItems(Collections.singletonList(주문_메뉴));
    }

    @DisplayName("주문을 등록할 수 있다")
    @Test
    void create() throws Exception {
        // given
        OrderRequest orderRequest = new OrderRequest(1L, Collections.singletonList(주문_메뉴));
        given(orderRepository.save(any())).willReturn(주문);
        given(orderMapperMock.mapFrom(any())).willReturn(주문);
        doNothing().when(orderValidator).validate(any());

        // when
        OrderResponse orderResponse = orderService.create(orderRequest);

        // then
        assertThat(orderResponse.getId()).isEqualTo(OrderResponse.of(new Order(1L)).getId());
    }

    @DisplayName("주문 항목이 비어있으면 등록할 수 없다")
    @Test
    void createException1() throws Exception {
        // given
        OrderRequest orderRequest = new OrderRequest(1L, null);
        given(orderMapperMock.mapFrom(any())).willThrow(IllegalArgumentException.class);
        // when & then
        assertThatThrownBy(() -> orderService.create(orderRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목의 메뉴가 존재하지 않으면 등록할 수 없다")
    @Test
    void createException2() throws Exception {
        given(orderMapperMock.mapFrom(any())).willThrow(IllegalArgumentException.class);

        // when & then
        assertThatThrownBy(() -> orderService.create(주문_요청)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목의 주문테이블이 존재하지 않으면 등록할 수 없다")
    @Test
    void createException3() throws Exception {
        //given
        given(orderMapperMock.mapFrom(주문_요청)).willThrow(IllegalArgumentException.class);

        // when & then
        assertThatThrownBy(() -> orderService.create(주문_요청)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전체 주문목록을 조회할 수 있다")
    @Test
    void list() throws Exception {
        // given
        given(orderRepository.findAll()).willReturn(Collections.singletonList(주문));

        // when
        List<OrderResponse> list = orderService.list();

        // then
        assertThat(list).hasSize(1);
    }

    @DisplayName("주문상태를 변경한다")
    @ParameterizedTest
    @CsvSource(value = {"MEAL", "COOKING", "COMPLETION"})
    void change(OrderStatus newOrderStatus) throws Exception {
        // given
        주문_요청 = new OrderRequest(1L, Collections.singletonList(주문_메뉴));

        given(orderRepository.findById(anyLong())).willReturn(Optional.of(주문));

        // when
        OrderResponse changedOrder = orderService.changeOrderStatus(1L, new OrderStatusRequest(newOrderStatus));

        // then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(newOrderStatus.toString());
    }

    @DisplayName("주문상태가 COMPLETION 이면 상태를 변경할 수 없다")
    @ParameterizedTest
    @CsvSource({"MEAL", "COOKING", "COMPLETION"})
    void changeException(OrderStatus orderStatus) throws Exception {
        // given
        Order order = new Order(1L, 1L, OrderStatus.COMPLETION);
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, new OrderStatusRequest(orderStatus)))
                .isInstanceOf(IllegalArgumentException.class);
    }


}
