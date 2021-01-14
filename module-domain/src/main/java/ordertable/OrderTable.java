package kitchenpos.domain.ordertable;

import kitchenpos.domain.ordertable.exceptions.InvalidTryChangeGuestsException;

import javax.persistence.*;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private NumberOfGuests numberOfGuests;

    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        validateCreation(numberOfGuests, empty);
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public void changeEmpty(final boolean empty) {
        // TODO: 이 부분이 단체 지정 Aggregate과 협력하도록 변경하면 됨
        this.empty = empty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (this.isEmpty()) {
            throw new InvalidTryChangeGuestsException("비어있는 주문 테이블의 방문한 손님 수를 바꿀 수 없습니다.");
        }
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getValue();
    }

    public boolean isEmpty() {
        return empty;
    }

    private void validateCreation(final int numberOfGuest, final boolean empty) {
        if (empty && numberOfGuest != 0) {
            throw new InvalidOrderTableException("비어 있는 경우 손님수는 0명이어야 한다.");
        }
    }
}
