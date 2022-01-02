package menu.exception;

import common.config.exception.PriceException;

public class WrongPriceException extends PriceException {
    public WrongPriceException() {
        super("가격이 0 이상이어야 합니다.");
    }
}
