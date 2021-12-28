package kitchenpos.menu.domain;

import kitchenpos.global.exception.EntityNotFoundException;
import kitchenpos.menu.exception.MenuPriceMoreThanMenuProductPriceSumException;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MenuValidator {

    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void menuCreateValidator(Menu menu) {
        existMenuGroupValidator(menu);
        List<Product> products = existProductValidator(menu);
        checkMenuPrice(menu, products);
    }

    private List<Product> existProductValidator(final Menu menu) {
        final List<Product> products = getMenuProducts(menu);
        checkExistProduct(menu, products);
        return products;
    }

    private void existMenuGroupValidator(final Menu menu) {
        menuGroupRepository.findById(menu.getMenuGroup())
                .orElseThrow(() -> new EntityNotFoundException(String.format("menu not found. find menu id is %d", menu.getMenuGroup())));
    }

    private List<Product> getMenuProducts(final Menu menu) {
        final List<Long> productIds = menu.getMenuProducts()
                .stream()
                .map(MenuProduct::getProduct)
                .collect(Collectors.toList());

        return productRepository.findByIdIn(productIds);
    }

    private void checkExistProduct(final Menu menu, final List<Product> products) {
        for (MenuProduct menuProduct : menu.getMenuProducts()) {
            getProduct(products, menuProduct);
        }
    }

    private Product getProduct(final List<Product> products, final MenuProduct menuProduct) {
        return products.stream()
                .filter(product -> product.getId().equals(menuProduct.getProduct()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(String.format("menu product not found. find menu product id is %d", menuProduct.getProduct())));
    }

    private void checkMenuPrice(Menu menu, List<Product> products) {
        final BigDecimal menuProductPriceSum = getMenuProductPriceSum(menu, products);
        if (menu.getPrice().compareTo(menuProductPriceSum) > 0) {
            throw new MenuPriceMoreThanMenuProductPriceSumException(menu.getPrice().toPlainString());
        }
    }

    private BigDecimal getMenuProductPriceSum(Menu menu, List<Product> products) {
        BigDecimal menuProductPriceSum = BigDecimal.ZERO;

        for (final MenuProduct menuProduct : menu.getMenuProducts()) {
            Product product = getProduct(products, menuProduct);
            final BigDecimal productPrice = product.getPrice();
            final BigDecimal menuProductTotalPrice = productPrice.multiply(BigDecimal.valueOf(menuProduct.getQuantity()));
            menuProductPriceSum = menuProductPriceSum.add(menuProductTotalPrice);
        }

        return menuProductPriceSum;
    }
}
