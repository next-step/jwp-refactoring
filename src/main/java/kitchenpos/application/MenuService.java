package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuFactory menuFactory;

    public MenuService(final MenuRepository menuRepository, final MenuFactory menuFactory) {
        this.menuRepository = menuRepository;
        this.menuFactory = menuFactory;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        Menu menu = menuFactory.create(menuRequest);
        return MenuResponse.of(menuRepository.save(menu));
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return MenuResponse.ofList(menus);
    }
}
