package kitchenpos.utils;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import kitchenpos.fixture.MenuFixtureFactory;
import kitchenpos.fixture.MenuGroupFixtureFactory;
import kitchenpos.fixture.OrderFixtureFactory;
import kitchenpos.fixture.OrderTableFixtureFactory;
import kitchenpos.fixture.ProductFixtureFactory;
import kitchenpos.fixture.TableGroupFixtureFactory;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuDto;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemDto;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.application.TableService;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceTestHelper {
    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private ProductService productService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderService orderService;

    public MenuGroup 메뉴그룹_생성됨(String name) {
        return menuGroupService.create(MenuGroupFixtureFactory.createMenuGroup(name));
    }

    public Product 상품_생성됨(String name, int price) {
        return productService.create(ProductFixtureFactory.createProduct(name, price));
    }

    public MenuDto 메뉴_생성됨(MenuGroup menuGroup, String menuName, int menuPrice, List<MenuProduct> menuProducts) {
        Menu menuFixture = MenuFixtureFactory.createMenu(menuGroup, menuName, menuPrice, menuProducts);
        return menuService.create(MenuDto.of(menuFixture));
    }

    public TableGroupResponse 테이블그룹_지정됨(int numberOfTables) {
        List<OrderTableResponse> orderTables = IntStream.range(0, numberOfTables)
                .mapToObj((index) -> 빈테이블_생성됨()).collect(toList());
        return tableGroupService.create(TableGroupFixtureFactory.createTableGroup(orderTables));
    }

    public TableGroupResponse 테이블그룹_지정됨(OrderTableResponse... orderTables) {
        return tableGroupService.create(TableGroupFixtureFactory.createTableGroup(Arrays.asList(orderTables)));
    }

    public OrderTableResponse 빈테이블_생성됨() {
        return tableService.create(OrderTableFixtureFactory.createEmptyOrderTable());
    }

    public OrderTableResponse 비어있지않은테이블로_변경(Long orderTableId) {
        return tableService.changeEmpty(orderTableId, OrderTableFixtureFactory.createParamForChangeEmptyState(false));
    }

    public OrderTableResponse 비어있지않은테이블_생성됨(int numberOfGuests) {
        return tableService.create(OrderTableFixtureFactory.createNotEmptyOrderTable(numberOfGuests));
    }

    public OrderTableResponse 빈테이블로_변경(Long orderTableId) {
        return tableService.changeEmpty(orderTableId, OrderTableFixtureFactory.createParamForChangeEmptyState(true));
    }

    public OrderTableResponse 테이블_인원수_변경(Long orderTableId, int updatedNumberOfGuests) {
        return tableService.changeNumberOfGuests(orderTableId,
                OrderTableFixtureFactory.createParamForChangeNumberOfGuests(updatedNumberOfGuests));
    }

    public OrderResponse 주문_생성됨(Long orderTableId, List<OrderLineItemDto> orderLineItemDtos) {
        return orderService.create(OrderFixtureFactory.createOrder(orderTableId, orderLineItemDtos));
    }

    public OrderResponse 주문상태_변경(Long orderId, OrderStatus orderStatus) {
        OrderRequest param = OrderFixtureFactory.createParamForUpdateStatus(orderStatus);
        return orderService.changeOrderStatus(orderId, param);
    }
}
