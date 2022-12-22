package kitchenpos.product.application;

import kitchenpos.common.Price;
import kitchenpos.common.Quantity;
import kitchenpos.product.dto.MenuProductRequest;
import kitchenpos.product.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuProductService {

    private final MenuProductRepository menuProductRepository;
    private final ProductService productService;

    public MenuProductService(MenuProductRepository menuProductRepository, ProductService productService) {
        this.menuProductRepository = menuProductRepository;
        this.productService = productService;
    }

    @Transactional
    public MenuProducts createMenuProducts(List<MenuProductRequest> menuProductRequests, Price menuPrice, Long menuId) {
        List<MenuProduct> menuProductList = menuProductRequests.stream()
                .map(menuProductRequest -> getMenuProduct(menuId, menuProductRequest))
                .collect(Collectors.toList());

        final MenuProducts menuProducts = new MenuProducts(menuProductList);
        menuProducts.checkValidMenuPrice(menuPrice);

        menuProductRepository.saveAll(menuProductList);

        return menuProducts;
    }

    private MenuProduct getMenuProduct(Long menuId, MenuProductRequest menuProductRequest) {
        final Long productId = menuProductRequest.getProductId();
        final Product product = productService.findById(productId);
        final Long quantity = menuProductRequest.getQuantity();
        return new MenuProduct(menuId, product, new Quantity(quantity));
    }

    public MenuProducts getMenuProductByMenuId(Long menuId) {
        return new MenuProducts(menuProductRepository.findAllByMenuId(menuId));
    }
}
