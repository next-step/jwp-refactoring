package kitchenpos.menu.validator;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.constant.ErrorCode;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(final MenuGroupRepository menuGroupRepository, final ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validate(MenuRequest menuRequest) {
        validateMenuGroup(findMenuGroupById(menuRequest.getMenuGroupId()));
        validateMenuProducts(menuRequest);
    }

    private void validateMenuGroup(MenuGroup menuGroup) {
        if(menuGroup == null) {
            throw new IllegalArgumentException(ErrorCode.메뉴_그룹은_비어있을_수_없음.getErrorMessage());
        }
    }

    private MenuGroup findMenuGroupById(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.존재하지_않는_메뉴_그룹.getErrorMessage()));
    }

    private void validateMenuProducts(MenuRequest menuRequest) {
        Price price = Price.from(menuRequest.getPrice());
        MenuProducts menuProducts = MenuProducts.from(findAllMenuProductsByProductId(menuRequest.getMenuProductRequests()));
        if(price.compareTo(calculateTotalPrice(menuProducts.findMenuProducts())) > 0) {
            throw new IllegalArgumentException(ErrorCode.메뉴의_가격은_메뉴상품들의_가격의_합보다_클_수_없음.getErrorMessage());
        }
    }

    private List<MenuProduct> findAllMenuProductsByProductId(List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
                .map(menuProductRequest -> {
                    Long productId = menuProductRequest.getProductId();
                    return menuProductRequest.toMenuProduct(findProductById(productId));
                })
                .collect(Collectors.toList());
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.존재하지_않는_상품.getErrorMessage()));
    }

    private Price calculateTotalPrice(List<MenuProduct> menuProducts) {
        Price totalPrice = Price.ZERO_PRICE;
        for(MenuProduct menuProduct: menuProducts) {
            Product product = findProductById(menuProduct.getProductId());
            Price menuProductPrice = product.getPrice()
                    .multiply(menuProduct.getQuantity());
            totalPrice = totalPrice.add(menuProductPrice);
        }
        return totalPrice;
    }
}
