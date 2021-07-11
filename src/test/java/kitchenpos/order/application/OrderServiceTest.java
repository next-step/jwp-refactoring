package kitchenpos.order.application;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenposNew.menu.domain.Menu;
import kitchenposNew.menu.domain.MenuRepository;
import kitchenposNew.menu.exception.NotFoundMenu;
import kitchenposNew.order.OrderStatus;
import kitchenposNew.order.application.OrderService;
import kitchenposNew.order.domain.Order;
import kitchenposNew.order.domain.OrderLineItem;
import kitchenposNew.order.domain.OrderLineItemRepository;
import kitchenposNew.order.domain.OrderRepository;
import kitchenposNew.order.dto.OrderLineItemRequest;
import kitchenposNew.order.dto.OrderLineItemResponse;
import kitchenposNew.order.dto.OrderRequest;
import kitchenposNew.order.dto.OrderResponse;
import kitchenposNew.order.exception.EmptyOrderLineItemsException;
import kitchenposNew.order.exception.NotEqualsOrderCountAndMenuCount;
import kitchenposNew.order.exception.NotFoundOrderTable;
import kitchenposNew.wrap.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
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
    private OrderTableDao orderTableDao;

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
        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        OrderLineItem firstOrderLineItem = new OrderLineItem(firstMenu, 1L);
        OrderLineItem secondOrderLineItem = new OrderLineItem(secondMenu, 1L);
        Order order = new Order(orderTable, Arrays.asList(firstOrderLineItem, secondOrderLineItem));
        when(orderTableDao.findById(1L)).thenReturn(Optional.ofNullable(orderTable));

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
        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        OrderLineItem firstOrderLineItem = new OrderLineItem(firstMenu, 1L);
        OrderLineItem secondOrderLineItem = new OrderLineItem(secondMenu, 1L);
        Order order = new Order(orderTable, Arrays.asList(firstOrderLineItem, secondOrderLineItem));

        // when
        // 주문 조회 함
        when(orderRepository.findAll()).thenReturn(Arrays.asList(order));

        // than
        // 주문 조회 됨
        List<OrderResponse> orderResponseList = orderService.list();
        assertThat(orderResponseList.get(0).getOrderLineItems()).containsAll(Arrays.asList(OrderLineItemResponse.of(firstOrderLineItem)
                                                                                            , OrderLineItemResponse.of(secondOrderLineItem)));

    }

    private OrderLineItemRequest 주문_아이템_생성(Long menuId, Long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }
}
