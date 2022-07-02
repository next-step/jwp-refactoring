package kitchenpos.service.menu.application;

import kitchenpos.menu.domain.InvalidPriceException;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
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
