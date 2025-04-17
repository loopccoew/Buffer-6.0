package buffer.core;

import java.util.ArrayList;
import java.util.List;

public class Octree {
    private AABB bounds;
    private List<GameObject> objects; //stores the blue boxes
    private Octree[] children;//subdivisions of transparent outer box
    private boolean isDivided = false;//flag which checks if the bound is subdivided

    public Octree(AABB bounds) {
        this.bounds = bounds;
        this.objects = new ArrayList<>();
    }

    public void insert(GameObject obj) {
        if (!bounds.intersects(obj.bounds)) return;

        if (!isDivided && objects.size() < 4) {
            objects.add(obj);
        } else {
            if (!isDivided) subdivide();
            for (Octree child : children) child.insert(obj);
        }
    }

    private void subdivide() { //divides space into octant when no of boxes in particular
        // octant are more than 4
        double midX = (bounds.minX + bounds.maxX) / 2;//calculating centre of the cube  (com)
        double midY = (bounds.minY + bounds.maxY) / 2;
        double midZ = (bounds.minZ + bounds.maxZ) / 2;
    
        children = new Octree[8];
        // Initialize ALL 8 children
        children[0] = new Octree(new AABB(bounds.minX, bounds.minY, bounds.minZ, midX, midY, midZ));
        children[1] = new Octree(new AABB(midX, bounds.minY, bounds.minZ, bounds.maxX, midY, midZ));
        children[2] = new Octree(new AABB(bounds.minX, midY, bounds.minZ, midX, bounds.maxY, midZ));
        children[3] = new Octree(new AABB(midX, midY, bounds.minZ, bounds.maxX, bounds.maxY, midZ));
        children[4] = new Octree(new AABB(bounds.minX, bounds.minY, midZ, midX, midY, bounds.maxZ));
        children[5] = new Octree(new AABB(midX, bounds.minY, midZ, bounds.maxX, midY, bounds.maxZ));
        children[6] = new Octree(new AABB(bounds.minX, midY, midZ, midX, bounds.maxY, bounds.maxZ));
        children[7] = new Octree(new AABB(midX, midY, midZ, bounds.maxX, bounds.maxY, bounds.maxZ));
        
        isDivided = true;//flag that stores if particular octant was subdivided or not
    }

    public List<GameObject> query(AABB range) {
        List<GameObject> found = new ArrayList<>();
        if (!bounds.intersects(range)) return found;

        for (GameObject obj : objects) {
            if (range.intersects(obj.bounds)) found.add(obj);
        }

        if (isDivided) {
            for (Octree child : children) {
                found.addAll(child.query(range));
            }
        }
        return found;
    }
}
