package kitchenpos.table.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Empty {

    @Column(nullable = false)
    private boolean empty;

    protected Empty() {
    }

    private Empty(final boolean empty) {
        this.empty = empty;
    }

    public static Empty of(final boolean empty) {
        return new Empty(empty);
    }

    public boolean isEmpty() {
        return empty;
    }
}
