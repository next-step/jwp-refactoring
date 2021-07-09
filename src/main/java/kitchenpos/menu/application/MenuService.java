package kitchenpos.menu.application;

import static kitchenpos.exception.KitchenposExceptionMessage.NOT_FOUND_MENU_GROUP;
import static kitchenpos.exception.KitchenposExceptionMessage.NOT_FOUND_PRODUCT;
import static kitchenpos.exception.KitchenposExceptionMessage.PRICE_CANNOT_LOWER_THAN_MIN;

import java.util.stream.Collectors;
import kitchenpos.exception.KitchenposException;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Transactional
@Service
public class MenuService {

    private static final BigDecimal MIN_PRICE = BigDecimal.ZERO;

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
        checkPriceGreaterThanMin(request.getPrice());
        final MenuGroup menuGroup = findMenuGroupById(request.getMenuGroupId());
        return MenuResponse.of(menuRepository.save(request.toMenu(menuGroup,
                                                                  getMenuProducts(request))));
    }

    private List<MenuProduct> getMenuProducts(MenuRequest request) {
        return request.getMenuProducts()
                      .stream()
                      .map(this::createMenuProduct)
                      .collect(Collectors.toList());
    }

    private MenuProduct createMenuProduct(MenuProductRequest menuProductRequest) {
        return new MenuProduct(findProductById(menuProductRequest),
                               menuProductRequest.getQuantity());
    }

    private Product findProductById(MenuProductRequest menuProductRequest) {
        return productRepository.findById(menuProductRequest.getProductId())
                                .orElseThrow(() -> new KitchenposException(NOT_FOUND_PRODUCT));
    }

    private void checkPriceGreaterThanMin(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(MIN_PRICE) < 0) {
            throw new KitchenposException(PRICE_CANNOT_LOWER_THAN_MIN);
        }
    }

    private MenuGroup findMenuGroupById(final Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                                  .orElseThrow(() -> new KitchenposException(NOT_FOUND_MENU_GROUP));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                             .map(MenuResponse::of)
                             .collect(Collectors.toList());
    }
}
