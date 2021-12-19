package kitchenpos.exception;

public class TableGroupNotFoundException extends NotFoundException {
    private static final String DEFAULT_MESSAGE = "단체 지정을 찾을 수 없습니다 : %d";

    public TableGroupNotFoundException(Long tableGroupId) {
        super(String.format(DEFAULT_MESSAGE, tableGroupId));
    }
}
