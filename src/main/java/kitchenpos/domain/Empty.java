package kitchenpos.domain;

public class Empty {

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
