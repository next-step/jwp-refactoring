package kitchenpos.menu.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {
    @Override
    Optional<MenuGroup> findById(Long aLong);

}
