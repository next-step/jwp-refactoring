package kitchenpos.menu.application;

import kitchenpos.ExceptionMessage;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Price;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupService menuGroupService;
    private final ProductService productService;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupService menuGroupService,
            final ProductService productService
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        final BigDecimal price = menuRequest.getPrice();
        final MenuGroup menuGroup = menuGroupService.findById(menuRequest.getMenuGroupId());
        final List<MenuProduct> menuProducts = menuRequest.getMenuProducts()
                .stream()
                .map(this::getMenuProduct)
                .collect(Collectors.toList());
        final Menu menu = new Menu(menuRequest.getName(), new Price(price), menuGroup, new MenuProducts(menuProducts));
        final Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.of(savedMenu);
    }

    private MenuProduct getMenuProduct(MenuProductRequest menuProductRequest) {
        final Long productId = menuProductRequest.getProductId();
        final Long quantity = menuProductRequest.getQuantity();
        return new MenuProduct(productService.findById(productId), new Quantity(quantity));
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }

    public Menu findById(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.NOT_EXIST_MENU.getMessage()));
    }
}
