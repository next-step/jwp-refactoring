package kitchenpos.common.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Empty {
    private boolean empty;

    public Empty() {
    }

    public Empty(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
