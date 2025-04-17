package buffer.core;

public class AABB {
    public double minX, minY, minZ;
    public double maxX, maxY, maxZ;

    public AABB(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this.minX = minX; this.minY = minY; this.minZ = minZ;
        this.maxX = maxX; this.maxY = maxY; this.maxZ = maxZ;
    }

    public boolean intersects(AABB other) {
        return (this.maxX >= other.minX && this.minX <= other.maxX) &&
               (this.maxY >= other.minY && this.minY <= other.maxY) &&
               (this.maxZ >= other.minZ && this.minZ <= other.maxZ);
    }
}