package kitchenpos.menugroup.application;

import java.util.Optional;

import kitchenpos.menugroup.domain.MenuGroup;

public interface MenuGroupMenuService {
    Optional<MenuGroup> findMenuGroupById(Long menuGroupId);
}
