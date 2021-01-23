package kitchenpos.table.domain;

import kitchenpos.exception.BadRequestException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class OrderTable {
    private static final int EMPTY_NUMBER = 0;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tableGroupId;
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

    public void group(Long tableGroupId) {
        if (tableGroupId == null) {
            throw new BadRequestException("Table group은 null로 세팅할 수 없습니다.");
        }
        this.tableGroupId = tableGroupId;
    }

    public void ungroup() {
        this.tableGroupId = null;
    }

    public boolean isGrouped() {
        return Objects.nonNull(tableGroupId);
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
