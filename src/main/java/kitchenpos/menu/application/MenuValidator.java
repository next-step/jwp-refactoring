package kitchenpos.menu.application;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import kitchenpos.common.domain.Price;
import kitchenpos.common.exception.CannotCreateException;
import kitchenpos.common.exception.ExceptionType;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.domain.request.MenuProductRequest;
import kitchenpos.menu.domain.request.MenuRequest;
import kitchenpos.menuGroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validate(MenuRequest menuRequest) {
        validateExistMenuGroup(menuRequest.getMenuGroupId());
        validateRegisteredProducts(menuRequest.getMenuProductRequests());
        validateMenuProducts(menuRequest.getMenuProductRequests(), menuRequest.getPrice());
    }

    private void validateExistMenuGroup(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new NotFoundException(ExceptionType.NOT_EXIST_MENU.getMessage(menuGroupId));
        }
    }

    private void validateRegisteredProducts(List<MenuProductRequest> menuProductRequests) {
        List<Product> products = findAllByMenuProductRequests(menuProductRequests);

        if (products.size() != menuProductRequests.size()) {
            throw new CannotCreateException(ExceptionType.CONTAINS_NOT_EXIST_PRODUCT);
        }
    }

    private void validateMenuProducts(List<MenuProductRequest> menuProductRequests, BigDecimal price) {
        menuProductRequests.forEach(this::validateHasProductId);
        validatePriceOverMenuPrice(menuProductRequests, new Price(price));
    }

    private void validateHasProductId(MenuProductRequest menuProductRequest) {
        if (Objects.isNull(menuProductRequest.getProductId())) {
            throw new CannotCreateException(ExceptionType.CONTAINS_NOT_EXIST_PRODUCT);
        }
    }

    private void validatePriceOverMenuPrice(List<MenuProductRequest> menuProductRequests, Price price) {
        Price sum = new Price(BigDecimal.ZERO);
        for (MenuProductRequest item : menuProductRequests) {
            Product product = findByProductId(item.getProductId());
            Price menuProductPrice = new Price(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            sum = sum.add(menuProductPrice);
        }

        if (price.isOverThan(sum)) {
            throw new CannotCreateException(ExceptionType.IS_NOT_OVER_THAN_MENU_PRICE);
        }
    }

    private List<Product> findAllByMenuProductRequests(List<MenuProductRequest> menuProductRequests) {
        List<Long> productIds = menuProductRequests.stream()
            .map(MenuProductRequest::getProductId)
            .collect(toList());

        return productRepository.findByIdIn(productIds);
    }

    private Product findByProductId(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new CannotCreateException(ExceptionType.NOT_EXIST_PRODUCT));
    }
}
