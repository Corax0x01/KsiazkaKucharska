package szymanski.jakub.backend.common;

/**
 * Template used for mapping between Entities and DTO's of different classes
 */
public interface Mapper<T, S> {

    /**
     * Maps from class <code>T</code> to <code>S</code>.
     */
    S mapTo(T t);

    /**
     * Maps from class <code>S</code> to <code>T</code>.
     */
    T mapFrom(S s);
}
