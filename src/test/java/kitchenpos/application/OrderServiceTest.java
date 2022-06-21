package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupDao menuGroupDao;
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private OrderTableDao orderTableDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderService orderService;

    private Menu 후라이드_양념_세트;
    private Menu 후라이드_단품_세트;
    private OrderTable 테이블_A;

    @BeforeEach
    void setUp() {
        super.setUp();

        Product 후라이드 = this.productDao.save(new Product("후라이드", BigDecimal.valueOf(16000)));
        Product 양념치킨 = this.productDao.save(new Product("양념치킨", BigDecimal.valueOf(16000)));
        Product 콜라 = this.productDao.save(new Product("콜라", BigDecimal.valueOf(2500)));
        MenuProduct 후라이드_메뉴상품 = new MenuProduct(후라이드.getId(), 1);
        MenuProduct 양념치킨_메뉴상품 = new MenuProduct(양념치킨.getId(), 2);
        MenuProduct 콜라_메뉴상품 = new MenuProduct(콜라.getId(), 1);
        MenuGroup 두마리메뉴 = this.menuGroupDao.save(new MenuGroup("두마리메뉴"));
        MenuGroup 한마리메뉴 = this.menuGroupDao.save(new MenuGroup("한마리메뉴"));
        후라이드_양념_세트 = this.menuDao.save(
            new Menu("후라이드_양념_세트", BigDecimal.valueOf(22000),
                두마리메뉴.getId(), Arrays.asList(후라이드_메뉴상품, 양념치킨_메뉴상품))
        );
        후라이드_단품_세트 = this.menuDao.save(
            new Menu("후라이드_단품_세트", BigDecimal.valueOf(17500),
                한마리메뉴.getId(), Arrays.asList(후라이드_메뉴상품, 콜라_메뉴상품))
        );

        테이블_A = this.orderTableDao.save(new OrderTable(4, false));
    }

    @Test
    @DisplayName("주문한 메뉴가 0개일 경우 예외를 던진다.")
    void createFail_orderLineItemsEmpty() {
        Order order = new Order(테이블_A.getId(), Collections.emptyList());

        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.orderService.create(order));
    }

    @Test
    @DisplayName("주문한 메뉴가 실제 존재하지 않는 경우 예외를 던진다.")
    void createFail_orderLineItems() {
        OrderLineItem 후라이드_양념_세트_주문 = new OrderLineItem(후라이드_양념_세트.getId(), 1);
        OrderLineItem 없는_주문 = new OrderLineItem(1000L, 1);
        Order order = new Order(테이블_A.getId(), Arrays.asList(후라이드_양념_세트_주문, 없는_주문));

        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.orderService.create(order));
    }

    @Test
    @DisplayName("주문한 테이블 정보가 존재하지 않을 경우 예외를 던진다.")
    void createFail_orderTableEmpty() {
        OrderLineItem 후라이드_양념_세트_주문 = new OrderLineItem(후라이드_양념_세트.getId(), 1);
        OrderLineItem 후라이드_단품_세트_주문 = new OrderLineItem(후라이드_단품_세트.getId(), 1);
        Order order = new Order(1000L, Arrays.asList(후라이드_양념_세트_주문, 후라이드_단품_세트_주문));

        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.orderService.create(order));
    }

    @Test
    @DisplayName("주문시 주문정보가 생성된디.")
    void create() {
        OrderLineItem 후라이드_양념_세트_주문 = new OrderLineItem(후라이드_양념_세트.getId(), 1);
        OrderLineItem 후라이드_단품_세트_주문 = new OrderLineItem(후라이드_단품_세트.getId(), 1);
        Order order = new Order(테이블_A.getId(), Arrays.asList(후라이드_양념_세트_주문, 후라이드_단품_세트_주문));

        order = this.orderService.create(order);

        assertThat(order.getId()).isNotNull();
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(order.getOrderedTime()).isNotNull();
        assertThat(order.getOrderLineItems()).hasSize(2);
    }

    @Test
    @DisplayName("주문을 모두 조회한다.")
    void list() {
        OrderLineItem 후라이드_양념_세트_주문 = new OrderLineItem(후라이드_양념_세트.getId(), 1);
        OrderLineItem 후라이드_단품_세트_주문 = new OrderLineItem(후라이드_단품_세트.getId(), 1);
        Order order1 = new Order(테이블_A.getId(), Arrays.asList(후라이드_양념_세트_주문, 후라이드_단품_세트_주문));
        Order order2 = new Order(테이블_A.getId(), Collections.singletonList(후라이드_양념_세트_주문));
        order1 = this.orderService.create(order1);
        order2 = this.orderService.create(order2);

        List<Order> list = this.orderService.list();

        assertThat(list).containsExactly(order1, order2);
    }

    @Test
    @DisplayName("주문 상태가 완료된 건 변경할 수 없다.")
    void changeOrderStatusFail() {
        OrderLineItem 후라이드_양념_세트_주문 = new OrderLineItem(후라이드_양념_세트.getId(), 1);
        OrderLineItem 후라이드_단품_세트_주문 = new OrderLineItem(후라이드_단품_세트.getId(), 1);
        Order order = new Order(테이블_A.getId(), Arrays.asList(후라이드_양념_세트_주문, 후라이드_단품_세트_주문));
        Order createdOrder = this.orderService.create(order);

        createdOrder.setOrderStatus(OrderStatus.COMPLETION.name());
        this.orderDao.save(createdOrder);

        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.orderService.changeOrderStatus(createdOrder.getId(), null));
    }

    @Test
    @DisplayName("주문 상태를 변경한다.")
    void changeOrderStatus() {
        OrderLineItem 후라이드_양념_세트_주문 = new OrderLineItem(후라이드_양념_세트.getId(), 1);
        OrderLineItem 후라이드_단품_세트_주문 = new OrderLineItem(후라이드_단품_세트.getId(), 1);
        Order order = new Order(테이블_A.getId(), Arrays.asList(후라이드_양념_세트_주문, 후라이드_단품_세트_주문));
        Order createdOrder = this.orderService.create(order);

        Order statusOrder = new Order();
        statusOrder.setOrderStatus(OrderStatus.MEAL.name());
        this.orderService.changeOrderStatus(createdOrder.getId(), statusOrder);
        Order expectedOrder = this.orderDao.findById(createdOrder.getId()).orElse(null);

        assertThat(expectedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

}
