package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Empty {

    @Column(nullable = false, columnDefinition = "bit(1)")
    private boolean empty;

    protected Empty() {
    }

    public Empty(boolean empty) {
        this.empty = empty;
    }

    public boolean is() {
        return empty;
    }
}
