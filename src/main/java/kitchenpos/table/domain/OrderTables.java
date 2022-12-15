package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.tablegroup.domain.TableGroup;

@Embeddable
public class OrderTables {



    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<OrderTable> orderTables;

    protected OrderTables() {
    }

    private OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public static OrderTables of(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void groupChange(TableGroup tableGroup) {
        this.orderTables.forEach(it->it.updateGroup(tableGroup));
    }

    public boolean isAllFinished(){
        return this.orderTables.stream()
                .allMatch(it->it.getOrders().isAllFinished());
    }

    public boolean isAllEmpty() {
        return orderTables.stream().allMatch(it-> it.getEmpty().value());
    }

    public boolean isAnyGrouped(){
        return orderTables.stream()
                .anyMatch(it-> Objects.nonNull(it.getTableGroup()));
    }
    public int getSize(){
        return orderTables.size();
    }

}
