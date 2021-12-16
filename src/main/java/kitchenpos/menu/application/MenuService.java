package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuService {

    private static final int MIN_PRICE = 0;

    private final MenuRepository menuRepository;
    private final MenuGroupService menuGroupService;
    private final ProductService productService;

    public MenuService(final MenuRepository menuRepository,
                       final MenuGroupService menuGroupService,
                       final ProductService productService) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
    }

    public MenuResponse create(final MenuRequest menuRequest) {
        existsMenuGroupById(menuRequest.getMenuGroupId());
        final MenuProducts menuProducts = new MenuProducts(makeMenuProducts(menuRequest));
        final Menu savedMenu = menuRepository.save(menuRequest.toMenu(menuProducts));
        return MenuResponse.from(savedMenu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Menu findById(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    private void existsMenuGroupById(Long menuGroupId) {
        menuGroupService.findById(menuGroupId);
    }

    private List<MenuProduct> makeMenuProducts(MenuRequest menuRequest) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : menuRequest.getMenuProducts()) {
            final Product product = getProductById(menuProductRequest.getProductId());
            menuProducts.add(new MenuProduct(product, menuProductRequest.getQuantity()));
        }
        return menuProducts;
    }

    private Product getProductById(Long productId) {
        return productService.findById(productId);
    }
}
