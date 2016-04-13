package vo.view;

import java.io.Serializable;

public class ExtraData implements Serializable {

    private String fieldName;
    private String fieldType;
    private String fieldValue;
    private String fieldLvl;
    private String fieldSubLvl;
    private String fieldId;

    public ExtraData() {
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getFieldLvl() {
        return fieldLvl;
    }

    public void setFieldLvl(String fieldLvl) {
        this.fieldLvl = fieldLvl;
    }

    public String getFieldSubLvl() {
        return fieldSubLvl;
    }

    public void setFieldSubLvl(String fieldSubLvl) {
        this.fieldSubLvl = fieldSubLvl;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExtraData extraData = (ExtraData) o;

        if (fieldId != null ? !fieldId.equals(extraData.fieldId) : extraData.fieldId != null) return false;
        if (fieldLvl != null ? !fieldLvl.equals(extraData.fieldLvl) : extraData.fieldLvl != null) return false;
        if (fieldName != null ? !fieldName.equals(extraData.fieldName) : extraData.fieldName != null) return false;
        if (fieldSubLvl != null ? !fieldSubLvl.equals(extraData.fieldSubLvl) : extraData.fieldSubLvl != null)
            return false;
        if (fieldType != null ? !fieldType.equals(extraData.fieldType) : extraData.fieldType != null) return false;
        if (fieldValue != null ? !fieldValue.equals(extraData.fieldValue) : extraData.fieldValue != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = fieldName != null ? fieldName.hashCode() : 0;
        result = 31 * result + (fieldType != null ? fieldType.hashCode() : 0);
        result = 31 * result + (fieldValue != null ? fieldValue.hashCode() : 0);
        result = 31 * result + (fieldLvl != null ? fieldLvl.hashCode() : 0);
        result = 31 * result + (fieldSubLvl != null ? fieldSubLvl.hashCode() : 0);
        result = 31 * result + (fieldId != null ? fieldId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ExtraData{" +
                "fieldName='" + fieldName + '\'' +
                ", fieldType='" + fieldType + '\'' +
                ", fieldValue='" + fieldValue + '\'' +
                ", fieldLvl='" + fieldLvl + '\'' +
                ", fieldSubLvl='" + fieldSubLvl + '\'' +
                ", fieldId='" + fieldId + '\'' +
                '}';
    }
}
