package kitchenpos.global.error;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "C001", " Invalid Input Value"),
    ENTITY_NOT_FOUND(400, "C003", " Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "C004", "Server Error"),
    INVALID_TYPE_VALUE(400, "C005", " Invalid Type Value"),

    // Menu
    MENU_PRICE_MORE_THAN_MENU_PRODUCT(400, "M001", "It's bigger than the sum of the menu products"),

    // Order
    ORDER_STATUS_COMPLETION(400, "C001", "When the order status is completed, status cannot be changed"),

    // Table
    TABLE_NOT_AVAILABLE(400, "T001", "It's an unavailable table"),
    TABLE_GROUP_NOT_AVAILABLE(400, "T002", "You can't designate it as a table group"),
    ALREADY_TABLE_GROUP(400, "T003", "It's designated as a table group");



    private final String code;
    private final String message;
    private int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }
}
