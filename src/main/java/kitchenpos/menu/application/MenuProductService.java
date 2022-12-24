package kitchenpos.menu.application;

import kitchenpos.common.domain.Price;
import kitchenpos.common.exception.NoSuchDataException;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.product.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuProductService {

    private final ProductRepository productRepository;
    private final MenuProductRepository menuProductRepository;

    public MenuProductService(final ProductRepository productRepository, final MenuProductRepository menuProductRepository) {
        this.productRepository = productRepository;
        this.menuProductRepository = menuProductRepository;
    }

    @Transactional
    public List<MenuProductResponse> createAll(List<MenuProductRequest> menuProductRequests, Long menuId, int menuPrice) {
        List<MenuProduct> menuProductList = menuProductRequests.stream()
                .map(menuProductRequest -> generateMenuProduct(menuProductRequest, menuId))
                .collect(Collectors.toList());

        MenuProducts menuProducts = new MenuProducts(menuProductList);
        menuProducts.validateMenuPrice(new Price(menuPrice));
        MenuProducts savedMenuProducts = new MenuProducts(menuProductRepository.saveAll(menuProductList));

        return savedMenuProducts.getMenuProducts().stream().map(MenuProductResponse::of).collect(Collectors.toList());
    }

    public List<MenuProductResponse> findMenuProductsByMenuId(Long menuId) {
        return menuProductRepository.findAllByMenuId(menuId).stream()
                .map(MenuProductResponse::of)
                .collect(Collectors.toList());
    }

    private MenuProduct generateMenuProduct(final MenuProductRequest menuProductRequest, Long menuId) {
        Product product = findProductById(menuProductRequest.getProductId());
        return new MenuProduct(menuId, product, menuProductRequest.getQuantity());
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(NoSuchDataException::new);
    }
}