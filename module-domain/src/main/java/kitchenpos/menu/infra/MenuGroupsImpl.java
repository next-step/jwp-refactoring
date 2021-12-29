package kitchenpos.menu.infra;

import org.springframework.stereotype.Component;

import kitchenpos.menu.domain.MenuGroups;
import kitchenpos.menugroup.domain.MenuGroupRepository;

@Component
public class MenuGroupsImpl implements MenuGroups {
	private final MenuGroupRepository menuGroupRepository;

	public MenuGroupsImpl(MenuGroupRepository menuGroupRepository) {
		this.menuGroupRepository = menuGroupRepository;
	}

	@Override
	public boolean contains(Long id) {
		return menuGroupRepository.findById(id).isPresent();
	}
}
