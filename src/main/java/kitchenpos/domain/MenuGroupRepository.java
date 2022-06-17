package kitchenpos.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupRepository extends JpaRepository<MenuGroupEntity, Long> {

    boolean existsById(Long id);
}
