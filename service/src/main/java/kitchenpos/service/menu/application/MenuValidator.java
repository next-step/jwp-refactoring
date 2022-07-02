package kitchenpos.service.menu.application;

import kitchenpos.domain.menu.InvalidPriceException;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.service.menu.dto.MenuProductRequest;
import kitchenpos.service.menu.dto.MenuRequest;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
public class MenuValidator {
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(
            MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }
    public void validate(MenuRequest request) {
        validateMenuGroup(request.getMenuGroupId());
        validateMenuProducts(request);
    }

    private void validateMenuGroup(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new NoSuchElementException();
        }
    }

    private void validateMenuProducts(MenuRequest request) {
        long sum = 0;
        for (MenuProductRequest menuProductRequest : request.getMenuProducts()) {
            final Product product = productRepository.findById(menuProductRequest.getProductId()).orElseThrow(NoSuchElementException::new);
            sum += product.getPrice() * menuProductRequest.getQuantity();
        }

        if (request.getPrice() > sum) {
            throw new InvalidPriceException();
        }
    }
}
