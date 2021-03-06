package org.bahmni.mart.form2.translations.model;

import java.util.HashMap;
import java.util.Map;

public class Form2Translation {
    private String locale;
    private Map<String, String> labels = new HashMap<>();
    private Map<String, String> concepts = new HashMap<>();
    private String formName;
    private String version;

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, String> getConcepts() {
        return concepts;
    }

    public void setConcepts(Map<String, String> concepts) {
        this.concepts = concepts;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public void setLabels(Map<String, String> labels) {
        this.labels = labels;
    }

}
