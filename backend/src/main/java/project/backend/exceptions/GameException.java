package project.backend.exceptions;

public class GameException extends Exception {
    public enum TYPE {
        GAME_NOT_STARTED_OR_ENDED("game not started or already ended"),
        GAME_ENDED("game ended"),
        WRONG_TURN("wrong turn"),
        INVALID_MOVE("invalid move");

        private final String message;
        TYPE(String message) {
            this.message = message;
        }
    }
    public GameException(TYPE type) {
        super(type.message);
    }
}
