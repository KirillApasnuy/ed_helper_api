package ru.bezfy.ed_helper_api.api.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class    AuthorizationModel {

    @NotNull
    @NotEmpty
    @Email(message = "Email is not valid")
    private String email;
    @NotNull
    @NotEmpty(message = "Password is not valid")
    @Length(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private String uploadFolderId;
    private Boolean isReceivedEmail;

    public @NotNull @NotEmpty @Email(message = "Email is not valid") String getEmail() {
        return email;
    }

    public void setEmail(@NotNull @NotEmpty @Email(message = "Email is not valid") String email) {
        this.email = email;
    }

    public @NotNull @NotEmpty(message = "Password is not valid") @Length(min = 6, message = "Password must be at least 6 characters") String getPassword() {
        return password;
    }

    public void setPassword(@NotNull @NotEmpty(message = "Password is not valid") @Length(min = 6, message = "Password must be at least 6 characters") String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "AuthorizationModel{" + "email='" + email + '\'' + ", password='" + password + '\'';
    }

    public String getUploadFolderId() {
        return uploadFolderId;
    }

    public void setUploadFolderId(String uploadFolderId) {
        this.uploadFolderId = uploadFolderId;
    }

    public Boolean getReceivedEmail() {
        return isReceivedEmail;
    }

    public void setReceivedEmail(Boolean receivedEmail) {
        isReceivedEmail = receivedEmail;
    }
}
