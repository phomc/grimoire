package dev.phomc.grimoire.utils;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.GeometryUtils;
import org.joml.Vector3f;

public class MathUtils {
    // https://stackoverflow.com/a/31276717
    public static Vec3 rotate(Vec3 point, Vec3 axis, double radians){
        double x = point.x, y = point.y, z = point.z;
        double u = axis.x, v = axis.y, w = axis.z;
        double k = u * x + v * y + w * z;
        double xPrime = u * k * (1d - Math.cos(radians)) + x * Math.cos(radians) + (-w * y + v * z) * Math.sin(radians);
        double yPrime = v * k * (1d - Math.cos(radians)) + y * Math.cos(radians) + (w * x - u * z) * Math.sin(radians);
        double zPrime = w * k * (1d - Math.cos(radians)) + z * Math.cos(radians) + (-v * x + u * y) * Math.sin(radians);
        return new Vec3(xPrime, yPrime, zPrime);
    }

    public static Vec3 getDirection(Entity entity) {
        float xRot = (float) Math.toRadians(-entity.getXRot());
        float yRot = (float) Math.toRadians(-entity.getYRot());
        // TODO optimize this
        Vec3 p = new Vec3(Math.sin(yRot), 0, Math.cos(yRot));
        Vec3 y = new Vec3(0, 1, 0);
        Vec3 poy = p.cross(y);
        return rotate(p, poy, xRot);
    }

    public static Vec3[] getCircularPoints(Vec3 origin, Vec3 normal, float radius, int points) {
        Vector3f a = new Vector3f(), b = new Vector3f();
        GeometryUtils.perpendicular(normal.toVector3f(), a, b);
        Vec3[] list = new Vec3[points];
        for (int i = 0; i < list.length; i++) {
            double f = 2f * Math.PI * (i / (double) points);
            // https://math.stackexchange.com/a/175864
            float m = (float) (radius * Math.cos(f));
            float n = (float) (radius * Math.sin(f));
            list[i] = new Vec3(origin.toVector3f().add(new Vector3f(a).mul(m)).add(new Vector3f(b).mul(n)));
        }
        return list;
    }
}
