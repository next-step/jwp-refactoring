package kitchenpos.util;

import kitchenpos.menu.domain.*;
import kitchenpos.order.domain.*;
import kitchenpos.order.domain.service.OrderValidator;
import kitchenpos.order.domain.service.TableGroupDomainService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

public class TestSupport {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private TableGroupRepository tableGroupRepository;

    public Product 상품_등록되어있음(String name, BigDecimal price) {
        return productRepository.save(new Product(name, price));
    }

    public Menu 메뉴_등록되어있음(String name, BigDecimal price) {
        return menuRepository.save(new Menu(name, price));
    }

    public MenuGroup 메뉴그룹_등록되어있음(String name) {
        return menuGroupRepository.save(new MenuGroup(name));
    }

    public OrderTable 테이블_등록되어있음(int numberOfGuests, boolean empty) {
        return orderTableRepository.save(new OrderTable(numberOfGuests, empty));
    }

    public Order 주문_등록되어있음(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        return orderRepository.save(new Order(orderTable, orderLineItems, new OrderValidator(menuRepository)));
    }

    public TableGroup 단체지정_등록되어있음(List<OrderTable> orderTables) {
        TableGroupDomainService tableGroupDomainService = new TableGroupDomainService();
        return tableGroupRepository.save(tableGroupDomainService.group(orderTables));
    }
}
