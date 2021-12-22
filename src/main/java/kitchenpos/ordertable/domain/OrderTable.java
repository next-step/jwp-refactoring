package kitchenpos.ordertable.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private NumberOfGuests numberOfGuests;
    @Embedded
    private OrderTableEmpty empty;

    public OrderTable() {
    }

    private OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
        this.empty = OrderTableEmpty.of(empty);
    }

    private OrderTable(Long id, int numberOfGuests, boolean empty) {
        this(numberOfGuests, empty);
        this.id = id;
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public static OrderTable generate(Long id, int numberOfGuests, boolean empty) {
        return new OrderTable(id, numberOfGuests, empty);
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumberOfGuests();
    }

    public boolean isEmpty() {
        return empty.isEmpty();
    }

    public boolean matchNumberOfGuests(int targetNumberOfGuests) {
        return numberOfGuests.matchNumberOfGuests(targetNumberOfGuests);
    }

    public void changeNumberOfGuests(int targetNumberOfGuests) {
        if (isEmpty()) {
            throw new IllegalArgumentException("빈 테이블일 경우 방문한 손님 수를 변경할 수 없습니다.");
        }
        this.numberOfGuests = NumberOfGuests.of(targetNumberOfGuests);
    }

    public void changeEmpty(boolean empty) {
        this.empty = OrderTableEmpty.of(empty);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderTable that = (OrderTable) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
