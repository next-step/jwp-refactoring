package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuMapper;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;

@Transactional
@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;
    private final MenuProductRepository menuProductRepository;

    public MenuService(final MenuRepository menuRepository,
        final MenuValidator menuValidator, final MenuProductRepository menuProductRepository) {

        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
        this.menuProductRepository = menuProductRepository;
    }

    public MenuResponse create(final MenuRequest menuRequest) {
        final MenuMapper menuMapper = new MenuMapper(menuValidator, menuRequest);
        final Menu menu = menuMapper.toMenu();
        final Menu saved = menuRepository.save(menu);
        final List<MenuProduct> menuProducts = menuMapper.toMenuProducts(saved);
        final List<MenuProduct> savedMenuProducts = menuProductRepository.saveAll(menuProducts);

        return MenuResponse.of(saved, savedMenuProducts);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll()
            .stream()
            .map(MenuResponse::of)
            .collect(Collectors.toList());
    }
}
