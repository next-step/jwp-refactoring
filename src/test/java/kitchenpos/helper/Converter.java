package kitchenpos.helper;

import java.math.BigDecimal;

public class Converter {

    public static BigDecimal convert(int price){
        return BigDecimal.valueOf(price);
    }
}
