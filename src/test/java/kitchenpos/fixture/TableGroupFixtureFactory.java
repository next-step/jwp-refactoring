package kitchenpos.fixture;


import kitchenpos.domain.tablegroup.TableGroup;

public class TableGroupFixtureFactory {

    private TableGroupFixtureFactory() {}

    public static TableGroup create(Long id){
        return TableGroup.from(id);
    }
}
