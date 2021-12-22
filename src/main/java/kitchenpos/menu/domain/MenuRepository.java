package kitchenpos.menu.domain;

import kitchenpos.common.exception.NotFoundMenuException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    Long countByIdIn(List<Long> id);

    default Menu findByIdElseThrow(Long id) {
        return this.findById(id)
                .orElseThrow(NotFoundMenuException::new);
    }
}
