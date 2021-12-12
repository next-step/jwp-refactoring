package kitchenpos.application;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.menu.MenuService;
import kitchenpos.application.order.OrderService;
import kitchenpos.domain.Price;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.dto.OrderDto;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.order.Orders;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItemRepository;
import kitchenpos.domain.order.OrdersRepository;
import kitchenpos.domain.order.OrderStatus;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private MenuService menuService;

    @Mock
    private OrdersRepository orderRepository;

    @Mock
    private OrderLineItemRepository orderLineItemRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderService orderService;

    private Menu 뿌링클콤보;

    private MenuProduct 뿌링클콤보_뿌링클치킨;
    private MenuProduct 뿌링클콤보_치킨무;
    private MenuProduct 뿌링클콤보_코카콜라;

    private Product 뿌링클치킨;
    private Product 치킨무;
    private Product 코카콜라;

    private OrderTable 치킨_주문_단체테이블;
    private Orders 치킨주문;
    private OrderLineItem 치킨_주문항목;

    @BeforeEach
    void setUp() {
        뿌링클치킨 = Product.of(1L, "뿌링클치킨", Price.of(15_000));
        치킨무 = Product.of(2L, "치킨무", Price.of(1_000));
        코카콜라 = Product.of(3L, "코카콜라", Price.of(3_000));

        뿌링클콤보 = Menu.of("뿌링클콤보", Price.of(18_000));

        뿌링클콤보_뿌링클치킨 = MenuProduct.of(뿌링클콤보, 뿌링클치킨, 1L);
        뿌링클콤보_치킨무 = MenuProduct.of(뿌링클콤보, 치킨무, 1L);
        뿌링클콤보_코카콜라 = MenuProduct.of(뿌링클콤보, 코카콜라, 1L);

        뿌링클콤보_뿌링클치킨.acceptMenu(뿌링클콤보);
        뿌링클콤보_치킨무.acceptMenu(뿌링클콤보);
        뿌링클콤보_코카콜라.acceptMenu(뿌링클콤보);

        치킨_주문_단체테이블 = OrderTable.of(10, false);

        치킨_주문항목 = OrderLineItem.of(뿌링클콤보, 1L);

        치킨주문 = Orders.of(치킨_주문_단체테이블, Lists.newArrayList(치킨_주문항목));

        치킨_주문항목.acceptOrder(치킨주문);

    }

    @DisplayName("주문이 저장된다.")
    @Test
    void create_order() {
        // given
        when(menuService.countByIdIn(anyList())).thenReturn(1L);
        when(menuService.findById(null)).thenReturn(this.뿌링클콤보);
        when(orderTableRepository.findById(null)).thenReturn(Optional.of(this.치킨_주문_단체테이블));
        
        Orders 치킨주문_요청 = Orders.of(this.치킨_주문_단체테이블, List.of(this.치킨_주문항목));

        when(orderRepository.save(any(Orders.class))).thenReturn(this.치킨주문);

        // when
        OrderDto savedOrder = orderService.create(OrderDto.of(치킨주문_요청));

        // then
        assertAll(
            () -> Assertions.assertThat(savedOrder.getOrderLineItems()).hasSize(1),
            () -> Assertions.assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name())
        );
    }

    @DisplayName("주문에속하는 수량있는 메뉴가 없는 주문은 예외가 발생된다.")
    @Test
    void exception_createOrder_emptyOrderedMenu() {
        // given
        this.치킨주문.changeOrderLineItems(List.of(this.치킨_주문항목));

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> orderService.create(OrderDto.of(this.치킨주문)));
    }

    @DisplayName("미등록된 주문테이블에서 주문 시 예외가 발생된다.")
    @Test
    void exception_createOrder_notExistedOrderTable() {
        // given
        when(orderTableRepository.findById(this.치킨_주문_단체테이블.getId())).thenReturn(Optional.empty());

        this.치킨주문.changeOrderLineItems(List.of(this.치킨_주문항목));

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> orderService.create(OrderDto.of(this.치킨주문)));
    }

    @DisplayName("주문테이블이 빈테이블일 시 예외가 발생된다.")
    @Test
    void exception_createOrder_emptyOrderTable() {
        // given
        when(menuService.countByIdIn(anyList())).thenReturn(1L);
        when(orderTableRepository.findById(null)).thenReturn(Optional.of(this.치킨_주문_단체테이블));

        this.치킨주문.changeOrderLineItems(List.of(this.치킨_주문항목));

        치킨_주문_단체테이블.changeEmpty(true);

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> orderService.create(OrderDto.of(this.치킨주문)));
    }

    @DisplayName("주문이 조회된다.")
    @Test
    void search_order() {
        // given
        when(orderRepository.findAll()).thenReturn(List.of(this.치킨주문));
   
        // when
        List<OrderDto> orders = orderService.list();

        // then
        Assertions.assertThat(orders).isEqualTo(List.of(OrderDto.of(this.치킨주문)));
    }

    @DisplayName("주문의 상태가 변경된다.")
    @Test
    void update_orderStatus() {
        // given
        when(orderRepository.findById(this.치킨주문.getId())).thenReturn(Optional.of(this.치킨주문));
        when(orderRepository.save(any(Orders.class))).thenReturn(this.치킨주문);

        this.치킨주문.changeOrderStatus(OrderStatus.MEAL);

        // when
        Orders chagedOrder = orderService.changeOrderStatus(this.치킨주문.getId(), OrderDto.of(this.치킨주문));

        // then
        assertAll(
            () -> Assertions.assertThat(chagedOrder).isEqualTo(this.치킨주문),
            () -> Assertions.assertThat(chagedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL)
        );
    }

    @DisplayName("계산이 완료된 주문의 상태를 변경시 예외가 발생된다.")
    @Test
    void exception_updateOrderStatus_afterOrderStatusCompletion() {
        // given
        when(orderRepository.findById(this.치킨주문.getId())).thenReturn(Optional.of(this.치킨주문));

        this.치킨주문.changeOrderStatus(OrderStatus.COMPLETION);

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> orderService.changeOrderStatus(this.치킨주문.getId(), OrderDto.of(this.치킨주문)));
    }
}
