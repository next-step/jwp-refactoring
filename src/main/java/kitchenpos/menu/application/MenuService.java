package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menugroup.MenuGroupRepository;
import kitchenpos.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuProductRepository menuProductRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(final MenuCreateRequest menuCreateRequest) {
        menuCreateRequest.checkPriceValidation();
        checkGroupExist(menuCreateRequest);
        final Menu menu = menuRepository.save(menuCreateRequest.toMenu(menuGroupRepository.getOne(menuCreateRequest.getMenuGroupId())));
        List<MenuProduct> products = menuCreateRequest.getMenuProducts()
                .stream()
                .map(it -> menuProductRepository.save(new MenuProduct(menu, productRepository.getOne(it.getProductId()), it.getQuantity())))
                .collect(Collectors.toList());
        menu.addMenuProducts(products);
        return menu;
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }

    private void checkGroupExist(MenuCreateRequest menuCreateRequest) {
        if (!menuGroupRepository.existsById(menuCreateRequest.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
    }
}
