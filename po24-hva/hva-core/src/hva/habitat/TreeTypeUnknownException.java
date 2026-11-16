package hva.habitat;

import java.io.Serial;

public class TreeTypeUnknownException extends Exception {

    @Serial
    private static final long serialVersionUID = 202407081733L;

    private final String _type;

    public TreeTypeUnknownException(String type) {
        _type = type;
    }

    public String getType() {
        return _type;
    }

}
