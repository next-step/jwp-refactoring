package kitchenpos.application;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.ChangeOrderStatusRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ServiceTest {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderService orderService;

    private Menu 중식_메뉴;
    private MenuGroup 중식;
    private MenuProduct 중식_메뉴_짬뽕;
    private MenuProduct 중식_메뉴_짜장;
    private Product 짬뽕;
    private Product 짜장;
    private Order 주문1;
    private OrderTable 주문_테이블;
    private OrderTable 빈주문_테이블;
    private OrderLineItem 중식_주문_항목;

    @BeforeEach
    void before() {
        중식 = menuGroupRepository.save(new MenuGroup("중식"));
        중식_메뉴 = menuRepository.save(new Menu("중식_메뉴", BigDecimal.valueOf(3000), 중식.getId()));

        짬뽕 = productRepository.save(new Product("짬뽕", BigDecimal.valueOf(1000)));
        짜장 = productRepository.save(new Product("짜장", BigDecimal.valueOf(2000)));

        중식_메뉴_짬뽕 = menuProductRepository.save(new MenuProduct(중식_메뉴, 짬뽕.getId(), 3));
        중식_메뉴_짜장 = menuProductRepository.save(new MenuProduct(중식_메뉴, 짜장.getId(), 1));

        중식_메뉴.addMenuProduct(Arrays.asList(중식_메뉴_짬뽕, 중식_메뉴_짜장));

        주문_테이블 = orderTableRepository.save(new OrderTable(1, false));
        빈주문_테이블 = orderTableRepository.save(new OrderTable(0, true));

        중식_주문_항목 = new OrderLineItem(중식_메뉴.getId(), 중식_메뉴.getName(), 중식_메뉴.getPrice().longValue(), 1L);

        주문1 = orderRepository.save(new Order(주문_테이블.getId(), 중식_주문_항목));
    }


    private List<OrderLineItemRequest> toOrderLineItemRequests(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream().
                map(o -> OrderLineItemRequest.of(o.getMenuId(), o.getMenuName().getValue(), o.getMenuPrice().getValue()
                        .longValue(), o.getQuantity().getValue()))
                .collect(Collectors.toList());
    }

    @Test
    @DisplayName("생성하려는 주문에서 주문 항목의 메뉴가 시스템에 등록 되어 있지 않으면 주문을 생성 할 수 없다.")
    void createFailWithMenuNotExistTest() {
        //given
        OrderLineItem orderLineItem = new OrderLineItem(100L, "썩은짜장", 100L, 100L);
        Order order = new Order(주문_테이블.getId(), orderLineItem);

        //when & then
        assertThatThrownBy(
                () -> orderService.create(
                        OrderRequest.of(order.getOrderTableId(), toOrderLineItemRequests(order.getOrderLineItems())))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("생성하려는 주문에서 주문 테이블이 시스템에 등록 되어 있지 않으면 주문을 생성 할 수 없다.")
    void createFailWithOrderNotExistTest() {
        //given
        OrderTable orderTable = new OrderTable(100L, 10, false);

        //when & then
        assertThatThrownBy(
                () -> orderService.create(
                        OrderRequest.of(100L, toOrderLineItemRequests(주문1.getOrderLineItems())))
        ).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("생성하려는 주문에서 주문 테이블이 빈주문 테이블이면 생성 할 수 없다.")
    void createFailWithEmptyOrderTableTest() {
        //given
        Order order = new Order(빈주문_테이블.getId(), 중식_주문_항목);

        //when & then
        assertThatThrownBy(
                () -> orderService.create(
                        OrderRequest.of(order.getOrderTableId(), toOrderLineItemRequests(order.getOrderLineItems())))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문을 생성 할 수 있다.")
    void createTest() {
        //when
        OrderResponse orderResponse = orderService.create(
                OrderRequest.of(주문1.getOrderTableId(), toOrderLineItemRequests(주문1.getOrderLineItems()))
        );

        //then
        assertThat(orderResponse).isNotNull();
        assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @Test
    @DisplayName("주문 목록을 조회 할 수 있다.")
    void listTest() {
        //when
        List<OrderResponse> orders = orderService.list();

        //then
        assertThat(orders).hasSize(1);
    }

    @Test
    @DisplayName("주문이 시스템에 등록 되어 있지 않으면 변경 할 수 없다.")
    void changeOrderStatusFailWithOrderNotExistTest() {
        //when & then
        assertThatThrownBy(
                () -> orderService.changeOrderStatus(1000L,
                        ChangeOrderStatusRequest.of(주문_테이블.getId(), OrderStatus.MEAL.name()))
        ).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("주문이 완료 상태이면 변경 할 수 없다.")
    void changeOrderStatusFailWithCompleteStatusTest() {
        //given
        orderService.changeOrderStatus(주문1.getId(),
                ChangeOrderStatusRequest.of(주문_테이블.getId(), OrderStatus.COMPLETION.name()));

        //when & then
        assertThatThrownBy(
                () -> orderService.changeOrderStatus(주문1.getId(),
                        ChangeOrderStatusRequest.of(주문_테이블.getId(), OrderStatus.MEAL.name()))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문의 상태를 변경 할 수 있다.")
    void changeOrderStatusTest() {
        //when
        OrderResponse orderResponse = orderService.changeOrderStatus(주문1.getId(),
                ChangeOrderStatusRequest.of(주문_테이블.getId(), OrderStatus.MEAL.name()));

        //then
        assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }
}
