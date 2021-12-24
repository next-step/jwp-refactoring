package kitchenpos.tablegroup.event;

import kitchenpos.tablegroup.domain.GroupingPair;
import org.springframework.context.ApplicationEvent;

public class GroupEvent extends ApplicationEvent {
    public GroupEvent(GroupingPair groupingPair) {
        super(groupingPair);
    }

    public GroupingPair getGroupingPair() {
        return (GroupingPair) super.getSource();
    }
}
