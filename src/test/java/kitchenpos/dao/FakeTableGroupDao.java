package kitchenpos.dao;

import kitchenpos.domain.TableGroup;

import java.util.*;

public class FakeTableGroupDao implements TableGroupDao {
    private Map<Long, TableGroup> map = new HashMap<>();
    private Long key = 1L;

    @Override
    public TableGroup save(TableGroup tableGroup) {
        tableGroup.createId(key);
        map.put(key, tableGroup);
        key++;
        return tableGroup;
    }

    @Override
    public Optional<TableGroup> findById(Long id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public List<TableGroup> findAll() {
        return new ArrayList<>(map.values());
    }
}
