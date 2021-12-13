package kitchenpos.application.menu;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.dto.menu.MenuGroupDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupDto create(final MenuGroupDto menuGroup) {
        return MenuGroupDto.of(menuGroupRepository.save(menuGroup.toMenuGroup()));
    }

    public List<MenuGroupDto> list() {
        return menuGroupRepository.findAll().stream()
                                    .map(MenuGroupDto::of)
                                    .collect(Collectors.toList());
    }
}
