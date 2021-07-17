package kitchenpos.menu.application;

import java.util.List;

import org.springframework.stereotype.Service;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;

@Service
public class MenuProductService {
    private final MenuProductRepository menuProductRepository;

    public MenuProductService(final MenuProductRepository menuProductRepository) {
        this.menuProductRepository = menuProductRepository;
    }

    public List<MenuProduct> createAll(final List<MenuProduct> menuProducts) {
        return menuProductRepository.saveAll(menuProducts);
    }
}
