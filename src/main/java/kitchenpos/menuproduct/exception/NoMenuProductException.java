package kitchenpos.menuproduct.exception;

public class NoMenuProductException extends RuntimeException{
    public NoMenuProductException() {
        super("메뉴상품이 하나도 없습니다");
    }
}
