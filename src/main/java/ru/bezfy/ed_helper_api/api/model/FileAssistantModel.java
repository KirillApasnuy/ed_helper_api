package ru.bezfy.ed_helper_api.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class FileAssistantModel {
    private List<Section> sections;

    // Getters and Setters
    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Section {
        private int id;
        private String title;
        private List<Subcategory> subcategories;

        // Getters and Setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<Subcategory> getSubcategories() {
            return subcategories;
        }

        public void setSubcategories(List<Subcategory> subcategories) {
            this.subcategories = subcategories;
        }
    }

    public static class Subcategory {
        private String id;
        private String title;
        private List<Content> content;

        // Getters and Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<Content> getContent() {
            return content;
        }

        public void setContent(List<Content> content) {
            this.content = content;
        }
    }

    public static class Content {
        private String id;
        private String question;
        private String answer;
        private List<String> media;

        // Getters and Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public List<String> getMedia() {
            return media;
        }

        public void setMedia(List<String> media) {
            this.media = media;
        }
    }
}