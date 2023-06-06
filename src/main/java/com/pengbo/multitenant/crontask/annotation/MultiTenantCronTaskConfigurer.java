package com.pengbo.multitenant.crontask.annotation;

import com.pengbo.multitenant.tenantmgmt.TenantList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.ScheduledMethodRunnable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 多租定时任务扩展器：基于spring @Scheduled的扩展
 * 被 @MultiTenantCronTask注解修饰的方法会被替换成 MultiTenantRunnable的 run方法
 * run方法会通过遍历租户列表，调整线程上下文id，执行目标任务
 *
 * @author pengbo
 * @since 2022-8-22
 */
@Slf4j
public class MultiTenantCronTaskConfigurer implements SchedulingConfigurer {

    @Autowired
    private TenantList tenantList;

    /**
     * Spring初始化定时任务注册器时，调用该方法对注册器进行扩展
     * 1. 通过反射获取注册器中cronTasks任务列表
     * 2. 遍历cronTasks任务列表，获取存在@MultiTenantCronTasks注解的方法
     * 3. 创建MultiTenantRunnable任务替换原有任务实现多租任务
     *
     * @param taskRegistrar
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {

        /**
         * 1.获取spring {@link org.springframework.scheduling.annotation.Scheduled}修饰的定时任务
         */
        List<CronTask> originCrontasks = taskRegistrar.getCronTaskList();

        // 2.篡改originCronTasks封装成多租户执行的定时任务
        List<CronTask> tenantCronTasks = new ArrayList<>();

        for (CronTask originCronTask : originCrontasks) {
            CronTask newCronTask = convertConTaskWithTenant(originCronTask);
            tenantCronTasks.add(newCronTask);
        }

        // 3.使用多租定时任务multiTenantCronTasks重新设置到spring中
        taskRegistrar.setCronTasksList(tenantCronTasks);
    }

    /**
     * 多租化改造 spring定时任务
     *
     * @param originCronTask
     * @return
     */
    private CronTask convertConTaskWithTenant(CronTask originCronTask) {
        // 过滤只改造@Scheduled修饰的任务
        if (!(originCronTask.getRunnable() instanceof ScheduledMethodRunnable)) {
            return originCronTask;
        }
        // 过滤同时有@MultiTenantCronTask修饰的任务
        ScheduledMethodRunnable originRunnable = (ScheduledMethodRunnable) originCronTask.getRunnable();
        MultiTenantCronTask multiTenantCronTaskAnnotation = originRunnable.getMethod().getAnnotation(MultiTenantCronTask.class);
        if (multiTenantCronTaskAnnotation == null) {
            return originCronTask;
        }
        // 改造originRunnable -> multiTenantRunnable
        MultiTenantRunnable multiTenantRunnable = new MultiTenantRunnable(originRunnable.getTarget(),
                originRunnable.getMethod(), multiTenantCronTaskAnnotation.multiType());

        // 封装成新的cronTask
        return new CronTask(multiTenantRunnable, (CronTrigger) originCronTask.getTrigger());
    }

    /**
     * 多租户任务包装类，对MultiTenant注解修饰的方法进行包装执行
     * 在执行包装方法之前修改线程上下文tenantId内存
     */
    public class MultiTenantRunnable implements Runnable {
        Object target; // 目标对象

        Method method; // 目标方法

        String multiType; // 多租类型

        public MultiTenantRunnable(Object target, Method method, String multiType) {
            this.target = target;
            this.method = method;
            this.multiType = multiType;
        }

        /**
         * 交给ScheduledTaskRegistrar 执行的方法，在目标方法前，设置线程上下文tenantId
         */
        @Override
        public void run() {

            Set<String> tenantIds = StringUtils.equals(multiType, "STACK") ? tenantList.getStackTenantList() : tenantList.getTenantList();
            for (String tenantId : tenantIds) {
//                ContextManager.setCurrentTenantId(tenantId);
                try {
                    method.invoke(target);
                } catch (Exception e) {
//                    log.error(tenantId + "crontask error", e);
                }
            }
        }
    }
}
