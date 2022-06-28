package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
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
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        if (request.getPrice().compareTo(totalAmount(request.getMenuProducts())) > 0) {
            throw new IllegalArgumentException("메뉴 가격은 구성 상품 금액 총합보다 크면 안 됩니다.");
        }

        Menu persistMenu = menuRepository.save(toEntity(request));

        return toResponse(persistMenu);
    }

    private BigDecimal totalAmount(List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
                .map(menuProduct ->
                        productRepository.getById(menuProduct.getProductId())
                                .getPrice(menuProduct.getQuantity()))
                .reduce(BigDecimal.ZERO, (acc, amount) -> acc.add(amount));
    }

    private Menu toEntity(final MenuRequest request) {
        List<MenuProduct> menuProducts = request.getMenuProducts().stream()
                .map(menuProduct -> new MenuProduct(
                        menuProduct.getProductId(),
                        menuProduct.getQuantity()))
                .collect(Collectors.toList());
        MenuGroup menuGroup = menuGroupRepository.getById(request.getMenuGroupId());

        return Menu.createMenu(
                request.getName(),
                request.getPrice(),
                menuGroup,
                menuProducts);
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(menu -> toResponse(menu))
                .collect(Collectors.toList());
    }

    public MenuResponse toResponse(Menu menu) {
        List<MenuProductResponse> menuProductResponses = menu.getMenuProducts().stream()
                .map(menuProduct -> {
                    Product product = productRepository.getById(menuProduct.getProductId());
                    return MenuProductResponse.of(
                            product.getId(),
                            product.getName(),
                            product.getUnitPrice(),
                            menuProduct.getQuantity());
                })
                .collect(Collectors.toList());
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menu.getMenuGroup().getId(),
                menuProductResponses);
    }
}
