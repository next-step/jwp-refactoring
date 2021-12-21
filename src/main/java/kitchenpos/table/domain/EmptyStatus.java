package kitchenpos.table.domain;

public class EmptyStatus {
    private boolean empty;

    protected EmptyStatus() {
    }

    public EmptyStatus(boolean empty) {
        this.empty = empty;
    }

    public boolean getStatus() {
        return empty;
    }
}
