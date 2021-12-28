package kitchenpos.order.event;

public class OrderTableUngrouped {
    private Long tableGroupId;

    public OrderTableUngrouped(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public static OrderTableUngrouped from(Long tableGroupId){
        return new OrderTableUngrouped(tableGroupId);
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
