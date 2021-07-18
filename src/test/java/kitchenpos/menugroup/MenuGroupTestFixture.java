package kitchenpos.menugroup;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;

public class MenuGroupTestFixture {

    public static final MenuGroupRequest 맥모닝_메뉴그룹_요청 = new MenuGroupRequest("맥모닝_메뉴그룹");
    public static final  MenuGroupRequest 맥디너_메뉴그룹_요청 = new MenuGroupRequest("맥디너_메뉴그룹");
    public static final  MenuGroupResponse 맥모닝_메뉴그룹_응답 = new MenuGroupResponse(1L, "맥모닝_메뉴그룹");
    public static final  MenuGroupResponse 맥디너_메뉴그룹_응답 = new MenuGroupResponse(2L, "맥디너_메뉴그룹");

    public static final  MenuGroup 맥모닝_메뉴그룹 = new MenuGroup(1L, "맥모닝_메뉴그룹");
    public static final  MenuGroup 맥디너_메뉴그룹 = new MenuGroup(2L, "맥디너_메뉴그룹");
}
