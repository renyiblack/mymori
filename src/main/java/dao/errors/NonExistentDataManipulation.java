package dao.errors;

public class NonExistentDataManipulation extends Error {
    public NonExistentDataManipulation(String message) {
        super(message);
    }
}
