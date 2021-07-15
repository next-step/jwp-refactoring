package kitchenpos.application.order;

import kitchenpos.application.menu.MenuService;
import kitchenpos.application.menugroup.MenuGroupService;
import kitchenpos.application.order.OrderService;
import kitchenpos.application.product.ProductService;
import kitchenpos.application.ordertable.OrderTable;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.dto.menugroup.MenuGroupRequest;
import kitchenpos.dto.menugroup.MenuGroupResponse;
import kitchenpos.dto.order.OrderLineItemResponse;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.dto.product.ProductRequest;
import kitchenpos.dto.product.ProductResponse;
import kitchenpos.repository.menugroup.MenuGroupRepository;
import kitchenpos.repository.ordertable.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 기능 테스트")
@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired
    OrderTableRepository orderTableRepository;

    @Autowired
    OrderService orderService;

    @Autowired
    MenuService menuService;

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @Autowired
    ProductService productService;

    @Autowired
    MenuGroupService menuGroupService;

    private OrderTable savedOrderTable;

    private OrderLineItemResponse request1;
    private OrderLineItemResponse request2;

    private OrderRequest orderRequest;
    private OrderResponse orderResponse;

    @BeforeEach
    void setUp() {
        // given
        OrderTable orderTable = new OrderTable(2, false);
        savedOrderTable = orderTableRepository.save(orderTable);

        request1 = createRequest("삼선짜장", 5000, 2L);
        request2 = createRequest("삼선짬뽕", 6000, 2L);

        orderRequest = new OrderRequest(savedOrderTable.getId(), Arrays.asList(request1, request2));

        orderResponse = orderService.create(orderRequest);
    }

    private OrderLineItemResponse createRequest(String menuName, int price, Long quantity) {
        MenuGroupResponse menuGroup = menuGroupService.create(new MenuGroupRequest("음식"));
        ProductResponse product = productService.create(new ProductRequest(menuName, new BigDecimal(price)));
        MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 1);
        MenuResponse menu = menuService.create(new MenuRequest("짜장면", new BigDecimal(price), menuGroup.getId(),
                Collections.singletonList(menuProductRequest)));
        return new OrderLineItemResponse(menu.getId(), quantity);
    }

    @Test
    @DisplayName("주문을 등록할 수 있다.")
    public void createTest() throws Exception {
        // then
        assertThat(orderResponse).isNotNull();
    }

    @Test
    @DisplayName("주문한 목록을 조회 할 수 있다.")
    public void findAllTest() throws Exception {
        // when
        List<OrderResponse> orders = orderService.findAll();

        // then
        assertThat(orders).isNotEmpty();
    }
}