package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderTable> orderTables = new ArrayList<>();

    public OrderTables() {
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void add(OrderTable orderTable){
        orderTables.add(orderTable);
    }

    public void validateCreate(){
        //2개 이상 그룹 등록
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }

        //한건이라도 이미 등록된 단체 있으면 에러
        for (final OrderTable orderTable : orderTables) {
            orderTable.validateCanGroupTable();
        }
    }

    public void setAllEmpty(boolean bool) {
        for (OrderTable savedOrderTable : orderTables) {
            savedOrderTable.setEmpty(bool);
        }
    }
}
