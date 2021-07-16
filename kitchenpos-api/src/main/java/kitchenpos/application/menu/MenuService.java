package kitchenpos.application.menu;

import static kitchenpos.exception.KitchenposExceptionMessage.MENU_PRICE_CANNOT_OVER_THAN_PRODUCT_PRICE;
import static kitchenpos.exception.KitchenposExceptionMessage.NOT_FOUND_MENU_GROUP;
import static kitchenpos.exception.KitchenposExceptionMessage.NOT_FOUND_PRODUCT;

import java.util.stream.Collectors;
import kitchenpos.common.Price;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.exception.KitchenposException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(final MenuRepository menuRepository,
                       final MenuGroupRepository menuGroupRepository,
                       final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public MenuResponse create(final MenuRequest request) {
        final MenuGroup menuGroup = findMenuGroupById(request.getMenuGroupId());
        checkOverPrice(Price.of(request.getPrice()), request.getMenuProducts());
        Menu savedMenu = menuRepository.save(request.toMenu(menuGroup.getId(), getMenuProducts(request)));
        return MenuResponse.of(savedMenu);
    }

    private void checkOverPrice(Price price, List<MenuProductRequest> menuProductRequests) {
        Price totalProductPrice = menuProductRequests.stream()
                                                     .map(this::getProductPrice)
                                                     .reduce(Price.ZERO, Price::add);
        if (price.isBiggerThan(totalProductPrice)) {
            throw new KitchenposException(
                MENU_PRICE_CANNOT_OVER_THAN_PRODUCT_PRICE);
        }
    }

    private Price getProductPrice(MenuProductRequest menuProductRequest) {
        return findProductById(menuProductRequest.getProductId()).getPrice()
                                                                 .multiply(menuProductRequest.getQuantity());
    }

    private Product findProductById(final Long productId) {
        return productRepository.findById(productId)
                                .orElseThrow(() -> new KitchenposException(NOT_FOUND_PRODUCT));
    }

    private List<MenuProduct> getMenuProducts(MenuRequest request) {
        return request.getMenuProducts()
                      .stream()
                      .map(this::createMenuProduct)
                      .collect(Collectors.toList());
    }

    private MenuProduct createMenuProduct(MenuProductRequest menuProductRequest) {
        return new MenuProduct(menuProductRequest.getProductId(),
                               menuProductRequest.getQuantity());
    }

    private MenuGroup findMenuGroupById(final Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                                  .orElseThrow(() -> new KitchenposException(
                                      NOT_FOUND_MENU_GROUP));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                             .map(MenuResponse::of)
                             .collect(Collectors.toList());
    }
}
