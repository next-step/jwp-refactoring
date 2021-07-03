package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuCreate;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.product.Products;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(final MenuCreate create) {
        MenuGroup menuGroup = menuGroupRepository.findById(create.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);
        Products products = new Products(productRepository.findAllById(create.getProductsIdInMenuProducts()));

        return menuRepository.save(Menu.create(create, menuGroup, products));
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
