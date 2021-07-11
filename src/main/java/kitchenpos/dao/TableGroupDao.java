package kitchenpos.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.TableGroup;

public interface TableGroupDao extends JpaRepository<TableGroup, Long> {
    TableGroup save(TableGroup entity);

    Optional<TableGroup> findById(Long id);

    List<TableGroup> findAll();
}
