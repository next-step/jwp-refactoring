package kitchenpos.application.fixture;

import kitchenpos.domain.TableGroup;

public class TableGroupFixtureFactory {

    private TableGroupFixtureFactory() {}

    public static TableGroup create(Long id){
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(id);

        return tableGroup;
    }
}
