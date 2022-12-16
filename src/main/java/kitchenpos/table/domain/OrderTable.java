package kitchenpos.table.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "order_table")
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    protected OrderTable() {}

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
        this.empty = empty;
    }

    public void changeEmpty(boolean empty, boolean existsOrderStatusComplete) {
        validateOrderStatusComplete(existsOrderStatusComplete);
        validateTableGroupNull();
        this.empty = empty;
    }

    private void validateTableGroupNull() {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException("단체 지정된 테이블입니다.");
        }
    }

    private void validateOrderStatusComplete(boolean exists){
        if(exists){
            throw new IllegalArgumentException("이미 계산이 완료되었습니다.");
        }
    }


    public void changeNumberOfGuests(int numberOfGuests) {
        validateChangeNumberOfGuestsNotEmpty();
        this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
    }

    private void validateChangeNumberOfGuestsNotEmpty() {
        if (empty) {
            throw new IllegalArgumentException("빈 테이블 입니다.");
        }
    }

    public void unTableGroup() {
        this.tableGroup = null;
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public int getNumberOfGuestsValue() {
        return numberOfGuests.value();
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public void setTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public Long tableGroupId() {
        if (Objects.isNull(tableGroup)) {
            return null;
        }
        return tableGroup.getId();
    }

    public static class Builder {

        private Long id;
        private TableGroup tableGroup;
        private int numberOfGuests;
        private boolean empty;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder tableGroup(TableGroup tableGroup) {
            this.tableGroup = tableGroup;
            return this;
        }

        public Builder numberOfGuests(int numberOfGuests) {
            this.numberOfGuests = numberOfGuests;
            return this;
        }

        public Builder empty(boolean empty) {
            this.empty = empty;
            return this;
        }

        public OrderTable build() {
            return new OrderTable(id, tableGroup, numberOfGuests, empty);
        }
    }
}
