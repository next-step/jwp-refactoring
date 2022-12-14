package kitchenpos.exception;

import static java.lang.String.format;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import kitchenpos.domain.Money;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidMenuPriceException extends RuntimeException {

	public static final String MESSAGE = "유효하지 않은 메뉴 가격입니다, 메뉴가격='%s', 상품 가격 합='%s'";

	public InvalidMenuPriceException(Money price, Money productsPrice) {
		super(format(MESSAGE, price.toBigInteger(), productsPrice.toBigInteger()));
	}
}
