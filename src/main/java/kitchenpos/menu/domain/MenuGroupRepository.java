package kitchenpos.menu.domain;

import kitchenpos.common.exception.NotFoundMenuGroupException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {
    default MenuGroup findByIdElseThrow(Long id) {
        return this.findById(id)
                .orElseThrow(NotFoundMenuGroupException::new);
    }
}
