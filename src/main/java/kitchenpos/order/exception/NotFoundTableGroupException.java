package kitchenpos.order.exception;

public class NotFoundTableGroupException extends IllegalArgumentException{
    public NotFoundTableGroupException(){
        super("테이블그룹을 찾을 수 없습니다.");
    }

}
