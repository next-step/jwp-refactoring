package kitchenpos.menu.service;

import kitchenpos.domain.MenuProduct;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.product.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MenuProductService {
    private final MenuProductRepository menuProductRepository;
    private final ProductService productService;

    public MenuProductService(MenuProductRepository menuProductRepository, ProductService productService) {
        this.menuProductRepository = menuProductRepository;
        this.productService = productService;
    }

    public MenuProduct saveProduct(long menuId, MenuProductRequest menuProductRequest) {
        return menuProductRepository.save(MenuProduct.of(
                menuId,
                productService.findById(menuProductRequest.getProductId()),
                menuProductRequest.ofQuantity()
        ));
    }
}
