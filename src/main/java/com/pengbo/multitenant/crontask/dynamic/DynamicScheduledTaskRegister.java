package com.pengbo.multitenant.crontask.dynamic;

import lombok.Setter;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

public class DynamicScheduledTaskRegister implements DisposableBean {

    @Setter
    private TaskScheduler taskScheduler;

    private ScheduledExecutorService localExecutor;

    private Map<String, ScheduledFuture<?>> scheduledTasks;

    /**
     * 新增任务
     *
     * @param taskId
     * @param task
     * @param trigger
     */
    public void addTask(String taskId, Runnable task, Trigger trigger) {
        if (scheduledTasks == null) {
            scheduledTasks = new ConcurrentHashMap<>();

        }
        scheduledTasks.computeIfAbsent(taskId, key -> scheduledTask(task, trigger));
    }

    public void removeTask(String taskId) {
        ScheduledFuture<?> scheduledTask = this.scheduledTasks.remove(taskId);
        if (scheduledTask != null) {
            scheduledTask.cancel(true);
        }
    }

    /**
     * 调度任务，返回已提交任务的scheduledFuture
     * a task with a {@link ScheduledExecutorService}
     *
     * @param task
     * @param trigger
     * @return
     */
    private ScheduledFuture<?> scheduledTask(Runnable task, Trigger trigger) {
        if (this.taskScheduler == null) {
            this.localExecutor = Executors.newScheduledThreadPool(10);
            this.taskScheduler = new ConcurrentTaskScheduler(this.localExecutor);
        }
        return this.taskScheduler.schedule(task, trigger);
    }

    @Override
    public void destroy() throws Exception {
        for (ScheduledFuture<?> task : this.scheduledTasks.values()) {
            task.cancel(true);
        }
        if (this.localExecutor != null) {
            this.localExecutor.shutdownNow();
        }
    }
}
