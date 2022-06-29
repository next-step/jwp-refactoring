package kitchenpos.event.listener;

import kitchenpos.event.customEvent.MenuCreateEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class MenuCreateEventListener implements ApplicationListener<MenuCreateEvent> {

    @Override
    public void onApplicationEvent(MenuCreateEvent event) {
        System.out.println("MENUCREATEEVENT_PUBLISHED!!!");
    }
}
