package com.yzbubble.bee.pipeline;

import java.util.Objects;
import java.util.UUID;

public class FilterNode {
    private String id;
    private String group;
    private Filter filter;

    public FilterNode(String id, String group, Filter filter) {
        this.setId(id);
        this.setGroup(group);
        this.setFilter(filter);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id == null || id.isEmpty()) {
            this.id = UUID.randomUUID().toString();
        }
        this.id = id;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        if (group == null || group.isEmpty()) {
            this.group = "default";
        }
        this.group = group;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilterNode that = (FilterNode) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(group, that.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, group);
    }
}
