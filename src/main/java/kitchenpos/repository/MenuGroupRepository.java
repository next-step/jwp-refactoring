package kitchenpos.repository;

import kitchenpos.domain.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {

    @Query("SELECT CASE WHEN COUNT(m.id) > 0 THEN TRUE ELSE FALSE END FROM MenuGroup m WHERE m.id = :id")
    boolean existsById(@Param("id") Long id);
}
