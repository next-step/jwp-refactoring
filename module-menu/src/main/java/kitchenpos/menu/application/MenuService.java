package kitchenpos.menu.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuCreationValidator;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuCreationValidator menuCreationValidator;

    public MenuService(MenuRepository menuRepository, MenuCreationValidator menuCreationValidator) {
        this.menuRepository = menuRepository;
        this.menuCreationValidator = menuCreationValidator;
    }

    @Transactional
    public MenuDto create(final MenuDto menuDto) {
        menuCreationValidator.validate(menuDto.getPrice(), menuDto.menuProducts());
        return MenuDto.of(menuRepository.save(menuDto.toMenu()));
    }

    public List<MenuDto> list() {
        List<Menu> menus = menuRepository.findAll();
        return menus.stream().map(MenuDto::of).collect(toList());
    }
}
