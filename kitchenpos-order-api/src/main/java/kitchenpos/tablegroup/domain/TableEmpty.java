package kitchenpos.tablegroup.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TableEmpty {
    @Column
    private boolean empty;

    protected TableEmpty() {}

    public TableEmpty(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
