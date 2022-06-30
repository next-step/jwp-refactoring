package kitchenpos.menu.application.validator;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;

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
public class MenuCreationValidator implements MenuValidator {

    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuCreationValidator(MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void execute(MenuRequest menuRequest) {
        validateExistMenuGroup(menuRequest.getMenuGroupId());
        validateRegisteredProducts(menuRequest.getMenuProductRequests());
        validateMenuProducts(menuRequest.getMenuProductRequests());
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

    private List<Product> findAllByMenuProductRequests(List<MenuProductRequest> menuProductRequests) {
        List<Long> productIds = menuProductRequests.stream()
            .map(MenuProductRequest::getProductId)
            .collect(toList());

        return productRepository.findByIdIn(productIds);
    }

    private void validateMenuProducts(List<MenuProductRequest> menuProductRequests) {
        menuProductRequests.forEach(this::validateHasProductId);
    }

    private void validateHasProductId(MenuProductRequest menuProductRequest) {
        if (Objects.isNull(menuProductRequest.getProductId())) {
            throw new CannotCreateException(ExceptionType.CONTAINS_NOT_EXIST_PRODUCT);
        }
    }
}
