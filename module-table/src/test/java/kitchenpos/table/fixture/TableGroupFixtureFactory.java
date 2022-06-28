package kitchenpos.table.fixture;


import kitchenpos.table.domain.TableGroup;

public class TableGroupFixtureFactory {

    private TableGroupFixtureFactory() {}

    public static TableGroup create(Long id){
        return TableGroup.from(id);
    }
}
