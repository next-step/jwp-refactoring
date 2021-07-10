package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menuproduct.domain.MenuProduct;
import kitchenpos.menuproduct.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menuproduct.domain.MenuProducts;
import kitchenpos.menuproduct.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.ui.exception.NoMenuGroupException;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;
    private final MenuProductRepository menuProductRepository;

    public MenuService(
            MenuRepository menuRepository,
            MenuGroupRepository menuGroupRepository, ProductRepository productRepository, MenuProductRepository menuProductRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
        this.menuProductRepository = menuProductRepository;
    }

    public MenuResponse create(MenuRequest menuRequest) {
        Menu menu = Menu.of(menuRequest);

        if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new NoMenuGroupException();
        }

        final Menu savedMenu = menuRepository.save(menu);

        List<MenuProduct> menuProductList = new ArrayList<>();

        for (MenuProductRequest menuProductRequest : menuRequest.getMenuProducts()) {
            productRepository.findById(menuProductRequest.getProductId()).ifPresent(
                    product -> menuProductList.add(
                            new MenuProduct(savedMenu, product, menuProductRequest.getQuantity())
                    )
            );
        }

        MenuProducts menuProducts = new MenuProducts(menuProductList);

        MenuValidator.validatePrice(menuProducts, savedMenu);

        return MenuResponse.of(savedMenu);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream().map(MenuResponse::of).collect(Collectors.toList());
    }
}
