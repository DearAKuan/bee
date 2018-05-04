package com.yzbubble.bee.pipeline;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.stream.Collectors;

public class FilterPipeline {
    private LinkedList<FilterNode> filters = new LinkedList<>();
    private ShippingPackage shippingPackage;
    private ListIterator<FilterNode> listIterator;

    public FilterPipeline(ShippingPackage shippingPackage) {
        this.shippingPackage = shippingPackage;
        listIterator = filters.listIterator();
    }

    public static FilterPipeline create(ShippingPackage shippingPackage) {
        return new FilterPipeline(shippingPackage);
    }

    public FilterPipeline register(FilterNode node) {
        filters.add(node);
        return this;
    }

    public FilterPipeline unregister(FilterNode node) {
        filters.remove(node);
        return this;
    }

    public FilterPipeline unregisterById(String id) {
        filters.removeAll(filters.stream().filter(x -> x.getId().equals(id)).collect(Collectors.toList()));
        return this;
    }

    public FilterPipeline unregisterByGroup(String group) {
        filters.removeAll(filters.stream().filter(x -> x.getGroup().equals(group)).collect(Collectors.toList()));
        return this;
    }

    public void next() {
        if (listIterator.hasNext()) {
            listIterator.next().getFilter().handle(this);
        }
        filters.listIterator();
    }

    public void reset() {
        listIterator = filters.listIterator();
    }

    public void handle() {
    }
}
