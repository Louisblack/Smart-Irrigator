package com.louishoughton.irrigator.job;

import com.louishoughton.irrigator.forecast.Forecast;
import com.louishoughton.irrigator.web.IrrigationRequest;
import com.louishoughton.irrigator.web.Error;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;
import static org.hibernate.annotations.CascadeType.SAVE_UPDATE;

@Entity
@Immutable
public class Execution {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;

    private Date dateRun;

    @OneToOne
    @Cascade(SAVE_UPDATE)
    private Forecast forecast;

    @OneToOne
    @Cascade(SAVE_UPDATE)
    private IrrigationRequest irrigationRequest;

    @OneToMany(mappedBy = "execution")
    @Cascade(SAVE_UPDATE)
    private List<com.louishoughton.irrigator.web.Error> errors;

    public Execution() {
        this.dateRun = new Date();
    }

    public Execution(Forecast forecast) {
        this.forecast = forecast;
    }

    public Execution(Forecast forecast, IrrigationRequest irrigationRequest) {
        this.forecast = forecast;
        this.irrigationRequest = irrigationRequest;
    }

    public Execution(Forecast forecast, IrrigationRequest irrigationRequest, List<Error> errors) {
        this(forecast, irrigationRequest);
        this.errors = errors;
        errors.stream().forEach(error -> error.setExecution(this));

    }

    public Execution(Error error, Error... errors) {
        this.errors = new ArrayList<>();
        this.errors.add(error);
        this.errors.addAll(Arrays.asList(errors));
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateRun() {
        return dateRun;
    }

    public void setDateRun(Date dateRun) {
        this.dateRun = dateRun;
    }

    public Forecast getForecast() {
        return forecast;
    }

    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }

    public IrrigationRequest getIrrigationRequest() {
        return irrigationRequest;
    }

    public void setIrrigationRequest(IrrigationRequest irrigationRequest) {
        this.irrigationRequest = irrigationRequest;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
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
