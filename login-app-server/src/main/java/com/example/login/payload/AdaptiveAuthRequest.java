package com.example.login.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AdaptiveAuthRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String authFactor;

//    @NotBlank
    @Size(max = 20)
    private String question;

//    @NotBlank
    @Size(max = 20)
    private String answer;

    @Size(max = 20)
    private String code;

    public AdaptiveAuthRequest(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAuthFactor() {
        return authFactor;
    }

    public void setAuthFactor(String authFactor) {
        this.authFactor = authFactor;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
