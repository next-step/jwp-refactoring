package kitchenpos.menu.domain;

import kitchenpos.menu.exception.LimitPriceException;
import kitchenpos.menu.exception.MenuProductNotFoundException;
import kitchenpos.product.exception.ProductNotFoundException;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.exception.MenuGroupNotFoundException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * packageName : kitchenpos.menu.domain
 * fileName : MenuValidator
 * author : haedoang
 * date : 2021/12/26
 * description :
 */
//TODO : DDD 도메인 서비스에 대한 이해
@Component
public class MenuValidator {
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validate(Menu menu) {
        if (menu.getMenuProducts().isEmpty()) {
            throw new MenuProductNotFoundException("메뉴 항목이 비어있습니다.");
        }

        if (!isExistMenuGroup(menu)) {
            throw new MenuGroupNotFoundException();
        }

        if (invalidMenuPrice(menu.getPrice(), menu.getMenuProducts())) {
            throw new LimitPriceException();
        }
    }

    private boolean isExistMenuGroup(Menu menu) {
        return menuGroupRepository.existsById(menu.getMenuGroupId());
    }

    private boolean invalidMenuPrice(MenuPrice price, List<MenuProduct> menuProducts) {
        return price.value().compareTo(getTotalPrice(menuProducts)) > 0;
    }

    private BigDecimal getTotalPrice(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(
                        menuProduct -> {
                            Product product = getProduct(menuProduct.getProductId());
                            return menuProduct.price(product.getPrice());
                        }
                ).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);
    }
}
