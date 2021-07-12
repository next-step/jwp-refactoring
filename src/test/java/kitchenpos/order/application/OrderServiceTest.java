package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.exception.NotFoundMenu;
import kitchenpos.order.OrderStatus;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exception.EmptyOrderLineItemsException;
import kitchenpos.order.exception.NotEqualsOrderCountAndMenuCount;
import kitchenpos.order.exception.NotFoundOrder;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("주문 기능 관련 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    private OrderLineItemRequest firstOrderLineItemRequest;
    private OrderLineItemRequest secondOrderLineItemRequest;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderLineItemRepository orderLineItemRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("주문항목이 없으면 예외발생")
    void isBlankOrderLineItem_exception() {
        // given
        // 주문 생성되어 있음
        OrderRequest orderRequest = new OrderRequest(1L, new ArrayList<>());

        // then
        // 주문 요청 시 오류 발생
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(EmptyOrderLineItemsException.class);
    }

    @Test
    @DisplayName("주문의 메뉴 수가 실제 메뉴 수와 맞지 않으면 예외발생")
    void isNotCollectQuantityMenuCount_exception() {
        // given
        // 주문 아이템 생성됨
        firstOrderLineItemRequest = 주문_아이템_생성(1L, 1L);
        secondOrderLineItemRequest = 주문_아이템_생성(2L, 1L);

        // and
        // 주문 생성되어 있음
        OrderRequest order = new OrderRequest(1L, Arrays.asList(firstOrderLineItemRequest, secondOrderLineItemRequest));

        // when
        // 실제로 한개의 메뉴만 있음
        when(menuRepository.countByIdIn(Arrays.asList(1L, 2L))).thenReturn(1L);

        // then
        // 주문 생성 요청하면 오류 발생
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(NotEqualsOrderCountAndMenuCount.class);
    }

    @Test
    @DisplayName("주문 메뉴가 없는 예외처리")
    void isNotExistMenu() {
        // given
        // 주문 아이템 생성됨
        firstOrderLineItemRequest = 주문_아이템_생성(1L, 1L);
        secondOrderLineItemRequest = 주문_아이템_생성(2L, 1L);
        when(menuRepository.countByIdIn(Arrays.asList(1L, 2L))).thenReturn(2L);

        // and
        // 주문 생성되어 있음
        OrderRequest order = new OrderRequest(1L, Arrays.asList(firstOrderLineItemRequest, secondOrderLineItemRequest));

        // when
        // 메뉴가 존재하지 않음
        when(menuRepository.findById(1L)).thenReturn(Optional.empty());

        // then
        // 주문 요청함
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(NotFoundMenu.class);
    }

    @Test
    @DisplayName("주문 정상 접수 됨")
    void 주문_정상_접수() {
        // given
        // 주문 아이템 생성됨
        firstOrderLineItemRequest = 주문_아이템_생성(1L, 1L);
        secondOrderLineItemRequest = 주문_아이템_생성(2L, 1L);
        when(menuRepository.countByIdIn(Arrays.asList(1L, 2L))).thenReturn(2L);

        // and
        // 메뉴 등록되어 있음
        Menu firstMenu = new Menu(1L);
        Menu secondMenu = new Menu(2L);
        when(menuRepository.findById(1L)).thenReturn(Optional.of(firstMenu));
        when(menuRepository.findById(2L)).thenReturn(Optional.of(secondMenu));

        // and
        // 주문 메뉴 및 테이블 생성되어 있음
        OrderRequest orderRequest = new OrderRequest(1L, Arrays.asList(firstOrderLineItemRequest, secondOrderLineItemRequest));
        OrderTable orderTable = new OrderTable(1L, 10);
        OrderLineItem firstOrderLineItem = new OrderLineItem(firstMenu, 1L);
        OrderLineItem secondOrderLineItem = new OrderLineItem(secondMenu, 1L);
        Order order = new Order(orderTable, new OrderLineItems(Arrays.asList(firstOrderLineItem, secondOrderLineItem)));
        when(orderTableRepository.findById(1L)).thenReturn(Optional.ofNullable(orderTable));

        // when
        // 주문 요청함
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        OrderResponse expected = orderService.create(orderRequest);

        // than
        // 주문 접수됨
        assertThat(expected.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @Test
    @DisplayName("주문 조회 테스트")
    void 주문_조회_테스트() {
        // given
        // 주문 아이템 생성됨
        firstOrderLineItemRequest = 주문_아이템_생성(1L, 1L);
        secondOrderLineItemRequest = 주문_아이템_생성(2L, 1L);

        // and
        // 주문 메뉴 및 테이블 생성되어 있음
        Menu firstMenu = new Menu(1L);
        Menu secondMenu = new Menu(2L);
        OrderTable orderTable = new OrderTable(1L, 10);
        OrderLineItem firstOrderLineItem = new OrderLineItem(firstMenu, 1L);
        OrderLineItem secondOrderLineItem = new OrderLineItem(secondMenu, 1L);
        Order order = new Order(orderTable, new OrderLineItems(Arrays.asList(firstOrderLineItem, secondOrderLineItem)));

        // when
        // 주문 조회 함
        when(orderRepository.findAll()).thenReturn(Arrays.asList(order));

        // than
        // 주문 조회 됨
        List<OrderResponse> orderResponseList = orderService.list();
        assertThat(orderResponseList.get(0).getOrderLineItems()).containsAll(Arrays.asList(OrderLineItemResponse.of(firstOrderLineItem)
                , OrderLineItemResponse.of(secondOrderLineItem)));
    }

    @Test
    @DisplayName("주문 상태 변경 시 없는 주문 예외")
    void 주문_상태_변경_테스트() {
        // given
        // 주문 아이템 생성됨
        firstOrderLineItemRequest = 주문_아이템_생성(1L, 1L);
        secondOrderLineItemRequest = 주문_아이템_생성(2L, 1L);

        // and
        // 주문 생성되어 있음
        OrderRequest order = new OrderRequest(1L, Arrays.asList(firstOrderLineItemRequest, secondOrderLineItemRequest));

        // then
        // 주문이 없음
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L))
                .isInstanceOf(NotFoundOrder.class);
    }

    @Test
    @DisplayName("주문 상태 변경")
    void 주문_상태_변경() {
        // given
        // 주문 아이템 생성됨
        firstOrderLineItemRequest = 주문_아이템_생성(1L, 1L);
        secondOrderLineItemRequest = 주문_아이템_생성(2L, 1L);

        // and
        // 주문 메뉴 및 테이블 생성되어 있음
        Menu firstMenu = new Menu(1L);
        Menu secondMenu = new Menu(2L);
        OrderTable orderTable = new OrderTable(1L, 10);
        OrderLineItem firstOrderLineItem = new OrderLineItem(firstMenu, 1L);
        OrderLineItem secondOrderLineItem = new OrderLineItem(secondMenu, 1L);
        Order order = new Order(orderTable, new OrderLineItems(Arrays.asList(firstOrderLineItem, secondOrderLineItem)));

        // when
        // 주문 조회됨
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // then
        // 상태 변경됨
        OrderResponse orderResponse = orderService.changeOrderStatus(1L);
        assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    private OrderLineItemRequest 주문_아이템_생성(Long menuId, Long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }
}
