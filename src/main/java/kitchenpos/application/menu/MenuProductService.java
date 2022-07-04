package kitchenpos.application.menu;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProductRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.menu.MenuProductRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuProductService {

    private final ProductRepository productRepository;

    public MenuProductService(ProductRepository productRepository, MenuProductRepository menuProductRepository) {
        this.productRepository = productRepository;
    }

    public List<MenuProduct> findMenuProductByMenuProductRequest(List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
            .map(it -> {
                Product product = productRepository.findById(it.getProductId()).orElseThrow(IllegalArgumentException::new);
                return new MenuProduct(product, it.getQuantity());
            })
            .collect(Collectors.toList());
    }
}
