package kitchenpos.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kitchenpos.menu.domain.MenuGroup;

@Repository
public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {

}
