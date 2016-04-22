package com.guru.vo.view;

public class IMTError {

    private Integer code;
    private String description;

    public IMTError() {
    }

    public IMTError(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IMTError imtError = (IMTError) o;

        if (code != null ? !code.equals(imtError.code) : imtError.code != null) return false;
        if (description != null ? !description.equals(imtError.description) : imtError.description != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "IMTError{" +
                "code=" + code +
                ", description='" + description + '\'' +
                '}';
    }
}
