package kitchenpos.manugroup.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.manugroup.domain.MenuGroup;
import kitchenpos.manugroup.domain.MenuGroupRepository;

@Service
public class MenuGroupService {
	private final MenuGroupRepository menuGroupRepository;

	public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
		this.menuGroupRepository = menuGroupRepository;
	}

	@Transactional
	public MenuGroup create(final MenuGroup menuGroup) {
		return menuGroupRepository.save(menuGroup);
	}

	public List<MenuGroup> list() {
		return menuGroupRepository.findAll();
	}
}
