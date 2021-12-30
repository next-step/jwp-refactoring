package kitchenpos.exception;

public class OverMenuPriceException extends BadRequestException {
    private static final String EXCEPTION_MESSAGE = "메뉴 가격이 상품의 가격 합계보다 높습니다.";

    public OverMenuPriceException() {
        super(EXCEPTION_MESSAGE);
    }
}
