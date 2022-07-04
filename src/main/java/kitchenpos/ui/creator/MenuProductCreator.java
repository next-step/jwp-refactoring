package kitchenpos.ui.creator;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuProductCreator {

    private final ProductRepository productRepository;
    private final MenuRepository menuRepository;

    public MenuProductCreator(ProductRepository productRepository, MenuRepository menuRepository) {
        this.productRepository = productRepository;
        this.menuRepository = menuRepository;
    }

    public MenuProduct toMenuProduct(MenuProductRequest menuProductRequest){
        Product product = setProduct(menuProductRequest);

        Menu menu = menuRepository.findById(menuProductRequest.getMenuId()).orElseThrow(IllegalArgumentException::new);

        return MenuProduct.builder().seq(menuProductRequest.getSeq())
                .product(product)
                .quantity(menuProductRequest.getQuantity())
                .menu(menu).build();
    }

    public MenuProductRequest toMenuProductRequest(MenuProduct menuProduct){
        Long productId = setProductId(menuProduct);
        Long menuId = setMenuId(menuProduct);

        return MenuProductRequest.builder().seq(menuProduct.getSeq())
                .productId(productId)
                .quantity(menuProduct.getQuantity())
                .menuId(menuId)
                .build();
    }
    private Product setProduct(MenuProductRequest menuProductRequest) {
        return productRepository.findById(menuProductRequest.getProductId())
                .orElse(null);
    }

    private Long setProductId(MenuProduct menuProduct) {
        Long productId = null;
        if(menuProduct.getProduct()!= null){
           productId = menuProduct.getProduct().getId();
        }
        return productId;
    }

    private Long setMenuId(MenuProduct menuProduct) {
        Long menuId = null;
        if(menuProduct.getMenu()!= null){
            menuId = menuProduct.getMenu().getId();
        }
        return menuId;
    }
}
