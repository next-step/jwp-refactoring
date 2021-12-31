package kitchenpos.order;

import kitchenpos.AcceptanceTest;
import kitchenpos.global.exception.EntityNotFoundException;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.dto.MenuGroupCreateRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderStatusChangeRequest;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.dto.ProductCreateRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.table.application.TableService;
import kitchenpos.table.dto.TableCreateRequest;
import kitchenpos.table.dto.TableResponse;
import kitchenpos.table.exception.TableNotAvailableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 관련 기능")
class OrderServiceTest extends AcceptanceTest {

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

    @Test
    @DisplayName("주문한 메뉴가 존재하지 않으면 예외가 발생한다.")
    void createOrderFailBecauseOfIsNotExistMenu() {
        // given
        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(1L, Arrays.asList(new OrderCreateRequest.OrderLineItem(1L, 1L)));

        // when
        assertThatThrownBy(() -> {
            orderService.create(orderCreateRequest);
        }).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("테이블이 존재하지 않으면 예외가 발생한다.")
    void createOrderFailBecauseOfIsNotExistTable() {
        // given
        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(1L, Arrays.asList(new OrderCreateRequest.OrderLineItem(1L, 1L)));

        // when
        assertThatThrownBy(() -> {
            orderService.create(orderCreateRequest);
        }).isInstanceOf(EntityNotFoundException.class);
    }


    @Test
    @DisplayName("주문 상태 변경 시 주문이 존재하지 않으면 예외가 발생한다.")
    void changeOrderStatusFailBecauseOfIsNotExistOrder() {
        // when
        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(1L, new OrderStatusChangeRequest(OrderStatus.COMPLETION));
        }).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("테이블의 상태가 사용불가라면 예외가 발생한다.")
    void createOrderFailBecauseOfIsEmptyTableStatus() {
        // given
        final ProductResponse productResponse = productService.create(new ProductCreateRequest("후라이드", BigDecimal.valueOf(7000)));
        final MenuGroupResponse menuGroupResponse = menuGroupService.create(new MenuGroupCreateRequest("추천메뉴"));
        final MenuResponse menuResponse = menuService.create(new MenuCreateRequest("후라이드 2마리", BigDecimal.valueOf(13000), menuGroupResponse.getId(), Arrays.asList(new MenuCreateRequest.MenuProductRequest(productResponse.getId(), 2L))));
        final TableResponse tableResponse = tableService.create(new TableCreateRequest(0, true));
        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(tableResponse.getId(), Arrays.asList(new OrderCreateRequest.OrderLineItem(menuResponse.getId(), 1L)));

        // when
        assertThatThrownBy(() -> {
            orderService.create(orderCreateRequest);
        }).isInstanceOf(TableNotAvailableException.class);
    }
}
