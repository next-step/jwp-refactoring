package kitchenpos.menu.application;

import kitchenpos.exception.CannotFindException;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static kitchenpos.common.Message.ERROR_MENUGROUP_NOT_FOUND;
import static kitchenpos.common.Message.ERROR_PRODUCT_NOT_FOUND;

@Service
@Transactional
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

    public MenuResponse create(final MenuRequest menuRequest) {

        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(() -> new CannotFindException(ERROR_MENUGROUP_NOT_FOUND));
        List<MenuProduct> menuProducts = getMenuProducts(menuRequest);
        Menu savedMenu = menuRepository.save(new Menu(menuRequest.getName(), Price.valueOf(menuRequest.getPrice()), menuGroup, menuProducts));
        return MenuResponse.of(savedMenu);
    }

    private List<MenuProduct> getMenuProducts(MenuRequest menuRequest) {
        List<Product> products = getProducts(menuRequest);
        return getMenuProductsWithProducts(menuRequest.getMenuProductRequests(), products);
    }

    private List<MenuProduct> getMenuProductsWithProducts(List<MenuProductRequest> menuProductRequests, List<Product> products) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : menuProductRequests) {
            Product product = findProductById(menuProductRequest.getProductId(), products);
            menuProducts.add(new MenuProduct(product, Quantity.of(menuProductRequest.getQuantity())));
        }
        return menuProducts;
    }

    private Product findProductById(Long productId, List<Product> products) {
        return products.stream()
                .filter(product -> product.hasSameIdAs(productId))
                .findFirst()
                .orElseThrow(() -> new CannotFindException(ERROR_PRODUCT_NOT_FOUND));
    }

    private List<Product> getProducts(MenuRequest menuRequest) {
        List<Long> productIds = menuRequest.getAllProductIds();
        List<Product> products = productRepository.findAllById(productIds);

        if (productIds.size() != products.size()) {
            throw new CannotFindException(ERROR_PRODUCT_NOT_FOUND);
        }

        return products;
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return MenuResponse.ofList(menus);
    }
}
