package kitchenpos.core;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Empty {

    @Column(nullable = false)
    private boolean empty;

    public Empty(boolean empty) {
        this.empty = empty;
    }

    protected Empty() {
    }

    public boolean isTrue() {
        return empty;
    }
}
