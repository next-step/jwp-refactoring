package kitchenpos.menu.application.validator;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;
import kitchenpos.core.exception.CannotCreateException;
import kitchenpos.core.exception.ExceptionType;
import kitchenpos.core.exception.NotFoundException;
import kitchenpos.menu.dto.request.MenuProductRequest;
import kitchenpos.menu.dto.request.MenuRequest;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Component;

@Component
public class MenuCreationValidator implements MenuValidator {

    private final MenuGroupRepository menuGroupRepository;
    private final ProductService productService;

    public MenuCreationValidator(MenuGroupRepository menuGroupRepository, ProductService productService) {
        this.menuGroupRepository = menuGroupRepository;
        this.productService = productService;
    }

    @Override
    public void validate(MenuRequest menuRequest) {
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

        return productService.findByIdIn(productIds);
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
