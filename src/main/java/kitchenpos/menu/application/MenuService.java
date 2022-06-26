package kitchenpos.menu.application;

import static java.util.Objects.requireNonNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupService menuGroupService;
    private final ProductService productService;

    public MenuService(MenuRepository menuRepository, MenuGroupService menuGroupService,
        ProductService productService) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        validateCreateRequest(menuRequest);

        Menu menu = new Menu(
            menuRequest.getName(),
            menuRequest.getPrice(),
            menuRequest.getMenuGroupId(),
            convertMenuProductEntity(menuRequest.getMenuProducts())
        );

        menuRepository.save(menu);

        return MenuResponse.of(menu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        List<Menu> menus = menuRepository.findAll();

        return menus.stream()
            .map(MenuResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Menu findMenuById(Long menuId) {
        return menuRepository.findById(menuId)
            .orElseThrow(IllegalArgumentException::new);
    }

    private List<MenuProduct> convertMenuProductEntity(List<MenuProductRequest> menuProductRequests) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : menuProductRequests) {
            Product product = productService.findProductById(menuProductRequest.getProductId());
            menuProducts.add(new MenuProduct(product.getId(), menuProductRequest.getQuantity()));
        }
        return menuProducts;
    }

    private void validateCreateRequest(MenuRequest menuRequest) {
        requireNonNull(menuRequest.getMenuProducts(), "상품이 설정되지 않았습니다.");
        requireNonNull(menuRequest.getMenuGroupId(), "메뉴 그룹이 설정되지 않았습니다.");

        if (!menuGroupService.existsMenuGroup(menuRequest.getMenuGroupId())) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다.");
        }

        Price price = new Price(menuRequest.getPrice());
        if (price.isGreaterThan(this.calculateTotalPrice(menuRequest.getMenuProductsToEntity()))) {
            throw new IllegalArgumentException("메뉴의 금액이 상품의 총합보다 클 수 없습니다.");
        }
    }

    private BigDecimal calculateTotalPrice(List<MenuProduct> menuProducts) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (MenuProduct menuProduct : menuProducts) {
            Product product = productService.findProductById(menuProduct.getProductId());
            BigDecimal price = product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity()));
            totalPrice = totalPrice.add(price);
        }
        return totalPrice;
    }

}
