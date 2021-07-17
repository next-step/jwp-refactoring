package kitchenpos.menu.application;

import java.util.Optional;

import org.springframework.stereotype.Service;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;

@Service
public class MenuOrderServiceImpl implements MenuOrderService {
    private final MenuRepository menuRepository;

    public MenuOrderServiceImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public Optional<Menu> findMenuById(Long menuId) {
        return this.menuRepository.findById(menuId);
    }
}
