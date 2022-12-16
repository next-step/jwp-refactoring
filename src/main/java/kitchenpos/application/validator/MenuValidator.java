package kitchenpos.application.validator;

import static kitchenpos.exception.ErrorCode.PRICE_IS_NULL_OR_MINUS;

import java.math.BigDecimal;
import java.util.Objects;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.exception.ErrorCode;
import kitchenpos.exception.KitchenposException;

public class MenuValidator {
    public static void validatePrice(MenuRequest menuRequest){
        if(validatePriceNull(menuRequest.getPrice()) || validatePriceLessThanZero(menuRequest.getPrice())){
            throw new KitchenposException(PRICE_IS_NULL_OR_MINUS);
        }
    }

    private static boolean validatePriceNull(BigDecimal price){
        return Objects.isNull(price);
    }

    private static boolean validatePriceLessThanZero(BigDecimal price){
        return price.compareTo(BigDecimal.ZERO) < 0;
    }

    public static void validatePriceGreaterThanSum(BigDecimal sum, BigDecimal price) {
        if (price.compareTo(sum) > 0) {
            throw new KitchenposException(ErrorCode.PRICE_GREATER_THAN_SUM);
        }
    }
}
