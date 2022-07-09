package kitchenpos.table.domain;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ErrorCode;
import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.PERSIST)
    private List<OrderTable> elements = new ArrayList<>();

    public OrderTables() {
    }

    public OrderTables(List<OrderTable> elements) {
        if (CollectionUtils.isEmpty(elements) || elements.size() < 2) {
            throw new BadRequestException(ErrorCode.MORE_THAN_TWO_ORDER_TABLE);
        }
        this.elements = elements;
    }

    public List<OrderTable> getElements() {
        return Collections.unmodifiableList(new ArrayList<>(elements));
    }

    public List<Long> extractIds() {
        return elements.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public int getSize() {
        return elements.size();
    }

    public void add(TableGroup tableGroup) {
        elements.forEach(orderTable -> orderTable.updateTableGroup(tableGroup));
    }

    public List<OrderTable> changeTableGroup() {
        elements.stream().forEach(orderTable -> orderTable.updateTableGroup(null));
        return elements;
    }
}
