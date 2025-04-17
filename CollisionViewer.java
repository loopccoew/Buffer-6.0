package buffer.visualization;
import java.util.List;
import buffer.core.*;//Imports GameObject, Octree, AABB classes.
import javafx.scene.*;//for rendering 3d objects
import javafx.scene.paint.*;//for colors
import javafx.scene.shape.*;//for shapes
import javafx.animation.AnimationTimer;//runs a loop that updates the simulation every frame 60fps

public class CollisionViewer {//this class initialised 3d objects visualises gameObjects
    // and collision using octree
    private Group root = new Group();
    //group is root node of 3d scene to which all the objects are added
    private Octree octree = new Octree(new AABB(-50, -50, -50, 50, 50, 50));
    //spatial tree that contains the simulation bounds
    private GameObject[] objects = new GameObject[20];
    //20 cubes stored in this array

    public CollisionViewer() {//constructor
       //each cube is being initialised randomly within a cube space
        for (int i = 0; i < objects.length; i++) {
            objects[i] = new GameObject(
                Math.random() * 80 - 40,
                Math.random() * 80 - 40,
                Math.random() * 80 - 40,
                1.0
            );
        }
    }

    public Parent createContent() {
        //this method sets up and starts animation (render loop)
        //returns node, which is the main scene node
        // FIXED: Added FPS control to AnimationTimer
        new AnimationTimer() {//animation timer is javafx class that lets you run code every frame
            private long lastTime = 0;
            
            @Override
            public void handle(long now) {
                if (now - lastTime < 16_666_666) return; // Strict 60 FPS
                update();
                lastTime = now;
            }
        }.start();

        return root;
    }

    private void update() {
        //this is the main loop
        //this update positions ,check the collisions , and renders cubes
        root.getChildren().clear();
        //clears previous cubes from the scene so that we can see current positions
        octree = new Octree(new AABB(-50, -50, -50, 50, 50, 50));
        //resets octree every frame
    
        // Update and check collisions
        for (GameObject obj : objects) {
            obj.update();
            octree.insert(obj);
            
            List<GameObject> candidates = octree.query(obj.bounds);
            //Returns a short list of potential colliders
            obj.isColliding = false;
            for (GameObject other : candidates) {
                if (obj != other && obj.bounds.intersects(other.bounds)) {
                    obj.isColliding = true;
                    break;
                }
            }
        }
    
        // Render objects
        for (GameObject obj : objects) {
            Box box = new Box(2, 2, 2);
            box.setTranslateX(obj.x);
            box.setTranslateY(obj.y);
            box.setTranslateZ(obj.z);
            box.setMaterial(new PhongMaterial(obj.isColliding ? Color.RED : Color.BLUE));
            root.getChildren().add(box);
        }
    }
}
