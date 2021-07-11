package kitchenpos.table.domain;

import java.util.Objects;
import java.util.Optional;

import javax.persistence.*;

import kitchenpos.tablegroup.domain.TableGroup;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;
    @Column(name = "number_of_guests")
    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return Optional.ofNullable(this.tableGroup).map(tg -> tg.getId()).orElse(null);
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void setTableGroup(final TableGroup tableGroup) {
        if (!Objects.isNull(tableGroup)) {
            this.empty = false;
        }
        this.tableGroup = tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean hasTableGroup() {
        return !Objects.isNull(this.tableGroup);
    }

    public void changeEmpty(boolean empty) {
        this.empty = empty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateNumberOfGuestsIsLessThanZero(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validateNumberOfGuestsIsLessThanZero(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("수정 가능한 인원이 0보다 작을 수 없습니다.");
        }
    }
}
