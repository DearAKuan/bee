package com.yzbubble.bee.pipeline;

@FunctionalInterface
public interface Filter {
    public void handle(FilterPipeline pipeline);
}
