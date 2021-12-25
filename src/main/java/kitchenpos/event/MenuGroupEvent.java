package kitchenpos.event;

public class MenuGroupEvent {
    private Long menuGroupId;

    private MenuGroupEvent(Long menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public static MenuGroupEvent from(Long menuGroupId){
        return new MenuGroupEvent(menuGroupId);
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }
}
