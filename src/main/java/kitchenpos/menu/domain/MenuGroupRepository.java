package kitchenpos.menu.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {
	Optional<MenuGroup> findById(Long id);

	boolean existsById(Long id);
}
