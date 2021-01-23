package kitchenpos.table.domain;

import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.exception.BadRequestException;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {
    private static final int EMPTY_NUMBER = 0;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private TableGroup tableGroup;
    private int numberOfGuests = EMPTY_NUMBER;

    public void update(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return numberOfGuests == EMPTY_NUMBER;
    }

    public void group(TableGroup tableGroup) {
        if (tableGroup == null) {
            throw new BadRequestException("Table group은 null로 세팅할 수 없습니다.");
        }
        this.tableGroup = tableGroup;
    }

    public void ungroup() {
        this.tableGroup = null;
    }

    public boolean isGroupped() {
        return Objects.nonNull(tableGroup);
    }

    public Long getTableGroupId() {
        return (tableGroup == null)?null:tableGroup.getId();
    }
}
