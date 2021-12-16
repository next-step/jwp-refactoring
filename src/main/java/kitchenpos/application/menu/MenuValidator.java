package kitchenpos.application.menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Component;

import kitchenpos.domain.Price;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.utils.StreamUtils;

@Component
public class MenuValidator {
    private static final String NOT_EXIST_MENU_GROUP_PRICE = "Menu 는 MenuGroup 가 필수값 입니다.";
    private static final String NOT_EXIST_MENU_PRICE = "Menu 는 Price 가 필수값 입니다.";
    private static final String NOT_EXIST_MENU = "Menu 가 존재하지 않습니다.";
    private static final String NOT_EXIST_PRODUCT = "Product 가 존재하지 않습니다.";
    private static final String INVALID_MENU_PRICE = "Menu Price 는 상품 가격 총합보다 작아야합니다.";

    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validate(Menu menu) {
        validateExistMenu(menu);
        validateMenuGroup(findMenuGroup(menu.getMenuGroupId()));
        validateProducts(menu.getMenuProducts());
        validateMenuPrice(menu.getPrice(), menu.getMenuProducts());
    }

    public void validateMenuProduct(MenuProduct menuProduct) {
        validateExistMenu(menuProduct.getMenu());
        validateProduct(menuProduct.getProductId());
    }

    private void validateMenuGroup(MenuGroup menuGroup) {
        if (Objects.isNull(menuGroup)) {
            throw new IllegalArgumentException(NOT_EXIST_MENU_GROUP_PRICE);
        }
    }

    private void validateExistMenu(Menu menu) {
        if (Objects.isNull(menu)) {
            throw new IllegalArgumentException(NOT_EXIST_MENU);
        }
    }

    private void validateMenuPrice(Price price, MenuProducts menuProducts) {
        if (Objects.isNull(price)) {
            throw new IllegalArgumentException(NOT_EXIST_MENU_PRICE);
        }

        validateMenuPriceCompareToTotalProductPrice(price, menuProducts);
    }

    private void validateProducts(MenuProducts menuProducts) {
        List<Long> productIds = StreamUtils.mapToList(menuProducts.getValues(), MenuProduct::getProductId);
        List<Product> products = findProducts(productIds);

        if (productIds.size() != products.size()) {
            throw new EntityNotFoundException(NOT_EXIST_PRODUCT);
        }
    }

    private void validateMenuPriceCompareToTotalProductPrice(Price price, MenuProducts menuProducts) {
        List<Long> productIds = StreamUtils.mapToList(menuProducts.getValues(), MenuProduct::getProductId);
        Map<Long, Price> productPriceDict = StreamUtils.mapToMap(findProducts(productIds),
                                                                 Product::getId,
                                                                 Product::getPrice);
        BigDecimal sum = StreamUtils.sumToBigDecimal(
            menuProducts.getValues(),
            menuProduct -> menuProduct.calculateTotalPrice(productPriceDict.get(menuProduct.getProductId())));

        if (price.isGreaterThan(sum)) {
            throw new IllegalArgumentException(INVALID_MENU_PRICE);
        }
    }

    private void validateProduct(Long productId) {
        Optional<Product> productOpt = productRepository.findById(productId);

        if (!productOpt.isPresent()) {
            throw new EntityNotFoundException(NOT_EXIST_PRODUCT);
        }
    }

    private MenuGroup findMenuGroup(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                                  .orElseThrow(EntityNotFoundException::new);
    }

    private List<Product> findProducts(List<Long> productIds) {
        return productRepository.findAllById(productIds);
    }
}
