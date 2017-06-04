/*
 * Don't sue me.
 */
package org.jimux.eve.blueprints;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/**
 *
 * @author James Smyth
 */
public class BlueprintLoader {
    public static void main(String[] args) throws Exception  {
        List<Blueprint> blueprints = getBlueprints("blueprints.yaml", "typeIDs.yaml");
        
        Blueprint blueprint = getBlueprintByName("Caracal Blueprint", blueprints);
        printBlueprintMaterialUsage(blueprint, 10);
    }
    
    public static Blueprint getBlueprintByName(String blueprintName, List<Blueprint> blueprints) {
        for (Blueprint blueprint : blueprints) {
            if (blueprint.getName() == null) {
                // Really shouldn't be any that don't have names set....
                continue;
            }
            if (blueprint.getName().equals(blueprintName)) {
                return blueprint;
            }
        }
        
        // Really, we're expecting 'Blueprint' to be in the name already, but let's help out anyway.
        for (Blueprint blueprint : blueprints) {
            if (blueprint.getName().equals(blueprintName + " Blueprint")) {
                return blueprint;
            }
        }
        
        // I don't know what you want. I give up.
        return null;
    }
    
    public static void printBlueprintMaterialUsage(Blueprint blueprint, int me) {
        System.out.println("Blueprint " + blueprint.getName() + " needs the following materials at " + me + " ME:");
        
        for (Material material : blueprint.getMaterials()) {
            double dquantity = material.getQuantity();
            double quantity = dquantity - ((dquantity / 100d) * (double) me);
            // Magic sauce to CCP's numbers is to always round up (here using ceil()) at the end.
            System.out.println(material.getName() + ": " +  (int) Math.ceil(quantity));
        }
    }
    
    public static List<Blueprint> getBlueprints(String blueprintsYamlPath, String itemsYamlPath) throws FileNotFoundException, YamlException {
        // Read in the items yaml file first so we can associate item IDs with actual names.
        YamlReader itemReader = new YamlReader(new FileReader(itemsYamlPath));
        Map itemList = (Map) itemReader.read();
        Iterator itemit = itemList.entrySet().iterator();
        HashMap<Integer, String> itemMap = new HashMap<>();
        while (itemit.hasNext()) {
            Map.Entry pair = (Map.Entry)itemit.next();
            Integer key = Integer.parseInt((String) pair.getKey());
            Map topMap = (Map) pair.getValue();
            
            // Yeah, english only. Sorry.
            itemMap.put(key, (String) ((Map)topMap.get("name")).get("en"));
        }
        
        // Now read in the blueprints yaml.
        YamlReader reader = new YamlReader(new FileReader(blueprintsYamlPath));
        Map blueprintList = (Map) reader.read();
        Iterator it = blueprintList.entrySet().iterator();
        
        List<Blueprint> blueprints = new ArrayList<Blueprint>();
        
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Integer key = Integer.parseInt((String) pair.getKey()); // This is the item ID of the blueprint
            Map topMap = (Map) pair.getValue();
            
            
            Blueprint blueprint = new Blueprint();
            blueprint.setId(key);
            // With the id of the blueprint, we can fetch the string value of the name from the item map.
            blueprint.setName(itemMap.get(key));
            
            List<Object> materials = null;
            if (topMap.containsKey("activities")) {
                Map activities = (Map) topMap.get("activities");
                if (activities.containsKey("manufacturing")) {
                    Map manufacturing = (Map) activities.get("manufacturing");
                    if (manufacturing.containsKey("materials")) {
                        materials = (List<Object>) manufacturing.get("materials");
                    }
                }
            }
            
            if (materials != null) {
                if (key == 687) {
                    System.out.println(materials);
                }
                for (Object materialObj : materials) {
                    Map materialEntry = (Map) materialObj;
                    Integer materialId = Integer.parseInt((String) materialEntry.get("typeID"));
                    
                    String materialName = (String) itemMap.get(materialId);
                    Integer quantity = Integer.parseInt((String) materialEntry.get("quantity"));
                    
                    Material material = new Material();
                    material.setId(materialId);
                    material.setQuantity(quantity);
                    material.setName(materialName);
                    blueprint.addMaterial(material);
                }
            }
            
            blueprints.add(blueprint);
        }
        
        return blueprints;
    }
}
