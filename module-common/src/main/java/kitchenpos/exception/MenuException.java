package kitchenpos.exception;

public class MenuException extends CustomException {

    private static final String CLASSIFICATION = "MENU";

    public MenuException(String message) {
        super(CLASSIFICATION, message);
    }
}
