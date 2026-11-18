package erp.basket;

/**
 * Represents the status of trying to checkout.
 */
public enum CheckoutStatus{
    ERROR,
    CANT_CHECKOUT_EMPTY_BASKET,
    NO_TRANSPORT_METHOD,
    NOT_ENOUGH,
    SUCCESS
}