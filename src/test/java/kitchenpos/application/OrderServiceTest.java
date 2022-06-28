package kitchenpos.application;

import static kitchenpos.helper.ReflectionHelper.setMenuId;
import static kitchenpos.helper.ReflectionHelper.setOrderId;
import static kitchenpos.helper.ReflectionHelper.setOrderLineItemId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.domainService.MenuOrderLineDomainService;
import kitchenpos.dto.dto.OrderLineItemDTO;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private OrderService orderService;

    @Mock
    private MenuOrderLineDomainService menuOrderLineDomainService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    private Order order;
    private MenuProduct chicken_menuProduct;
    private MenuProduct ham_menuProduct;
    private OrderLineItemDTO chickenOrder;
    private OrderLineItemDTO hamOrder;
    private OrderTable orderTable;

    @BeforeEach
    public void init() {
        setMenu();
        setOrderLineItem();
        setOrderTable();
        orderService = new OrderService(menuOrderLineDomainService, orderRepository, orderTableRepository);

        order = new Order();
        setOrderId(1L, order);

        OrderLineItem hamOrderLineItem = new OrderLineItem(null, ham_menuProduct.getMenu().getId(),
            1L);
        OrderLineItem chickenOrderLineItem = new OrderLineItem(null, chicken_menuProduct.getMenu().getId(),
            1L);
        setOrderLineItemId(1L, chickenOrderLineItem);
        setOrderLineItemId(2L, hamOrderLineItem);
        order.mapOrderLineItem(hamOrderLineItem);
        order.mapOrderLineItem(chickenOrderLineItem);

        order.startCooking();
    }

    private void setOrderTable() {
        orderTable = new OrderTable(0, true);
    }

    private void setMenu() {
        Product chicken = new Product("chicken", BigDecimal.valueOf(5000));
        Menu oneChickenMenu = new Menu("치킨한마리", BigDecimal.valueOf(4000), 1L);
        setMenuId(1L, oneChickenMenu);
        chicken_menuProduct = new MenuProduct(oneChickenMenu, 1L, 1L);

        Product ham = new Product("ham", BigDecimal.valueOf(4000));
        Menu oneHamMenu = new Menu("햄한개", BigDecimal.valueOf(3000), 1L);
        setMenuId(2L, oneHamMenu);
        ham_menuProduct = new MenuProduct(oneHamMenu, 2L, 1L);
    }

    private void setOrderLineItem() {
        chickenOrder = new OrderLineItemDTO();
        chickenOrder.setMenuId(chicken_menuProduct.getMenu().getId());
        chickenOrder.setQuantity(1L);

        hamOrder = new OrderLineItemDTO();
        hamOrder.setMenuId(ham_menuProduct.getMenu().getId());
        hamOrder.setQuantity(2L);
    }

    @Test
    @DisplayName("주문 생성 정상로직")
    void createOrderHappyCase() {
        //given
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderLineItems(Arrays.asList(chickenOrder, hamOrder));

        orderRequest.setOrderTableId(1L);
        orderTable.changeIsEmpty(false);

        doNothing().when(menuOrderLineDomainService).validateComponentForCreateOrder(orderRequest);
        when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable));
        when(orderRepository.save(any())).thenReturn(order);

        //when
        OrderResponse orderResponse = orderService.create(orderRequest);

        //then
        assertAll(
            () -> assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
            () -> assertThat(orderResponse.getOrderLineItems()).hasSize(2)
        );
    }

    @Test
    @DisplayName("주문 상품 없이 주문을 하면 에러 발생")
    void createWithoutItemsThrowError() {
        //given
        OrderRequest orderRequest = new OrderRequest();

        //when && then
        assertThatThrownBy(() -> orderService.create(orderRequest)).isInstanceOf(
            IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 상품이 존재하지 않는것이 있다면 에러 발생")
    void createWithoutOrderThrowError() {
        //given
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderLineItems(Arrays.asList(chickenOrder, hamOrder));

        doThrow(IllegalArgumentException.class).when(menuOrderLineDomainService)
            .validateComponentForCreateOrder(orderRequest);

        //when && then
        assertThatThrownBy(() -> orderService.create(orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문할 테이블이 존재하지 않다면 에러 발생")
    void createWithNotSavedTableThrowError() {
        //given
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderLineItems(Arrays.asList(chickenOrder, hamOrder));

        doThrow(IllegalArgumentException.class).when(menuOrderLineDomainService)
            .validateComponentForCreateOrder(orderRequest);

        //when && then
        assertThatThrownBy(() -> orderService.create(orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문할 테이블이 비어있다면 에러 발생")
    void createWithEmptyTableThrowError() {
        //given
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderLineItems(Arrays.asList(chickenOrder, hamOrder));
        orderRequest.setOrderTableId(1L);

        doNothing().when(menuOrderLineDomainService).validateComponentForCreateOrder(orderRequest);
        when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable));

        //when && then
        assertThatThrownBy(() -> orderService.create(orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("오더 상태 수정 정상로직")
    void changeOrderStatusHappyCase() {
        //given
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderStatus(OrderStatus.COOKING);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any())).thenReturn(order);

        //when
        OrderResponse orderResponse = orderService.changeOrderStatus(1L, orderRequest);

        assertThat(orderResponse.getOrderStatus()).isEqualTo("COOKING");
    }

    @Test
    @DisplayName("이미 완료된 오더 수정시 에러 발생")
    void changeOrderStatusAlreadyCompleteThrowError() {
        //given
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderStatus(OrderStatus.COMPLETION);
        order.changeOrderStatus(OrderStatus.COMPLETION);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        //when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }
}