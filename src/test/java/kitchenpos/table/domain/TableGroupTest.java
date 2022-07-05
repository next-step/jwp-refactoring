package kitchenpos.table.domain;

class TableGroupTest {

    public static TableGroup 테이블_그룹_생성(Long id) {
        return new TableGroup(id);
    }

    public static TableGroup 테이블_그룹_생성(Long id, OrderTables orderTables) {
        return new TableGroup(id, orderTables);
    }
}
