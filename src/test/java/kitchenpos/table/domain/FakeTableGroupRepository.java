package kitchenpos.table.domain;

import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;

import java.util.*;

public class FakeTableGroupRepository implements TableGroupRepository {
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
