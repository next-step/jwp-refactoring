package kitchenpos.menu.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
    }

    public MenuResponse create(final MenuRequest menuRequest) {
        List<MenuProduct> menuProducts = makeMenuProducts(menuRequest.getMenuProducts());
        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuRequest.getMenuGroupId(), menuProducts);
        return MenuResponse.of(menuRepository.save(menu));
    }

    private List<MenuProduct> makeMenuProducts(List<MenuProductRequest> menuProductRequests) {
        List<Long> productIds = findProductIds(menuProductRequests);
        return replaceWithSavedProduct(menuProductRequests, productRepository.findAllById(productIds));
    }

    private List<Long> findProductIds(List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
            .map(MenuProductRequest::getProductId)
            .collect(Collectors.toList());
    }

    private List<MenuProduct> replaceWithSavedProduct(List<MenuProductRequest> menuProductRequests, List<Product> products) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : menuProductRequests) {
            Product product = findProductById(products, menuProductRequest.getProductId());
            menuProducts.add(new MenuProduct(product, menuProductRequest.getQuantity()));
        }
        return menuProducts;
    }

    private Product findProductById(List<Product> products, Long productId) {
        return products.stream()
            .filter(product -> product.getId().equals(productId))
            .findFirst()
            .orElseThrow(EntityNotFoundException::new);
    }

    public List<MenuResponse> list() {
        return MenuResponse.listOf(menuRepository.findAll());
    }
}
