/*
 * Don't sue me.
 */
package org.jimux.eve.blueprints;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author James Smyth
 */
public class Blueprint {
    int id;
    String name;
    List<Material> materials = new ArrayList<>();

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

    public List<Material> getMaterials() {
        return materials;
    }

    public void setMaterials(List<Material> materials) {
        this.materials = materials;
    }
    
    public void addMaterial(Material material) {
        this.materials.add(material);
    }
}
