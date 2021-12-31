package kitchenpos.menu.application;

import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.IllegalArgumentException;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductService productService;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository, ProductService productService) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productService = productService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        validateMenu(menuRequest);
        final MenuProducts menuProducts = toMenuProducts(menuRequest);
        final Menu menu = toMenu(menuRequest, menuProducts);

        return MenuResponse.of(menuRepository.save(menu));
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }

    private void validateMenu(MenuRequest menuRequest) {
        checkMenuGroup(menuRequest.getMenuGroupId());
    }

    private void checkMenuGroup(Long menuGroupId) {
        menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new BadRequestException("해당 메뉴 그룹을 찾을 수 없습니다."));
    }

    private Menu toMenu(MenuRequest menuRequest, MenuProducts menuProducts) {
        return Menu.of(menuRequest.getName()
                , Price.of(menuRequest.getPrice())
                , menuRequest.getMenuGroupId()
                , menuProducts);
    }

    private MenuProducts toMenuProducts(MenuRequest menuRequest) {
        List<MenuProduct> menuProducts = menuRequest.getMenuProducts()
                .stream()
                .map(this::toMenuProduct)
                .collect(Collectors.toList());

        return MenuProducts.of(menuProducts);
    }

    private MenuProduct toMenuProduct(MenuProductRequest menuProductRequest) {
        Long productId = menuProductRequest.getProductId();
        ProductResponse productResponse = productService.findById(productId);
        Quantity quantity = Quantity.of(menuProductRequest.getQuantity());
        BigDecimal productPrice = productResponse.getPrice();

        return MenuProduct.of(productId, quantity, productPrice);
    }
}
