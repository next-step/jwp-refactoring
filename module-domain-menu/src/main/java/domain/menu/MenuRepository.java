package domain.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Query("select distinct m from Menu m " +
            "inner join fetch m.menuGroup ")
    List<Menu> findAllWithMenuGroupFetchJoin();
}
