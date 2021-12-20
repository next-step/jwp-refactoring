package kitchenpos.menugroup.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;

@Repository(value = "ToBeJpaMenuGroupRepository")
public interface JpaMenuGroupRepository extends MenuGroupRepository, JpaRepository<MenuGroup, Long> {
}
