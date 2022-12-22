package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.Price;
import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.exception.ErrorMessage;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
        MenuRepository menuRepository,
        MenuGroupRepository menuGroupRepository,
        ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = findMenuGroupById(menuRequest.getMenuGroupId());
        List<MenuProduct> menuProducts = menuRequest.getMenuProductRequests().stream()
            .map(it -> createMenuProduct(it.getProductId(), it.getQuantity()))
            .collect(Collectors.toList());
        validatePrice(menuRequest.getPrice(), menuProducts);
        Menu menu = Menu.of(menuRequest.getName(), menuRequest.getPrice(), menuGroup, MenuProducts.of(menuProducts));

        return MenuResponse.of(menuRepository.save(menu));
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
            .stream()
            .map(MenuResponse::of)
            .collect(Collectors.toList());
    }

    private void validatePrice(BigDecimal price, List<MenuProduct> menuProducts) {
        Price menuPrice = Price.of(price);
        Price menuProductsPrice = getTotalPrice(menuProducts);
        if (menuPrice.compareTo(menuProductsPrice) > 0) {
            throw new IllegalArgumentException(ErrorMessage.PRICE_HIGHER_THAN_MENU_PRODUCTS_TOTAL_PRICES);
        }
    }

    private Price getTotalPrice(List<MenuProduct> menuProducts) {
        return menuProducts
            .stream()
            .map(this::productPrice)
            .reduce(Price::add)
            .orElse(Price.ZERO);
    }

    private MenuProduct createMenuProduct(Long productId, int quantity) {
        Product product = findProductById(productId);
        return MenuProduct.of(product, quantity);
    }

    private MenuGroup findMenuGroupById(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
            .orElseThrow(() -> new EntityNotFoundException(MenuGroup.ENTITY_NAME, menuGroupId));

    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new EntityNotFoundException(Product.ENTITY_NAME, productId));

    }

    private Price productPrice(MenuProduct menuProduct) {
        Price price = menuProduct.getProduct().getPrice();
        return menuProduct.getTotalPrice(price);
    }

}
