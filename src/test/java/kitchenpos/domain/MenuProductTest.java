package kitchenpos.domain;

import static kitchenpos.domain.MenuTest.*;
import static kitchenpos.domain.ProductTest.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.exception.AlreadyAllocatedException;

@DisplayName("메뉴-제품 매핑 단위 테스트")
public class MenuProductTest {
    public static MenuProduct MP1후라이드 = new MenuProduct(후라이드, Quantity.valueOf(1));
    public static MenuProduct MP2양념치킨 = new MenuProduct(양념치킨, Quantity.valueOf(1));
    public static MenuProduct MP3반반치킨 = new MenuProduct(반반치킨, Quantity.valueOf(1));
    public static MenuProduct MP4통구이 = new MenuProduct(통구이, Quantity.valueOf(1));
    public static MenuProduct MP5간장치킨 = new MenuProduct(간장치킨, Quantity.valueOf(1));
    public static MenuProduct MP6순살치킨 = new MenuProduct(순살치킨, Quantity.valueOf(1));
}
