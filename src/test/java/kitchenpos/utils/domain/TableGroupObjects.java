package kitchenpos.utils.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;

public class TableGroupObjects {
    private final OrderTableObjects orderTableObjects;
    private final TableGroup tableGroup1;
    private final TableGroup tableGroup2;
    private final TableGroup tableGroup3;
    private final TableGroup tableGroup4;

    public TableGroupObjects() {
        orderTableObjects = new OrderTableObjects();
        tableGroup1 = new TableGroup();
        tableGroup2 = new TableGroup();
        tableGroup3 = new TableGroup();
        tableGroup4 = new TableGroup();

        tableGroup1.setId(1L);
        tableGroup2.setId(2L);
        tableGroup3.setId(3L);
        tableGroup4.setId(4L);
    }

    public TableGroup getTableGroup1() {
        return tableGroup1;
    }

    public TableGroup getTableGroup2() {
        return tableGroup2;
    }

    public TableGroup getTableGroup3() {
        return tableGroup3;
    }

    public TableGroup getTableGroup4() {
        return tableGroup4;
    }

    public List<TableGroup> getTableGroups() {
        return new ArrayList<>(Arrays.asList(tableGroup1, tableGroup2, tableGroup3, tableGroup4));
    }
}
