package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.MenuGroup;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(idClass = Long.class, domainClass = MenuGroup.class)
public interface MenuGroupDao {

    MenuGroup save(MenuGroup entity);

    Optional<MenuGroup> findById(Long id);

    List<MenuGroup> findAll();

    boolean existsById(Long id);
}
