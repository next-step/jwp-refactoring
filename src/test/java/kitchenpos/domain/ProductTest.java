package kitchenpos.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("제품 단위 테스트")
public class ProductTest {

    public static Product 후라이드 = new Product(1L, "후라이드", Price.valueOf(16000));
    public static Product 양념치킨 = new Product(2L, "양념치킨", Price.valueOf(16000));
    public static Product 반반치킨 = new Product(3L, "반반치킨", Price.valueOf(16000));
    public static Product 통구이 = new Product(4L, "통구이", Price.valueOf(16000));
    public static Product 간장치킨 = new Product(5L, "간장치킨", Price.valueOf(17000));
    public static Product 순살치킨 = new Product(6L, "순살치킨", Price.valueOf(17000));

    @Test
    @DisplayName("가격이 없거나 음수이면 오류 발생")
    void negativePriceOrNull() {
        assertThrows(IllegalArgumentException.class, () -> new Product(1L, "엽기떡볶이", Price.valueOf(-1)));
        assertThrows(IllegalArgumentException.class, () -> new Product(1L, "엽기떡볶이", null));
    }
}
