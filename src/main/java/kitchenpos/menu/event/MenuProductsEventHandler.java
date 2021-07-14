package kitchenpos.menu.event;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MenuProductsEventHandler {
    private final ProductRepository productRepository;
    private final MenuProductRepository menuProductRepository;

    public MenuProductsEventHandler(ProductRepository productRepository, MenuProductRepository menuProductRepository) {
        this.productRepository = productRepository;
        this.menuProductRepository = menuProductRepository;
    }

    @Async
    @EventListener
    public void saveMenuProduct(MenuCreatedEvent event) {
        List<MenuProduct> menuProductList = new ArrayList<>();

        for (MenuProductRequest menuProductRequest : event.getMenuProductRequests()) {
            productRepository.findById(menuProductRequest.getProductId()).ifPresent(
                    product -> menuProductList.add(
                            new MenuProduct(event.getMenu(), product, menuProductRequest.getQuantity())
                    )
            );
        }

        MenuProducts menuProducts = new MenuProducts(menuProductList);

        MenuProductValidator.validPrice(menuProducts, event.getMenu());

        menuProductRepository.saveAll(menuProducts.getMenuProducts());
    }

}
