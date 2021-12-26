package kitchenpos.menu.domain;

import kitchenpos.common.exception.NotFoundMenuException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    default Menu findByIdElseThrow(Long id) {
        return this.findById(id)
                .orElseThrow(NotFoundMenuException::new);
    }
}
