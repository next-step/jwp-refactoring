package kitchenpos.event;

import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MenuGroupEventHandler {

    @Autowired
    private MenuGroupService menuGroupService;

    @EventListener
    public void handler(MenuGroupEvent event){
        //MenuGroup menuGroup =menuGroupService.getMenuGroup(event.getMenuGroupId());
        //TODO
    }
}
