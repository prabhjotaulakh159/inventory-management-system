package prabhjot.safin.retail;

public enum Role {
    ADMIN(0),
    CUSTOMER(1);

    private final int number;

    private Role(int number) {
        this.number = number;
    }

    public int getRole() {
        return this.number;
    }
}
