package kitchenpos.menu.domain;

import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.exception.LimitPriceException;
import kitchenpos.menu.exception.MenuGroupNotFoundException;
import kitchenpos.menu.exception.ProductNotFoundException;
import kitchenpos.product.domain.Price;
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
@Component
public class MenuValidator {
    private final static int SAME = 0;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validate(Menu menu) {
        if (menu.getMenuProducts().isEmpty()) {
            throw new NotFoundException("메뉴 항목이 비어있습니다.");
        }

        if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new MenuGroupNotFoundException();
        }

        if (!samePrice(menu.getPrice(), menu.getMenuProducts())) {
            throw new LimitPriceException();
        }
    }

    private boolean samePrice(Price price, List<MenuProduct> menuProducts) {
        return price.value().compareTo(getTotalPrice(menuProducts)) == SAME;
    }

    private BigDecimal getTotalPrice(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(
                    menuProduct ->  {
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
