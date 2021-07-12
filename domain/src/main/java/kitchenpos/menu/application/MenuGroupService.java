package kitchenpos.menu.application;

import java.util.List;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.stream.Collectors.toList;

@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupDto create(String name) {
        return MenuGroupDto.of(menuGroupRepository.save(new MenuGroup(name)));
    }

    @Transactional(readOnly = true)
    public List<MenuGroupDto> list() {
        return menuGroupRepository.findAll()
                                  .stream()
                                  .map(MenuGroupDto::of)
                                  .collect(toList());
    }
}
