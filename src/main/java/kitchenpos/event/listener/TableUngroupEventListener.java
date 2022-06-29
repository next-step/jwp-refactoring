package kitchenpos.event.listener;

import kitchenpos.event.customEvent.TableUngroupEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class TableUngroupEventListener implements ApplicationListener<TableUngroupEvent> {

    @Override
    public void onApplicationEvent(TableUngroupEvent event) {
        System.out.println("TableungroupEvent!!");
    }
}
