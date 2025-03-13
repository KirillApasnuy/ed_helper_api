package ru.bezfy.ed_helper_api.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public class SubscriptionBody {
    @NotNull
    private String ruTitle;
    @NotNull
    private String enTitle;
    @NotNull
    private Boolean isPremium;
    @NotNull
    private Integer limitGenerations;
    @NotNull
    private Boolean isTTS;
    @NotNull
    private List<String> ruBenefits;
    @NotNull
    private List<String> enBenefits;
    @NotNull
    private BigDecimal amountPerMonth;
    @NotNull
    private BigDecimal amountPerMonthInYear;
    @NotNull
    private Boolean isAccessInGroup;

    public String getRuTitle() {
        return ruTitle;
    }

    public void setRuTitle(String ruTitle) {
        this.ruTitle = ruTitle;
    }

    public String getEnTitle() {
        return enTitle;
    }

    public void setEnTitle(String enTitle) {
        this.enTitle = enTitle;
    }

    @JsonProperty("isPremium")
    public Boolean isPremium() {
        return isPremium;
    }

    public void setPremium(Boolean premium) {
        isPremium = premium;
    }

    public Integer getLimitGenerations() {
        return limitGenerations;
    }

    public void setLimitGenerations(Integer limitGenerations) {
        this.limitGenerations = limitGenerations;
    }

    @JsonProperty("isTTS")
    public Boolean isTTS() {
        return isTTS;
    }

    public void setTTS(Boolean TTS) {
        isTTS = TTS;
    }

    public List<String> getRuBenefits() {
        return ruBenefits;
    }

    public void setRuBenefits(List<String> ruBenefits) {
        this.ruBenefits = ruBenefits;
    }

    public List<String> getEnBenefits() {
        return enBenefits;
    }

    public void setEnBenefits(List<String> enBenefits) {
        this.enBenefits = enBenefits;
    }

    public BigDecimal getAmountPerMonth() {
        return amountPerMonth;
    }

    public void setAmountPerMonth(BigDecimal amountPerMonth) {
        this.amountPerMonth = amountPerMonth;
    }

    public BigDecimal getAmountPerMonthInYear() {
        return amountPerMonthInYear;
    }

    public void setAmountPerMonthInYear(BigDecimal amountPerMonthInYear) {
        this.amountPerMonthInYear = amountPerMonthInYear;
    }

    @JsonProperty("isAccessInGroup")
    public Boolean isAccessInGroup() {
        return isAccessInGroup;
    }

    public void setAccessInGroup(Boolean accessInGroup) {
        isAccessInGroup = accessInGroup;
    }

    @Override
    public String toString() {
        return "SubscriptionBody{" +
                "ruTitle='" + ruTitle + '\'' +
                ", enTitle='" + enTitle + '\'' +
                ", isPremium=" + isPremium +
                ", limitGenerations=" + limitGenerations +
                ", isTTS=" + isTTS +
                ", ruBenefits=" + ruBenefits +
                ", enBenefits=" + enBenefits +
                ", amountPerMonth=" + amountPerMonth +
                ", amountPerMonthInYear=" + amountPerMonthInYear +
                ", isAccessInGroup=" + isAccessInGroup +
                '}';
    }
}
