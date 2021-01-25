package kitchenpos.service;

import kitchenpos.domain.Price;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.menu.MenuGroupRequest;
import kitchenpos.dto.menu.MenuGroupResponse;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.dto.order.OrderLineMenuRequest;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.dto.product.ProductResponse;
import kitchenpos.dto.table.TableRequest;
import kitchenpos.dto.table.TableResponse;
import kitchenpos.service.menu.MenuGroupService;
import kitchenpos.service.menu.MenuService;
import kitchenpos.service.order.OrderService;
import kitchenpos.service.product.ProductService;
import kitchenpos.service.table.TableService;

import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("주문 관련 테스트")
@Transactional
@SpringBootTest
public class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private TableService tableService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private ProductService productService;

    @PersistenceContext
    private EntityManager em;

    @DisplayName("주문 생성 테스트")
    @Test
    void create() {
        List<OrderResponse> orders = getSavedTable().getOrderResponses();

        assertThat(orders).extracting("id").isNotNull();
    }

    @DisplayName("주문 조회 테스트")
    @Test
    void findAll() {
        getSavedTable();

        List<OrderResponse> orders = orderService.findAll();

        for (OrderResponse order : orders) {
            assertThat(order.getOrderLineMenuResponses()).extracting("menuResponse")
                    .extracting("name")
                    .containsExactly("두마리 치킨", "김밥이 라면");
        }
    }

    @DisplayName("주문 상태 변경 테스트")
    @Test
    void change() {
        List<OrderResponse> orders = getSavedTable().getOrderResponses();
        Long orderId = orders.stream().map(OrderResponse::getId).findFirst().get();
        OrderResponse order = orderService.changeOrderStatus(orderId, new OrderRequest("COMPLETION"));

        em.flush();
        assertThat(order.getOrderStatus()).isEqualTo("COMPLETION");
    }

    private TableResponse getSavedTable() {
        TableResponse savedTableResponse = tableService.save(new TableRequest(0, true));
        tableService.changeEmpty(savedTableResponse.getId(), new TableRequest(false));
        OrderResponse ordered = doOrder(savedTableResponse);
        em.flush();
        em.clear();
        return tableService.changeGuestNumber(savedTableResponse.getId(), new TableRequest(4));
    }

    private OrderResponse doOrder(TableResponse tableResponse) {
        MenuResponse menu1 = createMenu("후라이드 치킨", "양념 치킨", "두마리 치킨");
        MenuResponse menu2 = createMenu("라면", "김밥", "김밥이 라면");
        return orderService.save(new OrderRequest(tableResponse.getId(),
                asList(new OrderLineMenuRequest(menu1.getId(), 2), new OrderLineMenuRequest(menu2.getId(), 1))));
    }

    private MenuResponse createMenu(String productName1, String productName2, String menuName) {
        MenuGroupResponse menuGroup = menuGroupService.save(new MenuGroupRequest("인기 메뉴"));
        return create(menuGroup, productName1, productName2, menuName);
    }

    private MenuResponse create(MenuGroupResponse menuGroup, String productName1, String productName2, String menuName) {
        ProductResponse product1 = productService.save(new Product(productName1, new Price(18000)));
        ProductResponse product2 = productService.save(new Product(productName2, new Price(20000)));
        MenuRequest menuRequest = new MenuRequest(menuName, 20000, menuGroup.getId(),
                asList(new MenuProductRequest(product1.getId(), 1), new MenuProductRequest(product2.getId(), 1)));
        return menuService.save(menuRequest);
    }
}
