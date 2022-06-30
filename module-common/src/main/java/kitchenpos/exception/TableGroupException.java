package kitchenpos.exception;

public class TableGroupException extends CustomException {

    private static final String CLASSIFICATION = "TABLEGROUP";

    public TableGroupException(String message) {
        super(CLASSIFICATION, message);
    }
}
