package kitchenpos.table.application;

import kitchenpos.tablegroup.domain.TableGroupEvent;
import kitchenpos.tablegroup.domain.TableUngroupEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * packageName : kitchenpos.table.application
 * fileName : TableGroupEventListener
 * author : haedoang
 * date : 2021-12-27
 * description :
 */
@Component
public class TableEventHandler {

    @EventListener
    public void groupEvent(TableGroupEvent event) {
        System.out.println("==================group event invoke =====================");

        System.out.println(event);

        System.out.println("==================group event invoke =====================");
    }


    @EventListener
    public void ungroupEvent(TableUngroupEvent event) {
        System.out.println("==================ungroup event invoke =====================");

        System.out.println(event);

        System.out.println("==================ungroup event invoke =====================");
    }

}
