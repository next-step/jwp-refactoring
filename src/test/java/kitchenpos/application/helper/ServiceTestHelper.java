package kitchenpos.application.helper;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import kitchenpos.application.OrderService;
import kitchenpos.order.application.TableGroupService;
import kitchenpos.order.application.TableService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
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
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
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

    public TableGroup 테이블그룹_지정됨(int numberOfTables) {
        List<OrderTable> orderTables = IntStream.range(0, numberOfTables)
                .mapToObj((index) -> 빈테이블_생성됨()).collect(toList());
        return tableGroupService.create(TableGroupFixtureFactory.createTableGroup(orderTables));
    }

    public TableGroup 테이블그룹_지정됨(OrderTable... orderTables) {
        return tableGroupService.create(TableGroupFixtureFactory.createTableGroup(Arrays.asList(orderTables)));
    }

    public OrderTable 빈테이블_생성됨() {
        return tableService.create(OrderTableFixtureFactory.createEmptyOrderTable());
    }

    public OrderTable 비어있지않은테이블로_변경(Long orderTableId) {
        OrderTable param = OrderTableFixtureFactory.createParamForChangeEmptyState(false);
        return tableService.changeEmpty(orderTableId, param);
    }

    public OrderTable 비어있지않은테이블_생성됨(int numberOfGuests) {
        return tableService.create(OrderTableFixtureFactory.createNotEmptyOrderTable(numberOfGuests));
    }

    public OrderTable 빈테이블로_변경(Long orderTableId) {
        OrderTable param = OrderTableFixtureFactory.createParamForChangeEmptyState(true);
        return tableService.changeEmpty(orderTableId, param);
    }

    public OrderTable 테이블_인원수_변경(Long orderTableId, int updatedNumberOfGuests) {
        OrderTable param = OrderTableFixtureFactory.createParamForChangeNumberOfGuests(updatedNumberOfGuests);
        return tableService.changeNumberOfGuests(orderTableId, param);
    }

    public Order 주문_생성됨(Long orderTableId, List<OrderLineItem> orderLineItems) {
        return orderService.create(OrderFixtureFactory.createOrder(orderTableId, orderLineItems));
    }

    public Order 주문상태_변경(Long orderId, OrderStatus orderStatus) {
        Order param = OrderFixtureFactory.createParamForUpdateStatus(orderStatus);
        return orderService.changeOrderStatus(orderId, param);
    }


}
