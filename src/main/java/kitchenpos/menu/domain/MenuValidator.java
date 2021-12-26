package kitchenpos.menu.domain;

import kitchenpos.menu.exception.LimitPriceException;
import kitchenpos.menu.exception.MenuGroupNotFoundException;
import kitchenpos.menu.exception.ProductNotFoundException;
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
            throw new IllegalStateException("메뉴 항목이 비어있습니다.");
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


//    private void comparePrice(List<MenuProduct> menuProducts) {
//        if (price.value().compareTo(totalPrice(menuProducts)) > 0) {
//            throw new LimitPriceException();
//        }
//    }

    //    private BigDecimal totalPrice(List<MenuProduct> menuProducts) {
//        return menuProducts.stream().map(MenuProduct::price)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//    }

//    private List<Product> getProducts(Menu menu) {
//        List<Long> productIds = menu.getMenuProducts()
//                .stream()
//                .map(MenuProduct::getId)
//                .collect(toList());
//        return productRepository.findAllById(productIds);
//    }
}
