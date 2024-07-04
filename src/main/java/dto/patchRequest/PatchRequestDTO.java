package dto.patchRequest;

import dto.helpers.Args;

import java.util.Map;

public class PatchRequestDTO {
    private Args args;
    private Map<String, Object> data;
    private Map<String, Object> files;
    private Map<String, Object> form;
    private Map<String, String> headers;
    private Object json;
    private String url;

    public Args getArgs() {
        return args;
    }

    public void setArgs(Args args) {
        this.args = args;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public Map<String, Object> getFiles() {
        return files;
    }

    public void setFiles(Map<String, Object> files) {
        this.files = files;
    }

    public Map<String, Object> getForm() {
        return form;
    }

    public void setForm(Map<String, Object> form) {
        this.form = form;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Object getJson() {
        return json;
    }

    public void setJson(Object json) {
        this.json = json;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}