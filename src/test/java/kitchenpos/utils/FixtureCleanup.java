package kitchenpos.utils;

import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menuGroup.MenuGroupRepository;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.orderTable.OrderTableRepository;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.domain.tableGroup.TableGroupRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FixtureCleanup {
    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @BeforeEach
    void cleanupFixtures() {
        tableGroupRepository.deleteAll();
        orderTableRepository.deleteAll();
        orderRepository.deleteAll();
        productRepository.deleteAll();
        menuRepository.deleteAll();
        menuGroupRepository.deleteAll();
    }
}
