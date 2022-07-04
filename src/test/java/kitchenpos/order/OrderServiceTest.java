package kitchenpos.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.transaction.Transactional;
import kitchenpos.order.application.OrderService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.product.domain.Product;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.repository.OrderLineItemRepository;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.product.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderServiceTest {
    @Autowired
    MenuProductRepository menuProductRepository;
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    OrderTableRepository orderTableRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    MenuGroupRepository menuGroupRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    OrderLineItemRepository orderLineItemRepository;

    @Autowired
    OrderService orderService;

    List<OrderLineItem> orderLineItems;
    Order order;

    @BeforeEach
    void setUp() {
        orderLineItems = new ArrayList<>();
        order = new Order();
        order.setId(1L);
    }

    @AfterEach
    void tearDown() {
        menuProductRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        orderLineItemRepository.deleteAllInBatch();
        menuRepository.deleteAllInBatch();
        menuGroupRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("빈 주문 내역 에러 반환")
    public void emptyOrderLinesCreate() {
        order.setOrderLineItems(orderLineItems);

        assertThatThrownBy(() -> orderService.create(OrderRequest.of(order))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 테이블에서 주문 시 에러 반환")
    public void orderTableNotExists()
    {
        order.setOrderTable(new OrderTable());
        assertThatThrownBy(() -> orderService.create(OrderRequest.of(order))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 테이블에서 주문 시 에러 반환")
    public void orderEmptyTable() {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setEmpty(true);

        order.setOrderTable(orderTable);
        assertThatThrownBy(() -> orderService.create(OrderRequest.of(order))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 정상 저장 후 COOKING 초기 상태 확인")
    public void orderSuccessSave() {
        Order savedOrder = 주문_생성_저장();
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    @Test
    @DisplayName("전체 주문 내역 보기")
    public void listOrders() {
        Order savedOrder = 주문_생성_저장();
        assertThat(orderService.list().get(0).getId()).isEqualTo(savedOrder.getId());
    }

    @Test
    @Transactional
    @DisplayName("전체 주문 내역 조회시 주문 항목 확인 가능")
    public void listOrdersShowOrderLineItems(){
        Order savedOrder = 주문_생성_저장();
        OrderLineItem orderLineItem = savedOrder.getOrderLineItems().getOrderLineItems().get(0);

        Order findOrder = orderService.list().stream()
                .filter(it -> it.getId().equals(savedOrder.getId()))
                .findFirst()
                .get();

        OrderLineItem findOrderLineItem = findOrder.getOrderLineItems().getOrderLineItems().get(0);

        assertThat(findOrderLineItem).isEqualTo(orderLineItem);
    }

    @Test
    @DisplayName("존재하지 않는 주문 상태 변경 시도 시 에러 반환")
    public void changeStatusNotExitsOrder() {
        order.setOrderStatus(OrderStatus.MEAL);

        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, order)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @Transactional
    @DisplayName("완료단계 주문 건 상태 변경 시도시 에러 반환")
    public void changeStatusInCompletion() {
        Order savedOrder = 주문_생성_저장();
        savedOrder.setOrderStatus(OrderStatus.COMPLETION);
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), new Order())).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 상태 변경 정상 처리")
    public void changeStatusSuccess() {
        Order savedOrder = 주문_생성_저장();
        savedOrder.setOrderStatus(OrderStatus.COOKING);
        Order changeOrder = new Order();
        changeOrder.setOrderStatus(OrderStatus.MEAL);

        assertThat(orderService.changeOrderStatus(savedOrder.getId(), changeOrder).getOrderStatus()).isEqualTo(
                changeOrder.getOrderStatus());
    }

    private Order 주문_생성_저장() {
        Menu 스낵랩_상품 = 메뉴생성();
        OrderLineItems orderLineItems = 주문항목_리스트_생성(스낵랩_상품);
        OrderTable orderTable = 주문테이블_생성();

        Order savedOrder = 주문저장(orderLineItems, orderTable);
        return savedOrder;
    }

    private Menu 메뉴생성() {
        MenuGroup 패스트푸드류 = menuGroupRepository.save(new MenuGroup("패스트푸드"));
        Product 스낵랩 = productRepository.save(new Product("스낵랩", BigDecimal.valueOf(3000)));

        MenuProduct 스낵랩_메뉴_상품 = new MenuProduct();
        스낵랩_메뉴_상품.setQuantity(1);
        스낵랩_메뉴_상품.setProduct(스낵랩);

        Menu 스낵랩_상품 = menuRepository.save(new Menu("스낵랩 상품", BigDecimal.valueOf(3000), 패스트푸드류,
                new MenuProducts(Arrays.asList(스낵랩_메뉴_상품))));
        return 스낵랩_상품;
    }

    private OrderLineItems 주문항목_리스트_생성(Menu menu) {
        OrderLineItem orderLineItem = new OrderLineItem(1L, null, menu, 1);
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.add(orderLineItem);
        return orderLineItems;
    }

    private OrderTable 주문테이블_생성() {
        return orderTableRepository.save(new OrderTable(null, 3, false));
    }

    private Order 주문저장(OrderLineItems orderLineItems, OrderTable orderTable) {
        Order order = new Order();
        order.setOrderLineItems(orderLineItems);
        order.setOrderTable(orderTable);
        return orderService.create(OrderRequest.of(order));
    }
}