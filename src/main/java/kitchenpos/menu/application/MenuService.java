package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
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
        final Menu savedMenu = Menu.of(
                menuRequest.getName(),
                menuRequest.getPrice(),
                menuGroupService.findMenuGroupById(menuRequest.getMenuGroupId()),
                findMenuProducts(menuRequest.getMenuProducts(), menuRequest.getPrice()));
        return MenuResponse.from(menuRepository.save(savedMenu));
    }

    public long countByIdIn(List<Long> ids) {
        return menuRepository.countByIdIn(ids);
    }

    public Menu findMenuById(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("메뉴가 존재하지 않습니다."));
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }

    private MenuProducts findMenuProducts(List<MenuProductRequest> menuProducts, BigDecimal menuPrice) {
        validateTotalPrice(menuProducts, menuPrice);

        return MenuProducts.from(menuProducts
                .stream()
                .map(menuProductRequest -> {
                    Product product = productService.findProductById(menuProductRequest.getProductId());
                    return MenuProduct.of(product.getId(), menuProductRequest.getQuantity());
                })
                .collect(Collectors.toList()));
    }

    private void validateTotalPrice(List<MenuProductRequest> menuProducts, BigDecimal menuPrice) {
        if (menuPrice.compareTo(calculatorTotalPrice(menuProducts)) > 0) {
            throw new IllegalArgumentException("메뉴의 가격은 " +
                    "메뉴 상품들의 수량 * 상품의 가격을 모두 더한 금액 보다 작거나 같아야 합니다.");
        }
    }

    private BigDecimal calculatorTotalPrice(List<MenuProductRequest> menuProducts) {
        return menuProducts.stream()
                .map(menuProduct -> {
                    Product product = productService.findProductById(menuProduct.getProductId());
                    return product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
