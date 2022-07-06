package kitchenpos.application.menu;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.menu.MenuProductRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuProductService {

    public static final String PRODUCT_NOT_FOUND_ERROR_MESSAGE = "요청에 해당하는 상품이 존재하지 않습니다.";
    private final ProductRepository productRepository;

    public MenuProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<MenuProduct> findMenuProductByMenuProductRequest(List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
            .map(it -> {
                Product product = productRepository.findById(it.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException(PRODUCT_NOT_FOUND_ERROR_MESSAGE));
                return new MenuProduct(product, it.getQuantity());
            })
            .collect(Collectors.toList());
    }
}
