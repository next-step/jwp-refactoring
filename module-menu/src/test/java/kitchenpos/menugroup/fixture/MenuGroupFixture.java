package kitchenpos.menugroup.fixture;

import kitchenpos.common.domain.Name;
import kitchenpos.menugroup.domain.MenuGroup;

public class MenuGroupFixture {
    public static MenuGroup 추천_메뉴 = create(1L, Name.of("추천 메뉴"));
    public static MenuGroup 강력_추천_메뉴 = create(2L, Name.of("강력_추천_메뉴"));

    public static MenuGroup create(Long id, Name name) {
        return MenuGroup.of(id, name);
    }
}
