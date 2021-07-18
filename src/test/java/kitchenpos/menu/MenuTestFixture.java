package kitchenpos.menu;

import kitchenpos.common.Price;
import kitchenpos.common.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;

import java.util.Arrays;
import java.util.List;

public class MenuTestFixture {

    public static final Long 맥모닝_메뉴그룹_ID = 1L;
    public static final Long 아이스_아메리카노_상품_ID = 1L;
    public static final Long 에그맥머핀_상품_ID = 2L;

    public static final Menu 맥모닝콤보 = new Menu("맥모닝콤보", Price.valueOf(10000), 맥모닝_메뉴그룹_ID);
    public static final MenuProduct 아이스_아메리카노_한잔 = new MenuProduct(맥모닝콤보, 아이스_아메리카노_상품_ID, Quantity.of(1L));
    public static final MenuProduct 에그맥머핀_한개 = new MenuProduct(맥모닝콤보, 에그맥머핀_상품_ID, Quantity.of(1L));

    public static final MenuProductRequest 아이스_아메리카노_한잔_요청 = new MenuProductRequest(아이스_아메리카노_상품_ID, 1L);
    public static final MenuProductRequest 에그맥머핀_한개_요청 = new MenuProductRequest(에그맥머핀_상품_ID, 1L);
    public static final List<MenuProductRequest> 맥모닝_콤보_상품요청_목록 = Arrays.asList(아이스_아메리카노_한잔_요청, 에그맥머핀_한개_요청);
    public static final MenuRequest 맥모닝콤보_요청 = new MenuRequest(맥모닝콤보.getName(), 맥모닝콤보.getPrice().value(), 맥모닝콤보.getMenuGroupId(), 맥모닝_콤보_상품요청_목록);

    public static final MenuProductResponse 아이스_아메리카노_한잔_응답 = new MenuProductResponse(1L, 맥모닝콤보.getId(), 아이스_아메리카노_상품_ID, 1L);
    public static final MenuProductResponse 에그맥머핀_한개_응답 = new MenuProductResponse(1L, 맥모닝콤보.getId(), 에그맥머핀_상품_ID, 1L);
    public static final MenuResponse 맥모닝콤보_응답 = new MenuResponse(1L, 맥모닝콤보.getName(), 맥모닝콤보.getPrice().value(),
            맥모닝콤보.getMenuGroupId(), Arrays.asList(아이스_아메리카노_한잔_응답, 에그맥머핀_한개_응답));
    public static final MenuResponse 메뉴_두번째_응답 = new MenuResponse(2L, 맥모닝콤보.getName(), 맥모닝콤보.getPrice().value(),
            맥모닝콤보.getMenuGroupId(), Arrays.asList(아이스_아메리카노_한잔_응답, 에그맥머핀_한개_응답));
}
