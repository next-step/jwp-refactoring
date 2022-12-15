package kitchenpos.infrastructure.jpa.adapter;

import kitchenpos.domain.MenuGroup;
import kitchenpos.port.MenuGroupPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MenuGroupJpaAdapter implements MenuGroupPort {
    @Override
    public MenuGroup save(MenuGroup entity) {
        return null;
    }

    @Override
    public Optional<MenuGroup> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<MenuGroup> findAll() {
        return null;
    }

    @Override
    public boolean existsById(Long id) {
        return false;
    }
}
