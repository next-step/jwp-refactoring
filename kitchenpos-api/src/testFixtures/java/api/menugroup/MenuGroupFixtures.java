package api.menugroup;

import api.menugroup.dto.MenuGroupRequest;

/**
 * packageName : kitchenpos.fixtures
 * fileName : MenuGroupFixtures
 * author : haedoang
 * date : 2021/12/17
 * description :
 */
public class MenuGroupFixtures {
    public static MenuGroupRequest 한마리메뉴그룹요청() {
        return MenuGroupRequest.of("한마리메뉴그룹요청");
    }

    public static MenuGroupRequest 두마리메뉴그룹요청() {
        return MenuGroupRequest.of("두마리메뉴그룹요청");
    }

    public static MenuGroupRequest 반반메뉴그룹요청() {
        return MenuGroupRequest.of("반반메뉴그룹요청");
    }

    public static MenuGroupRequest 메뉴그룹요청(String name) {
        return MenuGroupRequest.of(name);
    }
}
