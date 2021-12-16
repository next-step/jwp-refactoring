package kitchenpos.menu.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {
	Optional<MenuProduct> findById(Long id);
}
