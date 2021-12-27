package kitchenpos.tablegroup.event;

import org.springframework.context.ApplicationEvent;

public class GroupEvent extends ApplicationEvent {

    private GroupInfo groupInfo;

    public GroupEvent(Object source, GroupInfo groupInfo) {
        super(source);
        this.groupInfo = groupInfo;
    }

    public GroupInfo getGroupInfo() {
        return groupInfo;
    }
}
