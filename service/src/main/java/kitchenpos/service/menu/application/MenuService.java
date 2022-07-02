package kitchenpos.service.menu.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.service.menu.dto.MenuProductRequest;
import kitchenpos.service.menu.dto.MenuRequest;
import kitchenpos.service.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(
            final MenuRepository menuRepository, final MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        menuValidator.validate(request);

        Menu menu = new Menu(request.getName(), request.getPrice(), request.getMenuGroupId());
        menuRepository.save(menu);

        for (final MenuProductRequest menuProductRequest : request.getMenuProducts()) {
            menu.add(new MenuProduct(menu.getId(), menuProductRequest.getProductId(), menuProductRequest.getQuantity()));
        }

        return new MenuResponse(menu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream().map(MenuResponse::new).collect(Collectors.toList());
    }
}
