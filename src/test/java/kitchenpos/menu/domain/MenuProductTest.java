package kitchenpos.menu.domain;

import static kitchenpos.product.domain.ProductTest.*;

import org.junit.jupiter.api.DisplayName;

import kitchenpos.generic.quantity.domain.Quantity;

@DisplayName("메뉴-제품 매핑 단위 테스트")
public class MenuProductTest {
    public static MenuProduct MP1후라이드 = new MenuProduct(후라이드.getId(), Quantity.valueOf(1));
    public static MenuProduct MP2양념치킨 = new MenuProduct(양념치킨.getId(), Quantity.valueOf(1));
    public static MenuProduct MP3반반치킨 = new MenuProduct(반반치킨.getId(), Quantity.valueOf(1));
    public static MenuProduct MP4통구이 = new MenuProduct(통구이.getId(), Quantity.valueOf(1));
    public static MenuProduct MP5간장치킨 = new MenuProduct(간장치킨.getId(), Quantity.valueOf(1));
    public static MenuProduct MP6순살치킨 = new MenuProduct(순살치킨.getId(), Quantity.valueOf(1));
}
