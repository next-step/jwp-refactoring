package kitchenpos.menu.event;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import kitchenpos.product.domain.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
        BigDecimal sum = BigDecimal.ZERO;

        for (MenuProductRequest menuProductRequest : event.getMenuProductRequests()) {
            Product product = findByProductId(menuProductRequest.getProductId());
            menuProductList.add(new MenuProduct(event.getMenu(), product.getId(), menuProductRequest.getQuantity()));
            sum = sum.add(multiplyPrice(product.getPrice(), menuProductRequest.getQuantity()));
        }

        MenuProductValidator.validPrice(event.getMenu(), sum);

        menuProductRepository.saveAll(menuProductList);
    }

    private BigDecimal multiplyPrice(BigDecimal MenuPrice, Long quantity) {
        return MenuPrice.multiply(BigDecimal.valueOf(quantity));
    }

    private Product findByProductId(Long prductId) {
        return productRepository.findById(prductId).orElseThrow(IllegalArgumentException::new);
    }

}
