package kitchenpos.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class OrderIntegrationTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    private OrderTable 주문_테이블;
    private Menu 메뉴1;
    private Menu 메뉴2;

    private Order 주문1;
    private Order 주문2;

    @BeforeEach
    void setUp() {
        주문_테이블 = orderTableRepository.save(new OrderTable(3, false));

        MenuGroup 메뉴그룹 = menuGroupRepository.save(new MenuGroup("테스트메뉴그룹"));
        Product 제품1 = productRepository.save(new Product("테스트제품1", BigDecimal.valueOf(1000L)));
        Product 제품2 = productRepository.save(new Product("테스트제품2", BigDecimal.valueOf(3000L)));

        메뉴1 = menuRepository.save(Menu.Builder.of("테스트메뉴1", BigDecimal.valueOf(3000L))
                                              .menuGroup(메뉴그룹)
                                              .menuProducts(Arrays.asList(new MenuProduct(제품1, 2),
                                                                          new MenuProduct(제품2, 4)))
                                              .build());

        메뉴2 = menuRepository.save(Menu.Builder.of("테스트메뉴2", BigDecimal.valueOf(5000L))
                                              .menuGroup(메뉴그룹)
                                              .menuProducts(Arrays.asList(new MenuProduct(제품1, 2),
                                                                          new MenuProduct(제품2, 4)))
                                              .build());

        주문1 = orderRepository.save(new Order(주문_테이블,
                                             OrderStatus.COOKING,
                                             Arrays.asList(new OrderLineItem(메뉴1, 3),
                                                           new OrderLineItem(메뉴2, 4))));

        주문2 = orderRepository.save(new Order(주문_테이블,
                                             OrderStatus.COOKING,
                                             Arrays.asList(new OrderLineItem(메뉴1, 3),
                                                           new OrderLineItem(메뉴2, 4))));
    }


    @DisplayName("주문 생성 통합 테스트")
    @Test
    void createTest() {
        // given
        OrderRequest orderRequest = new OrderRequest(주문_테이블.getId(),
                                                     Arrays.asList(
                                                         new OrderLineItemRequest(메뉴1.getId(), 3),
                                                         new OrderLineItemRequest(메뉴2.getId(), 5)));

        // when
        OrderResponse actual = orderService.create(orderRequest);

        // then
        assertAll(() -> {
            assertThat(actual).isNotNull();
            assertThat(actual.getOrderTableId()).isEqualTo(주문_테이블.getId());
            assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
            assertThat(actual.getOrderLineItems()
                             .stream()
                             .map(OrderLineItemResponse::getMenuId)
                             .collect(Collectors.toList()))
                .hasSize(2)
                .containsExactly(메뉴1.getId(), 메뉴2.getId());
        });
    }

    @DisplayName("전체 주문 조회 통합 테스트")
    @Test
    void listTest() {
        // when
        assertThat(orderService.list()).isNotEmpty().hasSizeGreaterThanOrEqualTo(2);
    }

    @DisplayName("주문 상태 변경 통합 테스트")
    @Test
    void changeOrderStatusTest() {
        assertThat(orderService.changeOrderStatus(주문1.getId(), new OrderStatusRequest(OrderStatus.MEAL)))
            .isNotNull()
            .extracting(OrderResponse::getOrderStatus)
            .isEqualTo(OrderStatus.MEAL.name());
    }
}
