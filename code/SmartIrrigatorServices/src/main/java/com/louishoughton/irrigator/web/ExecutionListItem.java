package com.louishoughton.irrigator.web;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.louishoughton.irrigator.job.Execution;


public class ExecutionListItem {

    public static final String RAIN_ICON = "rain_icon";
    public static final String SUN_ICON = "sun_icon";
    public static final String ERROR_ICON = "error_icon";

    private String date;
    private boolean didIrrigate;
    private boolean hasErrors;
    private int irrigationDuration;
    private String iconClass = RAIN_ICON; // Pessimistically assume rain

    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    ExecutionListItem(String date, boolean didIrrigate, int irrigationDuration, 
            String iconClass) {
        this.date = date;
        this.didIrrigate = didIrrigate;
        this.irrigationDuration = irrigationDuration;
        this.iconClass = iconClass;
    }

    public ExecutionListItem(Date date, Collection<Execution> executions) {
        this.date = formatter.format(date);
        executions.stream().forEach(this::addExecutionData);
        setIcon();
    }

    private void addExecutionData(Execution execution) {
        if (execution.getIrrigationDuration() > 0) {
            didIrrigate = true;
            irrigationDuration += execution.getIrrigationDuration();
        }
        if (execution.hasErrors()) {
            hasErrors = true;
        }
    }

    private void setIcon() {
        if (hasErrors) {
            iconClass = ERROR_ICON;
        } else {
            iconClass = irrigationDuration > 0 ? SUN_ICON : RAIN_ICON;
        }
    }

    public String getDate() {
        return date;
    }

    public boolean isDidIrrigate() {
        return didIrrigate;
    }

    public int getIrrigationDuration() {
        return irrigationDuration;
    }

    public String getIconClass() {
        return iconClass;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
