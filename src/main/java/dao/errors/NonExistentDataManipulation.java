package dao.errors;

public class NonExistentDataManipulation extends Error {
    private static final long serialVersionUID = 1L;

    public NonExistentDataManipulation(String message) {
        super(message);
    }
}
