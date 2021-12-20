package kichenpos.table.table.ui.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class EmptyRequest {

    private final boolean empty;

    @JsonCreator
    public EmptyRequest(@JsonProperty("empty") boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
