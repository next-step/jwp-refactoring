package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.port.MenuGroupPort;
import kitchenpos.port.MenuPort;
import kitchenpos.port.ProductPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
        List<Product> product = productPort.findAllByIdIn(getProductId(request));
        Menu menu = new Menu(request.getName(), new Price(request.getPrice()), menuGroup);

        MenuProducts menuProducts = request.makeMenuProducts(product);

        menu.addMenuProducts(menuProducts);
        Menu saveMenu = menuPort.save(menu);

        return MenuResponse.from(saveMenu);
    }

    private List<Long> getProductId(MenuRequest request) {
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
}
