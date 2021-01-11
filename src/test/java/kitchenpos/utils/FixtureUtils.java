package kitchenpos.utils;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menuGroup.MenuGroup;
import kitchenpos.domain.menuGroup.MenuGroupRepository;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.orderTable.OrderTableRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.domain.tableGroup.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Collections;

@SpringBootTest
public class FixtureUtils {
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

    protected Long createMenuFixture() {
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("놀라운 메뉴 그룹"));
        Product product = productRepository.save(new Product("놀라운 상품", BigDecimal.ONE));
        Menu menu = menuRepository.save(Menu.of("놀라운 메뉴", BigDecimal.ZERO, menuGroup.getId(),
                Collections.singletonList(MenuProduct.of(product.getId(), 1L))));

        return menu.getId();
    }
}
