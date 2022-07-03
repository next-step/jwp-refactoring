package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<OrderTable> orderTables = new ArrayList<>();

    public OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void add(OrderTable orderTable){
        orderTables.add(orderTable);
    }

    public void validateCreate(){
        //2개 이상 그룹 등록
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }

        // table group 등록하면서 cascade 할거임
//       final List<Long> orderTableIds = orderTables.stream()
//                .map(OrderTable::getId)
//                .collect(Collectors.toList());
//
//        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
//
//        //등록 안된 orderTable 있으면 에러
//        if (orderTables.size() != savedOrderTables.size()) {
//            throw new IllegalArgumentException();
//        }

        //한건이라도 이미 등록된 단체 있으면 에러
        for (final OrderTable orderTable : orderTables) {
            orderTable.canGroupTable();
        }
    }
}
