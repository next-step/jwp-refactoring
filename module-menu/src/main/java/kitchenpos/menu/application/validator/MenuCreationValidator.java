package kitchenpos.menu.application.validator;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ErrorCode;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

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
        validateRegisteredProducts(menuRequest.getMenuProducts());
        validateMenuProducts(menuRequest.getMenuProducts());
    }

    private void validateExistMenuGroup(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new NotFoundException(ErrorCode.MENU_GROUP_NOT_FOUND);
        }
    }

    private void validateRegisteredProducts(List<MenuProductRequest> menuProductRequests) {
        List<Product> products = findAllByMenuProductRequests(menuProductRequests);

        if (products.size() != menuProductRequests.size()) {
            throw new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND);
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
            throw new BadRequestException(ErrorCode.CONTAINS_NOT_EXIST_MENU);
        }
    }
}
