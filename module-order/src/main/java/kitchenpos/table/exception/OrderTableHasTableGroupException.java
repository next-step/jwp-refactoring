package kitchenpos.table.exception;

public class OrderTableHasTableGroupException extends IllegalArgumentException {

    public OrderTableHasTableGroupException() {
        super("주문테이블이 그룹테이블에 속해 있습니다.");
    }

}
