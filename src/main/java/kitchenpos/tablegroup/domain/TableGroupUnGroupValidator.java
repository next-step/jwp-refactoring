package kitchenpos.tablegroup.domain;

public class TableGroupUnGroupValidator {

    public static void validate(boolean existsOrderStatusComplete) {
        if (existsOrderStatusComplete) {
            throw new IllegalArgumentException("이미 계산이 완료되었습니다.");
        }
    }
}
