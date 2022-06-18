package kitchenpos.application.fixture;

import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupFixtureFactory {

    private TableGroupFixtureFactory() {}

    public static TableGroup create(Long id){
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(id);

        return tableGroup;
    }

    public static TableGroup create(){
        return new TableGroup();
    }
}
