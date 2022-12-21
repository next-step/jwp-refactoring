package kitchenpos.menu.application;

import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.domain.*;
import kitchenpos.menugroup.port.MenuGroupPort;
import kitchenpos.menu.port.MenuPort;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.port.ProductPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.constants.ErrorCodeType.NOT_FOUND_PRODUCT;

@Service
@Transactional
public class MenuService {
    private final MenuPort menuPort;
    private final MenuGroupPort menuGroupPort;
    private final ProductPort productPort;

    public MenuService(
            final MenuPort menuPort,
            final MenuGroupPort menuGroupPort,
            final ProductPort productPort
    ) {
        this.menuPort = menuPort;
        this.menuGroupPort = menuGroupPort;
        this.productPort = productPort;
    }

    public MenuResponse create(final MenuRequest request) {
        MenuGroup menuGroup = menuGroupPort.findById(request.getMenuGroupId());
        List<Product> products = productPort.findAllByIdIn(getProductIds(request));
        Menu menu = new Menu(request.getName(), new Price(request.getPrice()), menuGroup.getId());

        MenuProducts menuProducts = makeMenuProducts(products, request);

        menu.addMenuProducts(menuProducts);
        Menu saveMenu = menuPort.save(menu);

        return MenuResponse.from(saveMenu);
    }

    private MenuProduct makeMenuProduct(List<Product> product, MenuProductRequest request) {
        Product targetProduct = findProduct(product, request.getProductId());
        return new MenuProduct(targetProduct, request.getQuantity());
    }

    private MenuProducts makeMenuProducts(List<Product> product, MenuRequest request) {
        return new MenuProducts(request.getMenuProduct().stream()
                .map(menuProduct -> makeMenuProduct(product, menuProduct))
                .collect(Collectors.toList()));
    }

    private List<Long> getProductIds(MenuRequest request) {
        return request.getMenuProduct()
                .stream()
                .map(MenuProductRequest::getProductId)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuPort.findAll();

        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }

    private Product findProduct(List<Product> products, Long productId) {
        return products.stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_PRODUCT.getMessage()));
    }
}
