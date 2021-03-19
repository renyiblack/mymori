package dao.errors;

public class criticalArgumentNull extends Error{
    private static final long serialVersionUID = 1L;

    public criticalArgumentNull(String message) {
        super(message);
    }
}
