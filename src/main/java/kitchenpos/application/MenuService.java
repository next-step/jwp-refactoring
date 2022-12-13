package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuValidator menuValidator;
    private final ProductRepository productRepository;

    public MenuService(
        final MenuRepository menuRepository,
        final MenuGroupRepository menuGroupRepository,
        final MenuValidator menuValidator,
        final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuValidator = menuValidator;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(final Menu menu) {
        final MenuProducts menuProducts = new MenuProducts(menu.getMenuProducts());
        List<Product> products = productRepository.findAllById(menuProducts.toProductIds());
        boolean menuGroupNotExists = !menuGroupRepository.existsById(menu.getMenuGroupId());

        menu.validate(menuValidator, products, menuGroupNotExists);

        return menuRepository.save(menu);
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
