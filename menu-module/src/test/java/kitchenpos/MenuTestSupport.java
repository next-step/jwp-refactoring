package kitchenpos;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;

import java.math.BigDecimal;

public class MenuTestSupport {
    @Autowired
    protected ProductRepository productRepository;
    @Autowired
    protected MenuRepository menuRepository;
    @Autowired
    protected MenuGroupRepository menuGroupRepository;

    public Product 상품_등록되어있음(String name, BigDecimal price) {
        return productRepository.save(new Product(name, price));
    }

    public Menu 메뉴_등록되어있음(String name, BigDecimal price) {
        return menuRepository.save(new Menu(name, price));
    }

    public MenuGroup 메뉴그룹_등록되어있음(String name) {
        return menuGroupRepository.save(new MenuGroup(name));
    }
}
