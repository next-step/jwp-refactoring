package menu.validator;

import common.domain.Price;
import menu.domain.MenuProducts;
import menu.dto.MenuRequest;
import menugroup.repository.MenuGroupRepository;
import org.springframework.stereotype.Component;
import product.domain.Product;
import product.repository.ProductRepository;

import java.util.List;

import static common.ErrorMessage.*;

@Component
public class MenuValidator {
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validateCreateMenu(MenuRequest menuRequest, MenuProducts menuProducts) {
        validateMenuGroup(menuRequest.getMenuGroupId());
        validateMenuProducts(menuRequest, menuProducts);
    }

    private void validateMenuGroup(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException(INVALID_MENU_GROUP.getMessage());
        }
    }

    private void validateMenuProducts(MenuRequest menuRequest, MenuProducts menuProducts) {
        if (menuRequest.getMenuProductsRequest().size() != menuProducts.value().size()) {
            throw new IllegalArgumentException(INVALID_MENU_PRODUCT.getMessage());
        }

        Price price = Price.from(menuRequest.getPrice());
        if (price.compareTo(menuProducts.totalPrice()) > 0) {
            throw new IllegalArgumentException(MENU_PRICE_LESS_THAN_SUM_OF_PRICE.getMessage());
        }
    }

    public List<Product> findAllByIdIn(final List<Long> productIds) {
        return productRepository.findAllByIdIn(productIds);
    }
}
