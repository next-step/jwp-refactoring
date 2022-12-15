package kitchenpos.order.application;

import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.*;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;

import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    private final Product 참치김밥 = new Product(1L, "참치김밥", new Price(new BigDecimal(3000)));
    private final Product 치즈김밥 = new Product(2L, "치즈김밥", new Price(new BigDecimal(2500)));
    private final Product 라볶이 = new Product(3L, "라볶이", new Price(new BigDecimal(4500)));
    private final Product 돈까스 = new Product(4L, "돈까스", new Price(new BigDecimal(7000)));
    private final Product 쫄면 = new Product(5L, "쫄면", new Price(new BigDecimal(5000)));

    private final MenuGroup 분식 = new MenuGroup(1L, "분식");

    private final MenuProduct 라볶이세트참치김밥 = new MenuProduct(참치김밥, new Quantity(1));
    private final MenuProduct 라볶이세트라볶이 = new MenuProduct(라볶이, new Quantity(1));
    private final MenuProduct 라볶이세트돈까스 = new MenuProduct(돈까스, new Quantity(1));

    private MenuProducts 라볶이세트구성 = new MenuProducts(Arrays.asList(라볶이세트참치김밥, 라볶이세트라볶이, 라볶이세트돈까스));
    private final Menu 라볶이세트 = new Menu(1L, "라볶이세트", new Price(new BigDecimal(14000)), 분식, 라볶이세트구성);

    OrderLineItem 주문항목1 = new OrderLineItem(1L, null, 라볶이세트, new Quantity(1));
    OrderLineItem 주문항목2 = new OrderLineItem(2L, null, 라볶이세트, new Quantity(1));
    OrderTable 주문테이블 = new OrderTable(1L, null, new NumberOfGuests(4), false);
    Order 주문 = new Order(1L, 주문테이블, OrderStatus.COOKING, LocalDateTime.now(),
            new OrderLineItems(Arrays.asList(주문항목1, 주문항목2)));

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuGroupService menuGroupService;
    @Mock
    private ProductService productService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    private MenuService menuService;
    private TableService tableService;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuRepository, menuGroupService, productService);
        tableService = new TableService(orderRepository, orderTableRepository);
        orderService = new OrderService(menuService, orderRepository, tableService);
    }

    @DisplayName("주문생성 테스트")
    @Test
    void createOrderTest() {
        //given
        when(orderTableRepository.findById(주문테이블.getId()))
                .thenReturn(Optional.ofNullable(주문테이블));
        given(orderRepository.save(any(Order.class)))
                .willReturn(주문);
        when(menuRepository.findById(라볶이세트.getId()))
                .thenReturn(Optional.ofNullable(라볶이세트));

        //when
        List<OrderLineItemRequest> orderLineItemRequests = Arrays.asList(new OrderLineItemRequest(라볶이세트.getId(), 1L));
        OrderResponse result = orderService.create(new OrderRequest(1L, orderLineItemRequests));

        //then
        assertAll(
                () -> assertThat(result.getId())
                        .isEqualTo(주문.getId()),
                () -> assertThat(result.getOrderStatus())
                        .isEqualTo(주문.getOrderStatus().name()),
                () -> assertThat(result.getOrderTableResponse().getId())
                        .isEqualTo(주문.getOrderTable().getId()),
                () -> assertThat(result.getOrderLineItems().stream().map(OrderLineItemResponse::getSeq).collect(Collectors.toList()))
                        .containsAll(주문.getOrderLineItems().getValue().stream().map(OrderLineItem::getSeq).collect(Collectors.toList()))
        );
    }


    @DisplayName("빈 주문항목 목록으로 주문생성 오류 테스트")
    @Test
    void createOrderEmptyOrderLinesExceptionTest() {
        assertThatThrownBy(() -> orderService.create(new OrderRequest(주문테이블.getId(), Arrays.asList())))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("주문항목에 있는 메뉴가 존재하지 않는 경우 주문생성 오류 테스트")
    @Test
    void createOrderWithNotExistMenuExceptionTest() {
        //given
        OrderLineItem 주문항목1 = new OrderLineItem(1L, null, 라볶이세트, new Quantity(1));
        OrderLineItem 주문항목2 = new OrderLineItem(2L, null, 라볶이세트, new Quantity(1));
        OrderTable 주문테이블 = new OrderTable(1L, null, new NumberOfGuests(4), false);
        Order 주문 = new Order(1L, 주문테이블, OrderStatus.COOKING, LocalDateTime.now(),
                new OrderLineItems(Arrays.asList(주문항목1, 주문항목2)));

        when(menuRepository.findById(라볶이세트.getId()))
                .thenReturn(Optional.ofNullable(null));


        //when
        List<OrderLineItemRequest> orderLineItemRequests = Arrays.asList(new OrderLineItemRequest(라볶이세트.getId(), 1L));
        assertThatThrownBy(() -> orderService.create(new OrderRequest(1L, orderLineItemRequests)))
                .isInstanceOf(IllegalArgumentException.class);
    }

//    @DisplayName("존재하지 않는 주문테이블로 주문생성 오류 테스트")
//    @Test
//    void createOrderWithNotExistOrderTableExceptionTest() {
//        //given
//        OrderLineItem 주문항목1 = new OrderLineItem(1L, 1L, 1L, 1);
//        OrderLineItem 주문항목2 = new OrderLineItem(2L, 1L, 2L, 1);
//        Order 주문 = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(),
//                Arrays.asList(주문항목1, 주문항목2));
//
//        when(menuRepository.findAllById(주문.getOrderLineItems().stream().map(OrderLineItem::getMenuId).collect(Collectors.toList())).size())
//                .thenReturn(주문.getOrderLineItems().size());
//        when(orderTableRepository.findById(주문.getOrderTableId())).thenReturn(Optional.ofNullable(null));
//
//        //when
//        //then
//        assertThatThrownBy(() -> orderService.create(주문)).isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @DisplayName("빈 주문테이블로 주문생성 오류 테스트")
//    @Test
//    void createOrderWithEmptyOrderTableExceptionTest() {
//        //given
//        OrderLineItem 주문항목1 = new OrderLineItem(1L, 1L, 1L, 1);
//        OrderLineItem 주문항목2 = new OrderLineItem(2L, 1L, 2L, 1);
//        OrderTable 주문테이블 = new OrderTable(1L, null, new NumberOfGuests(0), true);
//        Order 주문 = new Order(1L, 주문테이블.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(),
//                Arrays.asList(주문항목1, 주문항목2));
//
//        when(menuRepository.findAllById(주문.getOrderLineItems().stream().map(OrderLineItem::getMenuId).collect(Collectors.toList())).size())
//                .thenReturn(주문.getOrderLineItems().size());
//        when(orderTableRepository.findById(주문.getOrderTableId())).thenReturn(Optional.ofNullable(주문테이블));
//
//        //when
//        //then
//        assertThatThrownBy(() -> orderService.create(주문)).isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @DisplayName("주문목록 조회 테스트")
//    @Test
//    void retrieveOrdersTest() {
//        //given
//        OrderLineItem 주문항목1 = new OrderLineItem(1L, 1L, 1L, 1);
//        OrderLineItem 주문항목2 = new OrderLineItem(2L, 1L, 2L, 1);
//        OrderLineItem 주문항목3 = new OrderLineItem(3L, 2L, 1L, 1);
//        Order 주문1 = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(),
//                Arrays.asList(주문항목1, 주문항목2));
//        Order 주문2 = new Order(2L, 2L, OrderStatus.COOKING.name(), LocalDateTime.now(),
//                Arrays.asList(주문항목3));
//        when(orderDao.findAll()).thenReturn(Arrays.asList(주문1, 주문2));
//        for(final Order order : Arrays.asList(주문1, 주문2)) {
//            when(orderLineItemDao.findAllByOrderId(order.getId())).thenReturn(order.getOrderLineItems());
//        }
//
//        //when
//        List<Order> orders = orderService.list();
//
//        //then
//        assertThat(orders).contains(주문1, 주문2);
//    }
//
//    @DisplayName("주문상태변경 테스트")
//    @Test
//    void changeOrderStatusTest() {
//        //given
//        OrderLineItem 주문항목1 = new OrderLineItem(1L, 1L, 1L, 1);
//        OrderLineItem 주문항목2 = new OrderLineItem(2L, 1L, 2L, 1);
//        OrderTable 주문테이블 = new OrderTable(1L, null, new NumberOfGuests(4), false);
//        Order 주문 = new Order(1L, 주문테이블.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(),
//                Arrays.asList(주문항목1, 주문항목2));
//
//        when(orderDao.findById(주문.getId())).thenReturn(Optional.ofNullable(주문));
//        when(orderDao.save(주문)).thenReturn(주문);
//        when(orderLineItemDao.findAllByOrderId(주문.getId())).thenReturn(주문.getOrderLineItems());
//
//        //when
//        Order result = orderService.changeOrderStatus(주문.getId(), 주문);
//
//        //then
//        assertThat(result).isEqualTo(주문);
//    }
//
//    @DisplayName("존재하지 않는 주문의 상태변경 테스트")
//    @Test
//    void changeOrderStatusWithNotExistIdExceptionTest() {
//        //given
//        OrderLineItem 주문항목1 = new OrderLineItem(1L, 1L, 1L, 1);
//        OrderLineItem 주문항목2 = new OrderLineItem(2L, 1L, 2L, 1);
//        OrderTable 주문테이블 = new OrderTable(1L, null, new NumberOfGuests(4), false);
//        Order 주문 = new Order(1L, 주문테이블.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(),
//                Arrays.asList(주문항목1, 주문항목2));
//
//        when(orderDao.findById(주문.getId())).thenReturn(Optional.ofNullable(null));
//
//        //when
//        //then
//        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), 주문))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @DisplayName("완료상태인 주문의 상태변경 오류 테스트")
//    @Test
//    void changeCompleteOrderStatusExceptionTest() {
//        //given
//        OrderLineItem 주문항목1 = new OrderLineItem(1L, 1L, 1L, 1);
//        OrderLineItem 주문항목2 = new OrderLineItem(2L, 1L, 2L, 1);
//        OrderTable 주문테이블 = new OrderTable(1L, null, new NumberOfGuests(4), false);
//        Order 주문 = new Order(1L, 주문테이블.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now(),
//                Arrays.asList(주문항목1, 주문항목2));
//
//        when(orderDao.findById(주문.getId())).thenReturn(Optional.ofNullable(주문));
//
//        //when
//        //then
//        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), 주문))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
}
