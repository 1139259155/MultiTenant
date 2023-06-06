package com.pengbo.multitenant.crontask.dynamic;

import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.support.CronTrigger;

import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.function.Supplier;

public class DynamicCronTrigger implements Trigger {

    private CronTrigger cacheTrigger;

    /**
     * cron表达式提供器
     */
    private final Supplier<String> expressionSupplier;

    public DynamicCronTrigger(Supplier<String> expressionSupplier) {
        this.expressionSupplier = expressionSupplier;
        cacheTrigger = new CronTrigger(expressionSupplier.get(), ZoneId.systemDefault());
    }

    /**
     * @param triggerContext
     * @return
     */

    @Override
    public Date nextExecutionTime(TriggerContext triggerContext) {
        // 获取最新的cron表达式
        String expression = expressionSupplier.get();
        refreshExpression(expression);
        return cacheTrigger.nextExecutionTime(triggerContext);
    }

    /**
     * 获取并刷新当前cron表达式
     *
     * @return 当前最新的cron表达式
     */
    public String getExpression() {
        String expression = expressionSupplier.get();
        refreshExpression(expression);
        return expression;
    }

    private void refreshExpression(String expression) {
        if (StringUtils.isEmpty(expression)) {
            return;
        }
        // 如果获取到的cron表达式和缓存的不一致，则根据新表达式新建一个CronTrigger
        if (!Objects.equals(cacheTrigger.getExpression(), expression))
            cacheTrigger = new CronTrigger(expression, ZoneId.systemDefault());
    }
}
