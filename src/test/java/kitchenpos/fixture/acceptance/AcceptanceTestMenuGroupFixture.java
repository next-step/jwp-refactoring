package kitchenpos.fixture.acceptance;

import kitchenpos.menu.acceptance.MenuGroupAcceptanceTest;
import kitchenpos.menu.domain.MenuGroup;

public class AcceptanceTestMenuGroupFixture {
    public final MenuGroup 구이류;
    public final MenuGroup 식사류;

    public AcceptanceTestMenuGroupFixture() {
        this.구이류 = MenuGroupAcceptanceTest.메뉴_그룹_생성_요청("구이류").as(MenuGroup.class);
        this.식사류 = MenuGroupAcceptanceTest.메뉴_그룹_생성_요청("식사류").as(MenuGroup.class);
    }
}
