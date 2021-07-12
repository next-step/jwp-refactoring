package kitchenpos.table.domain;

public enum TableStatus {
    ORDER(0),
    EMPTY(1),
    IN_USE(2),
    COMPLETION(3);

    private int statusCode;

    TableStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public int statusCode() {
        return statusCode;
    }
}
