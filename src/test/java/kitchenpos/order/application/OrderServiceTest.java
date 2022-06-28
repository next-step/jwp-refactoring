package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.application.MenuGroupServiceTest;
import kitchenpos.menu.application.MenuServiceTest;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.domain.repository.OrderLineItemRepository;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.product.application.ProductServiceTest;
import kitchenpos.table.application.TableServiceTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
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
    private OrderLineItemRepository orderLineItemRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private OrderService orderService;

    private Product product;
    private MenuGroup menuGroup;
    private Menu menu;
    private OrderTable orderTable;
    private List<OrderLineItem> orderLineItems;
    private Order order;

    @BeforeEach
    void setUp() {
        product = ProductServiceTest.상품_생성(1L, "봉골레파스타", 13000);
        menuGroup = MenuGroupServiceTest.메뉴_그룹_생성(1L, "파스타메뉴");
        menu = MenuServiceTest.메뉴_생성(1L, "봉골레파스타세트", product.getPrice(), menuGroup.getId(),
                Arrays.asList(MenuServiceTest.메뉴_상품_생성(1L, 0L, product.getId(), 1L)));
        orderTable = TableServiceTest.테이블_생성(1L, 2, false);
        orderLineItems = Arrays.asList(주문_항목_생성(0L, menu.getId(), 1L));

        order = 주문_생성(1L, orderTable.getId(), orderLineItems);
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void create() {
        //given
        given(menuRepository.countByIdIn(any())).willReturn(menu.getId());
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));
        given(orderRepository.save(any())).willReturn(order);

        //when
        Order createdOrder = orderService.create(order);

        //then
        assertThat(createdOrder).isNotNull();
        assertThat(createdOrder.getId()).isEqualTo(order.getId());
    }

    @DisplayName("주문 항목이 없으면, 주문 등록에 실패한다.")
    @Test
    void create_invalidOrderLineItemsSize() {
        //given
        order.setOrderLineItems(new ArrayList<>());

        //when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목이 존재하지 않는 메뉴라면, 주문 등록에 실패한다.")
    @Test
    void create_invalidNotExistsMenu() {
        //given
        given(menuRepository.countByIdIn(any())).willReturn(0L);

        //when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 테이블이 존재하지 않는 테이블이라면, 주문 등록에 실패한다.")
    @Test
    void create_invalidNotExistsTable() {
        //given
        given(menuRepository.countByIdIn(any())).willReturn(menu.getId());
        given(orderTableRepository.findById(any())).willReturn(Optional.ofNullable(null));

        //when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 테이블이 빈 테이블이라면, 주문 등록에 실패한다.")
    @Test
    void create_invalidEmptyTable() {
        //given
        orderTable.setEmpty(true);
        given(menuRepository.countByIdIn(any())).willReturn(menu.getId());
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));

        //when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() {
        //given
        given(orderRepository.findAll()).willReturn(Arrays.asList(order));

        //when
        List<Order> orders = orderService.list();

        //then
        assertThat(orders).hasSize(1);
        assertThat(orders).containsExactly(order);
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        //given
        given(orderRepository.findById(any())).willReturn(Optional.of(order));
        given(orderRepository.save(any())).willReturn(order);
        given(orderLineItemRepository.findAllByOrderId(any())).willReturn(orderLineItems);
        Order newOrder = new Order();
        newOrder.setOrderStatus(OrderStatus.COOKING.name());

        //when
        Order changedOrder = orderService.changeOrderStatus(order.getId(), newOrder);

        //then
        assertThat(changedOrder).isNotNull();
        assertThat(changedOrder.getOrderStatus()).isEqualTo(newOrder.getOrderStatus());
    }

    @DisplayName("존재하지 않는 주문의 상태는 변경할 수 없다.")
    @Test
    void changeOrderStatus_invalidNotExistsOrder() {
        //given
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        given(orderRepository.findById(any())).willReturn(Optional.of(order));

        //when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), new Order()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태가 완료인 주문의 상태는 변경할 수 없다.")
    @Test
    void changeOrderStatus_invalidCompletion() {
        //given
        given(orderRepository.findById(any())).willReturn(Optional.ofNullable(null));

        //when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), new Order()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    public static Order 주문_생성(Long id, Long orderTableId, List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setId(id);
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(orderLineItems);
        return order;
    }

    public static OrderLineItem 주문_항목_생성(Long seq, Long menuId, Long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(seq);
        orderLineItem.setOrderId(null);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }
}
