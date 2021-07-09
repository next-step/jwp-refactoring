package kitchenpos.application.query;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.dto.response.MenuViewResponse;
import kitchenpos.exception.EntityNotExistsException;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuQueryService {
    private final MenuRepository menuRepository;
    private final MenuProductRepository menuProductRepository;

    public MenuQueryService(MenuRepository menuRepository, MenuProductRepository menuProductRepository) {
        this.menuRepository = menuRepository;
        this.menuProductRepository = menuProductRepository;
    }

    public List<MenuViewResponse> list() {
        List<Menu> menus = menuRepository.findAll();
        List<MenuProduct> menuProducts = menuProductRepository.findAll();

        List<MenuViewResponse> results = menus.stream()
                .map(item -> MenuViewResponse.of(item, menuProducts))
                .collect(Collectors.toList());

        return results;
    }

    public MenuViewResponse findById(Long id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(EntityNotExistsException::new);
        List<MenuProduct> menuProducts = menuProductRepository.findAllByMenu_Id(menu.getId());
        return MenuViewResponse.of(menu, menuProducts);
    }
}
