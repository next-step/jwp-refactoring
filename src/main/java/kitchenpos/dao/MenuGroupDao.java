package kitchenpos.dao;

import kitchenpos.domain.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuGroupDao extends JpaRepository<MenuGroup, Long> {
}
