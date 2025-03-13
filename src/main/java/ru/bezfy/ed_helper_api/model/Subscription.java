package ru.bezfy.ed_helper_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subscription")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "ruTitle", nullable = false)
    private String ruTitle;
    @Column(name = "enTitle", nullable = false)
    private String enTitle;

    @Column(name = "is_premium")
    private Boolean isPremium = false;

    @Column(name = "limit_generations")
    private Integer limitGenerations;

    @Column(name = "is_tts")
    private Boolean isTTS = false;

    @Column(name = "ruBenefits")
    @ElementCollection
    private List<String> ruBenefits;
    @Column(name = "enBenefits")
    @ElementCollection
    private List<String>enBenefits;
    @Column(name = "amount", nullable = false)
    private BigDecimal amountPerMonth;
    @Column(name = "amount_per_year", nullable = false)
    private BigDecimal amountPerMonthInYear;

    @Column(name = "is_access_in_group")
    private Boolean isAccessInGroup = false;
    @OneToMany(mappedBy = "subscription")
    @JsonIgnore
    private List<LocalUser> users;

    public Subscription(String ruTitle, String enTitle, Boolean isPremium, Integer limitGenerations, Boolean isTTS, List<String> ruBenefits, List<String> enBenefits, BigDecimal amountPerMonth, BigDecimal amountPerMonthInYear, Boolean isAccessInGroup) {
        this.ruTitle = ruTitle;
        this.enTitle = enTitle;
        this.isPremium = isPremium;
        this.limitGenerations = limitGenerations;
        this.isTTS = isTTS;
        this.ruBenefits = ruBenefits;
        this.enBenefits = enBenefits;
        this.amountPerMonth = amountPerMonth;
        this.amountPerMonthInYear = amountPerMonthInYear;
        this.isAccessInGroup = isAccessInGroup;
    }

    public Subscription() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Boolean getPremium() {
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

    public Boolean getTTS() {
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

    public Boolean isAccessInGroup() {
        return isAccessInGroup;
    }

    public void setAccessInGroup(Boolean accessInGroup) {
        isAccessInGroup = accessInGroup;
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

    public List<LocalUser> getUsers() {
        return users;
    }

    public void setUsers(List<LocalUser> users) {
        this.users = users;
    }
}
