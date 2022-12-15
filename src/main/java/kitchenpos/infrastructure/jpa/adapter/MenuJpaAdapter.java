package kitchenpos.infrastructure.jpa.adapter;

import kitchenpos.domain.menu.Menu;
import kitchenpos.port.MenuPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MenuJpaAdapter implements MenuPort {
    @Override
    public Menu save(Menu entity) {
        return null;
    }

    @Override
    public Optional<Menu> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Menu> findAll() {
        return null;
    }

    @Override
    public long countByIdIn(List<Long> ids) {
        return 0;
    }
}
