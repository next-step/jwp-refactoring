package kitchenpos.domain;

import kitchenpos.exception.TableEmptyUpdateException;
import kitchenpos.exception.TableGuestNumberUpdateException;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    @OneToOne(mappedBy = "orderTable", cascade = {CascadeType.ALL})
    private Order order;

    protected OrderTable() {
    }

    public OrderTable(Integer numberOfGuests, boolean empty) {
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
        this.empty = empty;
    }

    public void changeEmpty(boolean changeEmpty) {
        // 테이블 그룹이 있으면 예외처리
        if (Objects.nonNull(tableGroup)) {
            throw new TableEmptyUpdateException();
        }
        // 주문 상태가 완료가 아닌 경우 예외처리
        if (Objects.nonNull(order) && !order.isCompleted()) {
            throw new TableEmptyUpdateException();
        }
        this.empty = changeEmpty;
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests.value();
    }

    public boolean isEmpty() {
        return empty;
    }

    public Order getOrder() {
        return order;
    }

    public boolean isOrderFinished() {
        return order.isCompleted();
    }

    public void updateNumberOfGuests(Integer newNumberOfGuests) {
        if (empty) {
            throw new TableGuestNumberUpdateException();
        }

        this.numberOfGuests = NumberOfGuests.of(newNumberOfGuests);
    }

    public OrderTable addOrder(Order order) {
        this.order = order;
        return this;
    }

    public OrderTable groupBy(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        return this;
    }
}
