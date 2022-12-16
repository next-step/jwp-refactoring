package kitchenpos.menu.validator;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class MenuValidator {

    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository,
                         ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public void validateCreation(Long menuGroupId) {
        menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "메뉴 등록시, 등록되어 있는 메뉴 그룹만 지정할 수 있습니다[menuGroupId:" + menuGroupId + "]"));
    }

    @Transactional(readOnly = true)
    public List<MenuProduct> createMenuProducts(List<MenuProductRequest> menuProductRequests) {
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

    public void validateProductsPrice(Menu menu) {
        BigDecimal price = menu.getPrice();
        List<MenuProduct> menuProducts = menu.getMenuProducts();
        BigDecimal sumMenuProductsPrice = makeSumMenuProductsPrice(menuProducts);
        if (price.compareTo(sumMenuProductsPrice) > 0) {
            throw new IllegalArgumentException(
                    "메뉴의 가격은 메뉴상품들 가격의 합보다 낮아야 합니다[price:" + price + "/sumMenuProductsPrice:" + sumMenuProductsPrice
                            + "]");
        }
    }

    @Transactional(readOnly = true)
    BigDecimal makeSumMenuProductsPrice(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(menuProduct -> productRepository.findById(menuProduct.getProductId())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "등록되지 않은 상품은 메뉴로 지정할 수 없습니다[productId:" + menuProduct.getProductId() + "]"))
                        .multiply(menuProduct.getQuantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
