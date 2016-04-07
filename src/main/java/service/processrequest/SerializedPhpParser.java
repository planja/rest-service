package service.processrequest;

/**
 * Created by Никита on 07.04.2016.
 */

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SerializedPhpParser {
    private final String input;
    private int index;
    private boolean assumeUTF8 = true;
    private Pattern acceptedAttributeNameRegex = null;
    public static final Object NULL = new Object() {
        public String toString() {
            return "NULL";
        }
    };

    public SerializedPhpParser(String input) {
        this.input = input;
    }

    public SerializedPhpParser(String input, boolean assumeUTF8) {
        this.input = input;
        this.assumeUTF8 = assumeUTF8;
    }

    public Object parse() {
        char type = this.input.charAt(this.index);
        switch (type) {
            case 'N':
                this.index += 2;
                return NULL;
            case 'O':
                this.index += 2;
                return this.parseObject();
            case 'a':
                this.index += 2;
                return this.parseArray();
            case 'b':
                this.index += 2;
                return this.parseBoolean();
            case 'd':
                this.index += 2;
                return this.parseFloat();
            case 'i':
                this.index += 2;
                Object tmp = this.parseInt();
                if (tmp == null) {
                    tmp = this.parseFloat();
                }

                return tmp;
            case 's':
                this.index += 2;
                return this.parseString();
            default:
                throw new IllegalStateException("Encountered unknown type [" + type + "]");
        }
    }

    private Object parseObject() {
        SerializedPhpParser.PhpObject phpObject = new SerializedPhpParser.PhpObject();
        int strLen = this.readLength();
        phpObject.name = this.input.substring(this.index, this.index + strLen);
        this.index = this.index + strLen + 2;
        int attrLen = this.readLength();

        for (int i = 0; i < attrLen; ++i) {
            Object key = this.parse();
            Object value = this.parse();
            if (this.isAcceptedAttribute(key)) {
                phpObject.attributes.put(key, value);
            }
        }

        ++this.index;
        return phpObject;
    }

    private Map<Object, Object> parseArray() {
        int arrayLen = this.readLength();
        LinkedHashMap result = new LinkedHashMap();

        for (int i = 0; i < arrayLen; ++i) {
            Object key = this.parse();
            Object value = this.parse();
            if (this.isAcceptedAttribute(key)) {
                result.put(key, value);
            }
        }

        ++this.index;
        return result;
    }

    private boolean isAcceptedAttribute(Object key) {
        return this.acceptedAttributeNameRegex == null ? true : (!(key instanceof String) ? true : this.acceptedAttributeNameRegex.matcher((String) key).matches());
    }

    private int readLength() {
        int delimiter = this.input.indexOf(58, this.index);
        int arrayLen = Integer.valueOf(this.input.substring(this.index, delimiter)).intValue();
        this.index = delimiter + 2;
        return arrayLen;
    }

    private String parseString() {
        int strLen = this.readLength();
        int utfStrLen = 0;
        int byteCount = 0;

        while (true) {
            while (true) {
                while (byteCount != strLen) {
                    char value = this.input.charAt(this.index + utfStrLen++);
                    if (this.assumeUTF8) {
                        if (value >= 1 && value <= 127) {
                            ++byteCount;
                        } else if (value > 2047) {
                            byteCount += 3;
                        } else {
                            byteCount += 2;
                        }
                    } else {
                        ++byteCount;
                    }
                }

                String var5 = this.input.substring(this.index, this.index + utfStrLen);
                this.index = this.index + utfStrLen + 2;
                return var5;
            }
        }
    }

    private Boolean parseBoolean() {
        int delimiter = this.input.indexOf(59, this.index);
        String value = this.input.substring(this.index, delimiter);
        if (value.equals("1")) {
            value = "true";
        } else if (value.equals("0")) {
            value = "false";
        }

        this.index = delimiter + 1;
        return Boolean.valueOf(value);
    }

    private Double parseFloat() {
        int delimiter = this.input.indexOf(59, this.index);
        String value = this.input.substring(this.index, delimiter);
        this.index = delimiter + 1;
        return Double.valueOf(value);
    }

    private Integer parseInt() {
        int delimiter = this.input.indexOf(59, this.index);
        int index_old = this.index;
        String value = this.input.substring(this.index, delimiter);
        this.index = delimiter + 1;

        try {
            return Integer.valueOf(value);
        } catch (Exception var5) {
            this.index = index_old;
            return null;
        }
    }

    public void setAcceptedAttributeNameRegex(String acceptedAttributeNameRegex) {
        this.acceptedAttributeNameRegex = Pattern.compile(acceptedAttributeNameRegex);
    }

    public static class PhpObject {
        public String name;
        public Map<Object, Object> attributes = new HashMap();

        public PhpObject() {
        }

        public String toString() {
            return "\"" + this.name + "\" : " + this.attributes.toString();
        }
    }
}
