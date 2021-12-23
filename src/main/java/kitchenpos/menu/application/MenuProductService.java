package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuProductService {

    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuProductService(
            final MenuProductRepository menuProductRepository
            , final ProductRepository productRepository
    ) {
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuProducts allSave(List<MenuProductRequest> menuProductRequests, Menu menu) {
        List<MenuProduct> savedMenuProducts = menuProductRepository.saveAll(getMenuProducts(menuProductRequests));
        MenuProducts menuProducts = MenuProducts.of(savedMenuProducts);
        menuProducts.initMenu(menu);
        return menuProducts;
    }

    public List<MenuProduct> findAllByMenu(Menu menu) {
        return menuProductRepository.findAllByMenu(menu);
    }

    private List<MenuProduct> getMenuProducts(List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
                .map(this::getMenuProduct)
                .collect(Collectors.toList());
    }

    private MenuProduct getMenuProduct(MenuProductRequest menuProductRequest) {
        final Product product = productRepository.findByIdElseThrow(menuProductRequest.getProductId());
        return menuProductRequest.toMenuProduct(product);
    }
}
