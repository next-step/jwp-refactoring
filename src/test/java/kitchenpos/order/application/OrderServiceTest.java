package kitchenpos.order.application;

import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.constant.OrderStatus;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.order.repository.OrderLineItemRepository;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.menu.application.MenuServiceTest.generateMenu;
import static kitchenpos.table.application.OrderTableServiceTest.generateOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("주문")
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderLineItemRepository orderLineItemRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    private Menu 메뉴1;
    private Menu 메뉴2;

    private OrderTable 메뉴테이블;

    private OrderLineItem 주문항목1;
    private OrderLineItem 주문항목2;

    private OrderLineItemRequest 주문항목요청1;
    private OrderLineItemRequest 주문항목요청2;

    private List<OrderLineItemRequest> 주문항목요청들;

    private Order 주문;

    private OrderRequest 주문요청;

    @BeforeEach
    void setUp() {
        메뉴1 = generateMenu(1L, "menu1", null, new MenuGroup(1L, null), null);
        메뉴2 = generateMenu(2L, "menu2", null, new MenuGroup(1L, null), null);

        메뉴테이블 = generateOrderTable(null, 0, false);

        주문항목1 = generateOrderLineItem(메뉴1, 2L);
        주문항목2 = generateOrderLineItem(메뉴2, 1L);

        주문항목요청1 = generateOrderLineItemRequest(메뉴1.getId(), 2L);
        주문항목요청2 = generateOrderLineItemRequest(메뉴2.getId(), 1L);

        주문항목요청들 = Arrays.asList(주문항목요청1, 주문항목요청2);

        주문 = generateOrder(메뉴테이블);

        주문요청 = generateOrderRequest(메뉴테이블.getId(), 주문항목요청들);
    }

    @Test
    @DisplayName("전체 주문을 조회할 수 있다.")
    void orderTest1() {
        List<Order> 주문들 = 주문들_생성();
        given(orderRepository.findAllJoinFetch()).willReturn(주문들);

        List<OrderResponse> 조회된_주문들 = orderService.list();

        assertThat(조회된_주문들.size()).isEqualTo(주문들.size());
    }

    @Test
    @DisplayName("새로운 주문을 추가할 수 있다.")
    void orderTest2() {
        given(menuRepository.countByIdIn(Arrays.asList(메뉴1.getId(), 메뉴2.getId()))).willReturn(2L);
        given(orderTableRepository.findById(주문.getOrderTableId())).willReturn(Optional.of(메뉴테이블));
        given(orderRepository.save(any(Order.class))).willReturn(주문);
        given(menuRepository.findById(any(Long.class))).willReturn(Optional.of(메뉴1));
        given(menuRepository.findById(any(Long.class))).willReturn(Optional.of(메뉴2));

        OrderResponse 생성된_주문 = orderService.create(주문요청);

        assertThat(생성된_주문.getId()).isEqualTo(주문.getId());
    }

    @Test
    @DisplayName("새로운 주문 추가 : 주문항목은 비어있어선 안된다.")
    void orderTest3() {
        given(menuRepository.countByIdIn(Arrays.asList(메뉴1.getId(), 메뉴2.getId()))).willReturn(2L);
        given(menuRepository.findById(any(Long.class))).willReturn(Optional.of(메뉴1));
        given(menuRepository.findById(any(Long.class))).willReturn(Optional.of(메뉴2));
        given(orderTableRepository.findById(주문요청.getOrderTableId())).willReturn(Optional.of(generateOrderTable(null, 0, true)));

        assertThatThrownBy(() -> orderService.create(주문요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("새로운 주문 추가 : 존재하지 않는 주문 테이블로 요청할 수 없다.")
    void orderTest4() {
        given(menuRepository.countByIdIn(Arrays.asList(메뉴1.getId(), 메뉴2.getId()))).willReturn(2L);
        given(menuRepository.findById(any(Long.class))).willReturn(Optional.of(메뉴1));
        given(menuRepository.findById(any(Long.class))).willReturn(Optional.of(메뉴2));
        given(orderTableRepository.findById(주문요청.getOrderTableId())).willReturn(Optional.of(generateOrderTable(null, 0, true)));

        assertThatThrownBy(() -> orderService.create(주문요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("새로운 주문 추가 : 실제로 존재하는 메뉴로만 요청할 수 있다.")
    void orderTest5() {
        given(menuRepository.countByIdIn(Arrays.asList(메뉴1.getId(), 메뉴2.getId()))).willReturn(2L);
        given(menuRepository.findById(any(Long.class))).willReturn(Optional.of(메뉴1));
        given(menuRepository.findById(any(Long.class))).willReturn(Optional.of(메뉴2));
        given(orderTableRepository.findById(주문요청.getOrderTableId())).willReturn(Optional.of(generateOrderTable(null, 0, false)));
        given(menuRepository.countByIdIn(Arrays.asList(메뉴1.getId(), 메뉴2.getId()))).willReturn(1L);

        assertThatThrownBy(() -> orderService.create(주문요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 현황을 수정할 수 있다.")
    void orderTest6() {
        given(orderRepository.findById(주문.getId())).willReturn(Optional.of(주문));
        given(orderRepository.save(주문)).willReturn(주문);

        OrderStatusRequest 변경할_주문_현황 = generateOrderStatusRequest(OrderStatus.COMPLETION.name());
        OrderResponse 변경된_주문 = orderService.changeOrderStatus(주문.getId(), 변경할_주문_현황);

        assertThat(변경된_주문.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @Test
    @DisplayName("주문 현황 수정 : 존재하지 않는 주문으로 요청할 수 없다.")
    void orderTest7() {
        OrderStatusRequest 변경할_주문_현황 = generateOrderStatusRequest(OrderStatus.COMPLETION.name());

        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), 변경할_주문_현황))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("주문 현황 수정 : 완료된 상태의 주문 현황은 수정할 수 없다.")
    void orderTest8() {
        OrderStatusRequest 변경할_주문_현황 = generateOrderStatusRequest(OrderStatus.COMPLETION.name());
        주문.changeStatus(변경할_주문_현황.getOrderStatus());

        given(orderRepository.findById(주문.getId())).willReturn(Optional.of(주문));

        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), 변경할_주문_현황))
                .isInstanceOf(IllegalArgumentException.class);

    }

    public static OrderLineItem generateOrderLineItem(Menu menu, long quantity) {
        return new OrderLineItem(null, menu, quantity);
    }

    private OrderLineItemRequest generateOrderLineItemRequest(Long menuId, long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }

    public static Order generateOrder(OrderTable orderTable) {
        return new Order(orderTable, null, null);
    }

    public static OrderRequest generateOrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequests) {
        return new OrderRequest(orderTableId, orderLineItemRequests);
    }

    private OrderStatusRequest generateOrderStatusRequest(String status) {
        return new OrderStatusRequest(status);
    }

    private List<Order> 주문들_생성() {
        return Arrays.asList(주문);
    }

}
