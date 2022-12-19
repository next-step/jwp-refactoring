package kitchenpos.menu.mapper;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MenuMapper {

    private final ProductRepository productRepository;

    public MenuMapper(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public Menu mapFrom(final MenuRequest menuRequest) {
        return new Menu(menuRequest.getName(), menuRequest.getPrice(), menuRequest.getMenuGroupId(),
                createMenuProducts(menuRequest.getMenuProductRequests()));
    }

    private List<MenuProduct> createMenuProducts(List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
                .map(menuProductRequest -> {
                    Product product = productRepository.findById(menuProductRequest.getProductId())
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "등록되지 않은 상품은 메뉴상품으로 지정할 수 없습니다[productId:" + menuProductRequest.getProductId()
                                            + "]"));
                    return new MenuProduct(null, product.getId(), menuProductRequest.getQuantity());
                })
                .collect(Collectors.toList());
    }
}
