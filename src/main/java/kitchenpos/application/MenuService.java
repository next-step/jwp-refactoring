package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
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
        List<Product> product = productPort.findAllByIdIn(request.getProductId());

        Menu menu = Menu.of(request.getName(), Price.from(request.getPrice()), menuGroup, null);
        menu.addMenuProducts(request.makeMenuProducts(product));
        Menu saveMenu = menuPort.save(menu);

        return MenuResponse.from(saveMenu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuPort.findAll();

        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
