/*
 * Don't sue me.
 */
package org.jimux.eve.blueprints;

/**
 *
 * @author James Smyth
 */
public class Material {
    int id;
    String name;
    int quantity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
