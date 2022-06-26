package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import kitchenpos.ServiceTest;
import kitchenpos.menu.application.MenuTestFixture;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.util.dto.SaveMenuDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;

class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MenuTestFixture menuTestFixture;

    private OrderLineItem orderLineItem;
    private OrderTable orderTable;


    @BeforeEach
    public void setUp() {
        super.setUp();
        Product 후라이드 = this.productRepository.save(new Product("후라이드", BigDecimal.valueOf(16000)));
        Product 양념치킨 = this.productRepository.save(new Product("양념치킨", BigDecimal.valueOf(16000)));
        MenuProduct menuProduct1 = new MenuProduct(후라이드.getId(), 1);
        MenuProduct menuProduct2 = new MenuProduct(양념치킨.getId(), 1);
        List<MenuProduct> menuProducts = Arrays.asList(menuProduct1, menuProduct2);

        Menu menu = menuTestFixture.메뉴_만들기(new SaveMenuDto(menuProducts, new MenuGroup("메뉴 그룹"), "메뉴", 32000));

        orderLineItem = new OrderLineItem(OrderMenu.of(menu), 1);
        orderTable = this.orderTableRepository.save(new OrderTable(4, false));
    }

    @Test
    @DisplayName("주문시 주문정보가 생성된디.")
    void create() {
        OrderResponse orderResponse = createOrder(orderTable.getId(), orderLineItem);

        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(orderResponse.getOrderedTime()).isNotNull();
        assertThat(orderResponse.getOrderLineItems()).hasSize(1);
    }

    @TestFactory
    @DisplayName("주문 생성시 예외처리를 검증한다.")
    Stream<DynamicTest> createFail() {
        return Stream.of(
            DynamicTest.dynamicTest("주문한 메뉴가 0개일 경우", () -> {
                assertThatIllegalArgumentException()
                    .isThrownBy(() -> createOrder(orderTable.getId()));
            }),
            DynamicTest.dynamicTest("주문한 메뉴가 실제 존재하지 않는 경우", () -> {
                OrderMenu orderMenu = new OrderMenu(Long.MAX_VALUE, "test", BigDecimal.TEN);
                OrderLineItem orderLineItem = new OrderLineItem(orderMenu, 1);

                assertThatIllegalArgumentException()
                    .isThrownBy(() -> {createOrder(orderTable.getId(), orderLineItem);});
            }),
            DynamicTest.dynamicTest("테이블 정보가 없는 경우", () -> {
                assertThatExceptionOfType(InvalidDataAccessApiUsageException.class)
                    .isThrownBy(() -> createOrder(null, orderLineItem));
            })
        );
    }

    @Test
    @DisplayName("주문을 모두 조회한다.")
    void list() {
        OrderResponse 주문 = createOrder(orderTable.getId(), orderLineItem);
        OrderResponse 주문_추가 = createOrder(orderTable.getId(), orderLineItem);

        List<OrderResponse> list = this.orderService.list();

        assertThat(list).containsAll(Arrays.asList(주문, 주문_추가));
    }

    @Test
    @DisplayName("주문 상태를 변경한다.")
    void changeOrderStatus() {
        OrderResponse order = createOrder(orderTable.getId(), orderLineItem);

        OrderResponse response = this.orderService.changeOrderStatus(order.getId(), new OrderRequest(OrderStatus.MEAL));

        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }


    @Test
    @DisplayName("주문 상태가 완료된 건 변경할 수 없다.")
    void changeOrderStatusFail() {
        OrderResponse order = createOrder(orderTable.getId(), orderLineItem);
        this.orderService.changeOrderStatus(order.getId(), new OrderRequest(OrderStatus.COMPLETION));

        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.orderService.changeOrderStatus(order.getId(), new OrderRequest(OrderStatus.COOKING)));
    }

    private OrderResponse createOrder(Long tableId, OrderLineItem... orderLineItem) {
        return this.orderService.create(new OrderRequest(tableId, OrderLineItemRequest.of(Arrays.asList(orderLineItem))));
    }

}
