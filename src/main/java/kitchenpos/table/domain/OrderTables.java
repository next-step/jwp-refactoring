package kitchenpos.table.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.PERSIST)
    private List<OrderTable> elements = new ArrayList<>();

    public OrderTables() {
    }

    public OrderTables(List<OrderTable> elements) {
        if (CollectionUtils.isEmpty(elements) || elements.size() < 2) {
            throw new IllegalArgumentException();
        }

        this.elements = elements;
    }

    public List<OrderTable> getOrderTables() {
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

    public void validate(int number) {
        if (this.getSize() != number) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : this.elements) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
                throw new IllegalArgumentException();
            }
        }
    }

    public void add(TableGroup tableGroup) {
        elements.forEach(orderTable -> orderTable.updateTableGroup(tableGroup));
    }
}
