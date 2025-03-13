package ru.bezfy.ed_helper_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import ru.bezfy.ed_helper_api.model.enums.SubscribeState;
import ru.bezfy.ed_helper_api.model.enums.UserType;

import java.util.*;

@Entity
@Table(name = "local_user")
public class LocalUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;
    @Column(name = "date_created", nullable = false)
    private Date dateCreated = new Date();
    @Column(name = "user_type", nullable = false)
    private UserType userType = UserType.USER;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "password", length = 1024)
    @JsonIgnore
    private String password;
    @Column(name = "upload_folder_id")
    private String uploadFolderId;
    @Column(name = "used_tokens_in_period")
    private Long usedTokensInPeriod = 0L;
    @Column(name = "used_tokens_in_period_in_last_month")
    private Long usedTokensInPeriodInLastMonth = 0L;
    @Column(name = "count_generation_in_last_month")
    private Long countGenerationInLastMonth = 0L;
    @Column(name = "subscribe_state")
    private SubscribeState subscribeState = SubscribeState.UNSUBSCRIBED;
    @Column(name = "paid_start_date")
    private Date paidStartDate;
    @Column(name = "paid_end_date")
    private Date paidEndDate;
    @Column(name = "is_blocked")
    private Boolean isBlocked = false;
    @Column(name = "reason_for_blocked")
    private String reasonForBlocked;
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id desc")
    private List<VerificationToken> verificationTokens = new ArrayList<>();
    @Column(name = "email_verified")
    private Boolean emailVerified = false;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "subscription_id", referencedColumnName = "id")
    private Subscription subscription;
    @Column(name = "is_auto_renewal")
    private Boolean isAutoRenewal = false;
    @Column(name = "receive_email")
    private Boolean ReceiveEmail = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getUsedTokensInPeriod() {
        return usedTokensInPeriod;
    }

    public void setUsedTokensInPeriod(Long usedTokensInPeriod) {
        this.usedTokensInPeriod = usedTokensInPeriod;
    }

    public Long getUsedTokensInPeriodInLastMonth() {
        return usedTokensInPeriodInLastMonth;
    }

    public void setUsedTokensInPeriodInLastMonth(Long usedTokensInPeriodInLastMonth) {
        this.usedTokensInPeriod = this.usedTokensInPeriod + usedTokensInPeriodInLastMonth;
        this.usedTokensInPeriodInLastMonth = this.usedTokensInPeriodInLastMonth + usedTokensInPeriodInLastMonth;
    }

    public SubscribeState getSubscribeState() {
        return subscribeState;
    }

    public void setSubscribeState(SubscribeState subscribeState) {
        this.subscribeState = subscribeState;
    }

    public Date getPaidStartDate() {
        return paidStartDate;
    }

    public void setPaidStartDate(Date paidStartDate) {
        this.paidStartDate = paidStartDate;
    }

    public Date getPaidEndDate() {
        return paidEndDate;
    }

    public void setPaidEndDate(Date paidEndDate) {
        this.paidEndDate = paidEndDate;
    }
    public Boolean getBlocked() {
        return isBlocked;
    }

    public void setBlocked(Boolean blocked) {
        isBlocked = blocked;
    }

    public String getReasonForBlocked() {
        return reasonForBlocked;
    }

    public void setReasonForBlocked(String reasonForBlocked) {
        this.reasonForBlocked = reasonForBlocked;
    }

    public List<VerificationToken> getVerificationTokens() {
        return verificationTokens;
    }

    public void setVerificationTokens(List<VerificationToken> verificationTokens) {
        this.verificationTokens = verificationTokens;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    @Override
    public String toString() {
        return "LocalUser{" + "dateCreated=" + dateCreated + ", userType=" + userType + ", email='" + email + '\'' + ", password='" + password + '\'' + ", usedTokensInPeriod=" + usedTokensInPeriod + ", usedTokensInPeriodInLastMonth=" + "usedTokensInPeriodIn";
    }

    public Long getCountGenerationInLastMonth() {
        return countGenerationInLastMonth;
    }

    public void setCountGenerationInLastMonth(Long countGenerationInLastMonth) {
        this.countGenerationInLastMonth = countGenerationInLastMonth;
    }

    public Boolean getAutoRenewal() {
        return isAutoRenewal;
    }

    public void setAutoRenewal(Boolean autoRenewal) {
        isAutoRenewal = autoRenewal;
    }

    public String getUploadFolderId() {
        return uploadFolderId;
    }

    public void setUploadFolderId(String uploadFolderId) {
        this.uploadFolderId = uploadFolderId;
    }

    public Boolean getReceiveEmail() {
        return ReceiveEmail;
    }

    public void setReceiveEmail(Boolean receiveEmail) {
        ReceiveEmail = receiveEmail;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }
}
